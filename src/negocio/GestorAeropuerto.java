package negocio;

import modelo.*;
import java.util.ArrayList;

public class GestorAeropuerto {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioActivo = null;
    private ArrayList<Vuelo> vuelos = new ArrayList<>();
    private ArrayList<Puerta> puertas = new ArrayList<>();
    private ArrayList<AsignacionPuerta> asignaciones = new ArrayList<>();
    private ArrayList<Equipaje> equipajes = new ArrayList<>();
    private ArrayList<String> aerolineas = new ArrayList<>();
    private int contEtiquetas = 1000, contVuelos = 100;

    public GestorAeropuerto() {
        puertas.add(new Puerta("B1", "Internacional", "Operativa"));
        puertas.add(new Puerta("B2", "Internacional", "Operativa"));
        puertas.add(new Puerta("B3", "Internacional", "Operativa"));
        puertas.add(new Puerta("B4", "Internacional", "En mantenimiento"));

        aerolineas.add("Avianca");
        aerolineas.add("LATAM");
        aerolineas.add("Aeroregional");
        aerolineas.add("Copa Airlines");
    }

    public ArrayList<String> getAerolineas() { return aerolineas; }
    public boolean haySesionActiva() { return usuarioActivo != null; }
    public Usuario getUsuarioActivo() { return usuarioActivo; }
    public void cerrarSesion() { usuarioActivo = null; }

    public Usuario buscarUsuario(String doc) {
        for (Usuario u : usuarios) if (u.getDocumento().equals(doc)) return u;
        return null;
    }

    public String registrarUsuario(String doc, String nom, String clave) {
        if (buscarUsuario(doc) != null) return "Error: Cedula/Pasaporte ya registrado.";
        usuarioActivo = new Usuario(doc, nom, clave);
        usuarios.add(usuarioActivo);
        return "Usuario " + nom + " registrado e iniciado sesion.";
    }

    public String iniciarSesion(String doc, String clave) {
        Usuario u = buscarUsuario(doc);
        if (u == null || !u.getClave().equals(clave)) return "Error: Datos incorrectos.";
        usuarioActivo = u;
        return "Bienvenido/a, " + u.getNombre() + ".";
    }

    public boolean tieneVuelosUsuario() {
        if (!haySesionActiva()) return false;
        for (Vuelo v : vuelos) if (v.getDocumentoUsuario().equals(usuarioActivo.getDocumento())) return true;
        return false;
    }

    public boolean tieneEquipajesUsuario() {
        for (Equipaje e : equipajes) if (buscarVueloUsuario(e.getCodigoVuelo()) != null) return true;
        return false;
    }

    // --- MODULO 1: VUELOS ---
    public Vuelo buscarVueloUsuario(String cod) {
        if (!haySesionActiva()) return null;
        for (Vuelo v : vuelos) {
            if (v.getCodigo().equalsIgnoreCase(cod) && v.getDocumentoUsuario().equals(usuarioActivo.getDocumento())) return v;
        }
        return null;
    }

    public String registrarVuelo(String aero, String orig, String dest, int anio, int mes, int dia, double hora, ArrayList<Pasajero> pasajeros) {
        contVuelos++;
        String cod = "VUE-" + contVuelos;
        Vuelo nuevo = new Vuelo(cod, aero, orig, dest, anio, mes, dia, hora, pasajeros, usuarioActivo.getDocumento());
        vuelos.add(nuevo);
        return "Vuelo Internacional " + cod + " registrado para el " + nuevo.getFechaFormateada() + " (" + pasajeros.size() + " pasajero/s).";
    }

    public String actualizarEstadoVuelo(String cod, String est, String mot, double nuevaHora) {
        Vuelo v = buscarVueloUsuario(cod);
        if (v == null || !v.puedeCambiarA(est)) return "Error: Cambio no permitido.";
        if (est.equals("Retrasado")) v.setHoraSalida(nuevaHora);
        v.setEstado(est);
        v.setMotivo(mot);

        // Actualizacion automatica operativa de maletas segun avance del vuelo
        for (Equipaje eq : equipajes) {
            if (eq.getCodigoVuelo().equalsIgnoreCase(cod) && !eq.getEstado().equals("EXTRAVIADO")) {
                if (est.equals("Cancelado")) {
                    eq.setEstado("RETENIDO");
                } else if (est.equals("Embarcando") || est.equals("Embarcado")) {
                    eq.setEstado("CARGADO");
                } else if (est.equals("Despegado")) {
                    eq.setEstado("EN TRANSITO");
                }
            }
        }

        if (est.equals("Cancelado")) {
            asignaciones.removeIf(a -> a.getCodigoVuelo().equalsIgnoreCase(cod));
            return "Vuelo cancelado. Se libero la puerta y se retuvo el equipaje.";
        }
        return "Vuelo " + cod + " actualizado a " + est + ".";
    }

    public String consultarVuelos() {
        String txt = "=== MIS VUELOS (" + usuarioActivo.getNombre() + ") ===\n";
        int c = 0;
        for (Vuelo v : vuelos) {
            if (v.getDocumentoUsuario().equals(usuarioActivo.getDocumento())) {
                c++;
                txt += v.getCodigo() + " | " + v.getAerolinea() + " | " + v.getOrigen() + " -> " + v.getDestino() +
                        " | Fecha: " + v.getFechaFormateada() + " | Salida: " + v.getHoraSalida() +
                        "h | Pasajeros: " + v.getPasajeros().size() + " | Estado: " + v.getEstado() + "\n";
            }
        }
        return c == 0 ? "No tiene vuelos registrados." : txt;
    }

