package interfaz;

import modelo.*;
import negocio.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        GestorAeropuerto gestor = new GestorAeropuerto();
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n=== SISTEMA AEROPORTUARIO INTERNACIONAL ===");
            System.out.println("Usuario: " + (gestor.haySesionActiva() ? gestor.getUsuarioActivo().getNombre() : "Ninguno"));
            System.out.println("1. Usuario (Registrar / Iniciar / Cerrar sesion)");
            System.out.println("2. Registrar vuelo");
            System.out.println("3. Actualizar estado de vuelo");
            System.out.println("4. Consultar mis vuelos");
            System.out.println("5. Consultar disponibilidad de puertas");
            System.out.println("6. Asignar puerta a mi vuelo");
            System.out.println("7. Ver asignaciones de puertas");
            System.out.println("8. Registrar equipaje de pasajeros");
            System.out.println("9. Ver equipajes o reportar maleta extraviada");
            System.out.println("10. Salir");
            opcion = Validador.leerEntero(sc, "Opcion: ");

            if (opcion >= 2 && opcion <= 9 && !gestor.haySesionActiva()) {
                JOptionPane.showMessageDialog(null, "Primero registre un usuario o inicie sesion (Opcion 1).");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n1. Registrar | 2. Iniciar sesion | 3. Cerrar sesion | 4. Volver");
                    int subU = Validador.leerEntero(sc, "Elija: ");
                    if (subU == 4) break;
                    if (subU == 1) {
                        String doc = "";
                        do {
                            System.out.print("Cedula (10 numeros exactos): ");
                            doc = sc.nextLine().trim();
                            if (!Validador.esSoloNumeros(doc) || doc.length() != 10 || gestor.buscarUsuario(doc) != null) {
                                System.out.println("Error: Debe tener 10 numeros exactos y no estar repetido.");
                                doc = "";
                            }
                        } while (doc.equals(""));

                        String nom = "";
                        do {
                            System.out.print("Nombre completo: ");
                            nom = sc.nextLine().trim();
                            if (!Validador.esSoloLetras(nom)) { System.out.println("Error: Solo letras."); nom = ""; }
                        } while (nom.equals(""));

                        String clave = "";
                        do {
                            System.out.print("Clave PIN (4 numeros): ");
                            clave = sc.nextLine().trim();
                            if (!Validador.esSoloNumeros(clave) || clave.length() != 4) { System.out.println("Error: 4 numeros."); clave = ""; }
                        } while (clave.equals(""));

                        JOptionPane.showMessageDialog(null, gestor.registrarUsuario(doc, nom, clave));
                    } else if (subU == 2) {
                        System.out.print("Cedula: "); String d = sc.nextLine().trim();
                        System.out.print("Clave: "); String c = sc.nextLine().trim();
                        JOptionPane.showMessageDialog(null, gestor.iniciarSesion(d, c));
                    } else if (subU == 3) {
                        gestor.cerrarSesion();
                        JOptionPane.showMessageDialog(null, "Sesion cerrada.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- REGISTRO DE VUELO INTERNACIONAL ---");
                    for (int i = 0; i < gestor.getAerolineas().size(); i++) {
                        System.out.println((i + 1) + ". " + gestor.getAerolineas().get(i));
                    }
                    int selA = -1;
                    do {
                        selA = Validador.leerEntero(sc, "Elija aerolinea (0 para volver): ");
                        if (selA == 0) break;
                    } while (selA < 1 || selA > gestor.getAerolineas().size());
                    if (selA == 0) break;
                    String aero = gestor.getAerolineas().get(selA - 1);

                    String orig = "";
                    do {
                        System.out.print("Origen (solo letras): ");
                        orig = sc.nextLine().trim();
                        if (!Validador.esSoloLetras(orig)) { System.out.println("Error: Solo letras."); orig = ""; }
                    } while (orig.equals(""));

                    String dest = "";
                    while (true) {
                        System.out.print("Destino (solo letras): ");
                        dest = sc.nextLine().trim();
                        if (!Validador.esSoloLetras(dest) || dest.equalsIgnoreCase(orig)) {
                            System.out.println("Error: Destino invalido o igual al origen.");
                            continue;
                        }
                        System.out.print("¿Seguro que el destino es " + dest + "? (S/N): ");
                        if (sc.nextLine().trim().equalsIgnoreCase("S")) break;
                    }

                    int anio = 0;
                    do {
                        anio = Validador.leerEntero(sc, "Año del vuelo (2026 a 2027): ");
                        if (anio < 2026 || anio > 2027) System.out.println("Error: Solo se permiten vuelos entre 2026 y 2027.");
                    } while (anio < 2026 || anio > 2027);

                    int mes = 0;
                    do {
                        mes = Validador.leerEntero(sc, "Mes del vuelo (1 a 12): ");
                        if (mes < 1 || mes > 12) {
                            System.out.println("Error: Mes invalido.");
                        } else if (anio == 2026 && mes < 11) {
                            System.out.println("Error: Para el año 2026 solo puede elegir mes 11 o 12.");
                            mes = 0;
                        }
                    } while (mes < 1 || mes > 12);

                    int maxDias = (mes == 2) ? 28 : ((mes == 4 || mes == 6 || mes == 9 || mes == 11) ? 30 : 31);
                    int dia = 0;
                    do {
                        dia = Validador.leerEntero(sc, "Dia del vuelo (1 a " + maxDias + "): ");
                    } while (dia < 1 || dia > maxDias);

                    double hora = -1;
                    do {
                        hora = Validador.leerDecimal(sc, "Hora salida (0.0 a 23.99, ej. 12.23): ");
                    } while (hora < 0.0 || hora > 23.99);

                    ArrayList<Pasajero> listaPasajeros = new ArrayList<>();
                    listaPasajeros.add(new Pasajero(gestor.getUsuarioActivo().getDocumento(), gestor.getUsuarioActivo().getNombre()));

                    int totalPersonas = 0;
                    do {
                        totalPersonas = Validador.leerEntero(sc, "¿Para cuantas personas en total registrara este vuelo? (incluido usted): ");
                        if (totalPersonas < 1) {
                            System.out.println("Error: Debe viajar al menos usted (1).");
                        } else if (totalPersonas > 5) {
                            System.out.println("Error: La cantidad ingresada excede el cupo permitido por reserva.");
                            totalPersonas = 0;
                        }
                    } while (totalPersonas < 1);

                    for (int i = 2; i <= totalPersonas; i++) {
                        System.out.println("--- Datos del acompañante #" + (i - 1) + " ---");
                        String cedAc = "";
                        do {
                            System.out.print("Cedula/Pasaporte (10 numeros): ");
                            cedAc = sc.nextLine().trim();
                            boolean repetida = false;
                            for (Pasajero p : listaPasajeros) if (p.getCedula().equals(cedAc)) repetida = true;
                            if (!Validador.esSoloNumeros(cedAc) || cedAc.length() != 10 || repetida) {
                                System.out.println("Error: Cedula invalida o repetida en este vuelo.");
                                cedAc = "";
                            }
                        } while (cedAc.equals(""));

                        String nomAc = "";
                        do {
                            System.out.print("Nombre completo (solo letras): ");
                            nomAc = sc.nextLine().trim();
                            if (!Validador.esSoloLetras(nomAc)) { System.out.println("Error: Solo letras."); nomAc = ""; }
                        } while (nomAc.equals(""));

                        listaPasajeros.add(new Pasajero(cedAc, nomAc));
                    }

                    JOptionPane.showMessageDialog(null, gestor.registrarVuelo(aero, orig, dest, anio, mes, dia, hora, listaPasajeros));
                    break;

                case 3:
                    if (!gestor.tieneVuelosUsuario()) { JOptionPane.showMessageDialog(null, "No tiene vuelos."); break; }
                    System.out.println("\n" + gestor.consultarVuelos());
                    System.out.print("Codigo del vuelo (ej. VUE-101): ");
                    String codAct = sc.nextLine().trim();
                    Vuelo vAct = gestor.buscarVueloUsuario(codAct);
                    if (vAct == null) { JOptionPane.showMessageDialog(null, "Vuelo no encontrado."); break; }

                    String[] todosEstados = {"Embarcando", "Embarcado", "Retrasado", "Despegado", "Cancelado"};
                    ArrayList<String> validos = new ArrayList<>();
                    for (String e : todosEstados) if (vAct.puedeCambiarA(e)) validos.add(e);

                    if (validos.isEmpty()) { JOptionPane.showMessageDialog(null, "Este vuelo ya finalizo (" + vAct.getEstado() + ")."); break; }

                    System.out.println("Estado actual: " + vAct.getEstado() + ". Opciones permitidas:");
                    for (int i = 0; i < validos.size(); i++) System.out.println((i + 1) + ". " + validos.get(i));
                    int selEst = 0;
                    do {
                        selEst = Validador.leerEntero(sc, "Elija numero de nuevo estado: ");
                    } while (selEst < 1 || selEst > validos.size());
                    String estV = validos.get(selEst - 1);

                    String mot = "Operacion normal";
                    double nHora = 0.0;
                    if (estV.equals("Retrasado") || estV.equals("Cancelado")) {
                        do {
                            System.out.print("Motivo de " + estV + ": ");
                            mot = sc.nextLine().trim();
                        } while (mot.equals(""));
                    }
                    if (estV.equals("Retrasado")) {
                        do {
                            nHora = Validador.leerDecimal(sc, "Nueva hora estimada (mayor a " + vAct.getHoraSalida() + "h): ");
                            if (nHora <= vAct.getHoraSalida() || nHora > 23.99) System.out.println("Error: Debe ser mayor a la hora actual del vuelo.");
                        } while (nHora <= vAct.getHoraSalida() || nHora > 23.99);
                    }
                    JOptionPane.showMessageDialog(null, gestor.actualizarEstadoVuelo(codAct, estV, mot, nHora));
                    break;

                case 4:
                    System.out.println("\n" + gestor.consultarVuelos());
                    break;

                case 5:
                    double h1 = Validador.leerDecimal(sc, "Hora inicio: ");
                    double h2 = Validador.leerDecimal(sc, "Hora fin: ");
                    if (h2 <= h1) JOptionPane.showMessageDialog(null, "Error: La hora fin debe ser mayor a la hora inicio.");
                    else System.out.println("\n" + gestor.consultarDisponibilidadPuertas(h1, h2));
                    break;

                case 6:
                    if (!gestor.tieneVuelosUsuario()) { JOptionPane.showMessageDialog(null, "Registre un vuelo primero."); break; }
                    System.out.println("\n" + gestor.consultarVuelos());
                    System.out.print("Codigo de su vuelo para asignar puerta: ");
                    Vuelo vP = gestor.buscarVueloUsuario(sc.nextLine().trim());
                    if (vP == null || !vP.estaActivo()) { JOptionPane.showMessageDialog(null, "Vuelo invalido o inactivo."); break; }

                    JOptionPane.showMessageDialog(null, gestor.asignarPuertaAutomatica(vP));
                    break;

                case 7:
                    System.out.println("\n" + gestor.consultarAsignacionesPuertas());
                    break;

                case 8:
                    if (!gestor.tieneVuelosUsuario()) { JOptionPane.showMessageDialog(null, "Registre un vuelo primero."); break; }
                    System.out.println("\n" + gestor.consultarVuelos());
                    System.out.print("Codigo de su vuelo: ");
                    Vuelo vEq = gestor.buscarVueloUsuario(sc.nextLine().trim());
                    if (vEq == null || !vEq.estaActivo()) { JOptionPane.showMessageDialog(null, "Vuelo invalido."); break; }

                    if (vEq.isEquipajeRegistrado()) {
                        JOptionPane.showMessageDialog(null, "El equipaje de los pasajeros de este vuelo ya fue registrado anteriormente.");
                        break;
                    }

                    System.out.println("\nNOTA DE EQUIPAJE: De 1 a 2 maletas incluidas (carga max 23.0 kg c/u).");
                    System.out.println("De 3 hasta max 4 maletas cuesta $30.0 extra c/u. Si supera 23.0 kg (hasta 32.0 kg) paga $25.0 extra.");

                    double costoGlobal = 0;
                    String resumen = "=== RESUMEN EQUIPAJE VUELO " + vEq.getCodigo() + " ===\n";

                    for (Pasajero p : vEq.getPasajeros()) {
                        System.out.println("\n>>> Pasajero: " + p.getNombre() + " (Cedula: " + p.getCedula() + ")");
                        int cant = -1;
                        while (true) {
                            cant = Validador.leerEntero(sc, "¿Cuantas maletas registra " + p.getNombre() + "? (0 a 4): ");
                            if (cant < 0 || cant > 4) continue;
                            if (cant >= 3) {
                                double pagoMaletas = (cant - 2) * 30.0;
                                System.out.print("De 3 a 4 maletas tiene cargo extra ($" + pagoMaletas + "). ¿Seguro que desea continuar? (S/N): ");
                                if (sc.nextLine().trim().equalsIgnoreCase("S")) break;
                            } else {
                                break;
                            }
                        }

                        if (cant == 0) {
                            resumen += p.getNombre() + " -> Sin equipaje registrado.\n";
                            continue;
                        }

                        for (int i = 1; i <= cant; i++) {
                            double peso = 0, extra = (i >= 3) ? 30.0 : 0.0;
                            while (true) {
                                peso = Validador.leerDecimal(sc, "Peso maleta #" + i + " (carga max 23.0 kg sin recargo): ");
                                if (peso <= 0 || peso > 32.0) {
                                    System.out.println("Error: El peso debe estar entre 0.1 y 32.0 kg (limite absoluto).");
                                    continue;
                                }
                                if (peso > 23.0) {
                                    System.out.print("Supera la carga max de 23.0 kg. ¿Seguro que desea pagar $25.0 extra por sobrepeso? (S/N): ");
                                    if (sc.nextLine().trim().equalsIgnoreCase("S")) { extra += 25.0; break; }
                                } else {
                                    break;
                                }
                            }
                            costoGlobal += extra;
                            String etiq = gestor.registrarMaletaIndividual(p.getCedula(), p.getNombre(), vEq.getCodigo(), peso, extra);
                            resumen += p.getNombre() + " -> " + etiq + " (" + peso + " kg) | Extra: $" + extra + "\n";
                        }
                    }

                    vEq.setEquipajeRegistrado(true);
                    resumen += "\nTOTAL EXTRAS A PAGAR: $" + costoGlobal;
                    JOptionPane.showMessageDialog(null, resumen);
                    break;

                case 9:
                    System.out.println("\n1. Ver equipajes registrados y su estado | 2. Reportar maleta como EXTRAVIADA");
                    int subE = Validador.leerEntero(sc, "Elija: ");
                    if (subE == 1) {
                        System.out.println("\n" + gestor.consultarEquipajes());
                    } else if (subE == 2) {
                        if (!gestor.tieneEquipajesUsuario()) {
                            JOptionPane.showMessageDialog(null, "No tiene equipajes registrados.");
                            break;
                        }
                        System.out.println("\n" + gestor.consultarEquipajes());
                        System.out.print("Ingrese la etiqueta de la maleta extraviada (ej. BAG-1001): ");
                        String et = sc.nextLine().trim();
                        JOptionPane.showMessageDialog(null, gestor.reportarMaletaExtraviada(et));
                    }
                    break;

                case 10:
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema.");
                    break;
            }
        } while (opcion != 10);
        sc.close();
    }
}