    // --- MODULO 2: PUERTAS ---
    public Puerta buscarPuerta(String id) {
        for (Puerta p : puertas) if (p.getIdPuerta().equalsIgnoreCase(id)) return p;
        return null;
    }

    private double calcularHoraInicioEmbarque(double horaSalida) {
        double hIni = horaSalida - 1.0;
        if (hIni < 0.0) hIni = 0.0;
        return Math.round(hIni * 100.0) / 100.0;
    }

    public String consultarDisponibilidadPuertas(double hIni, double hFin) {
        String txt = "=== DISPONIBILIDAD PUERTAS INTERNACIONALES (" + hIni + "h - " + hFin + "h) ===\n";
        for (Puerta p : puertas) {
            if (!p.estaOperativa()) {
                txt += p.getIdPuerta() + " -> En mantenimiento\n";
            } else {
                boolean ocupada = false;
                for (AsignacionPuerta a : asignaciones) {
                    if (a.getIdPuerta().equalsIgnoreCase(p.getIdPuerta()) && a.seCruzaCon(hIni, hFin)) ocupada = true;
                }
                txt += p.getIdPuerta() + " -> " + (ocupada ? "OCUPADA" : "LIBRE") + "\n";
            }
        }
        return txt;
    }

    public String asignarPuertaAutomatica(Vuelo v) {
        for (AsignacionPuerta a : asignaciones) {
            if (a.getCodigoVuelo().equalsIgnoreCase(v.getCodigo())) {
                return "Error: Este vuelo ya tiene asignada la puerta " + a.getIdPuerta() + ".";
            }
        }

        double hIni = calcularHoraInicioEmbarque(v.getHoraSalida());
        double hFin = v.getHoraSalida();

        for (Puerta p : puertas) {
            if (p.estaOperativa()) {
                boolean ocupada = false;
                for (AsignacionPuerta a : asignaciones) {
                    if (a.getIdPuerta().equalsIgnoreCase(p.getIdPuerta()) && a.seCruzaCon(hIni, hFin)) {
                        ocupada = true;
                    }
                }
                if (!ocupada) {
                    asignaciones.add(new AsignacionPuerta(v.getCodigo(), p.getIdPuerta(), hIni, hFin));
                    return "Puerta asignada automaticamente: " + p.getIdPuerta() +
                            " al vuelo " + v.getCodigo() + " (" + hIni + "h - " + hFin + "h).";
                }
            }
        }
        return "Error: No hay puertas disponibles en el horario " + hIni + "h - " + hFin + "h.";
    }

    public String consultarAsignacionesPuertas() {
        String txt = "=== ASIGNACIONES ===\n";
        if (asignaciones.isEmpty()) {
            txt += "No hay puertas asignadas actualmente.\n";
        } else {
            for (AsignacionPuerta a : asignaciones) {
                txt += "Puerta " + a.getIdPuerta() + " -> Vuelo " + a.getCodigoVuelo() +
                        " (" + a.getHoraInicio() + "h - " + a.getHoraFin() + "h)\n";
            }
        }
        return txt;
    }

    // --- MODULO 3: EQUIPAJE ---
    public Equipaje buscarEquipaje(String etiq) {
        for (Equipaje e : equipajes) if (e.getEtiqueta().equalsIgnoreCase(etiq)) return e;
        return null;
    }

    public String registrarMaletaIndividual(String pas, String nom, String codVuelo, double peso, double costoExtra) {
        contEtiquetas++;
        String etiq = "BAG-" + contEtiquetas;
        equipajes.add(new Equipaje(etiq, pas, nom, codVuelo, peso, costoExtra));
        return etiq;
    }

    public String reportarMaletaExtraviada(String etiq) {
        Equipaje eq = buscarEquipaje(etiq);
        if (eq == null || buscarVueloUsuario(eq.getCodigoVuelo()) == null) {
            return "Error: No se encontro esa etiqueta en sus vuelos.";
        }
        if (eq.getEstado().equals("EXTRAVIADO")) {
            return "Aviso: Esta maleta ya fue reportada como EXTRAVIADA.";
        }
        if (eq.getEstado().equals("ENTREGADO") || eq.getEstado().equals("RETENIDO")) {
            return "Error: No se puede reportar extravio porque la maleta esta en estado " + eq.getEstado() + ".";
        }
        eq.setEstado("EXTRAVIADO");
        return "Reporte generado: La maleta " + etiq + " de " + eq.getNombrePasajero() + " ha sido marcada como EXTRAVIADA.";
    }

    public String consultarEquipajes() {
        String txt = "=== EQUIPAJES DE MIS VUELOS ===\n";
        int c = 0;
        for (Equipaje e : equipajes) {
            if (buscarVueloUsuario(e.getCodigoVuelo()) != null) {
                c++;
                txt += e.getEtiqueta() + " | Vuelo: " + e.getCodigoVuelo() + " | Pasajero: " + e.getNombrePasajero() +
                        " | Peso: " + e.getPesoKg() + "kg | Extra: $" + e.getCostoExtra() + " | Estado: " + e.getEstado() + "\n";
            }
        }
        return c == 0 ? "No hay equipajes registrados." : txt;
    }
}
