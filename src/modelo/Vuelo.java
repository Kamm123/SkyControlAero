package modelo;



import java.util.ArrayList;



public class Vuelo {

    private String codigo, aerolinea, origen, destino, tipoVuelo, estado, motivo, documentoUsuario;

    private int anio, mes, dia;

    private double horaSalida;

    private ArrayList<Pasajero> pasajeros;

    private boolean equipajeRegistrado;



    public Vuelo(String codigo, String aerolinea, String origen, String destino, int anio, int mes, int dia, double horaSalida, ArrayList<Pasajero> pasajeros, String documentoUsuario) {

        this.codigo = codigo;

        this.aerolinea = aerolinea;

        this.origen = origen;

        this.destino = destino;

        this.tipoVuelo = "Internacional";

        this.anio = anio;

        this.mes = mes;

        this.dia = dia;

        this.horaSalida = horaSalida;

        this.pasajeros = pasajeros;

        this.estado = "Programado";

        this.motivo = "Registro inicial";

        this.documentoUsuario = documentoUsuario;

        this.equipajeRegistrado = false;

    }



    public String getCodigo() { return codigo; }

    public String getAerolinea() { return aerolinea; }

    public String getOrigen() { return origen; }

    public String getDestino() { return destino; }

    public String getTipoVuelo() { return tipoVuelo; }

    public String getFechaFormateada() {

        return (dia < 10 ? "0" + dia : dia) + "/" + (mes < 10 ? "0" + mes : mes) + "/" + anio;

    }

    public double getHoraSalida() { return horaSalida; }

    public void setHoraSalida(double horaSalida) { this.horaSalida = horaSalida; }

    public ArrayList<Pasajero> getPasajeros() { return pasajeros; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }

    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDocumentoUsuario() { return documentoUsuario; }

    public boolean isEquipajeRegistrado() { return equipajeRegistrado; }

    public void setEquipajeRegistrado(boolean equipajeRegistrado) { this.equipajeRegistrado = equipajeRegistrado; }



    public boolean estaActivo() {

        return estado.equals("Programado") || estado.equals("Embarcando") || estado.equals("Embarcado") || estado.equals("Retrasado");

    }



    public boolean puedeCambiarA(String nuevoEstado) {

        if (estado.equals("Despegado") || estado.equals("Cancelado")) return false;

        if (estado.equals("Programado")) return nuevoEstado.equals("Embarcando") || nuevoEstado.equals("Retrasado") || nuevoEstado.equals("Cancelado");

        if (estado.equals("Embarcando")) return nuevoEstado.equals("Embarcado") || nuevoEstado.equals("Retrasado") || nuevoEstado.equals("Cancelado");

        if (estado.equals("Embarcado")) return nuevoEstado.equals("Despegado") || nuevoEstado.equals("Retrasado") || nuevoEstado.equals("Cancelado");

        if (estado.equals("Retrasado")) return nuevoEstado.equals("Embarcando") || nuevoEstado.equals("Programado") || nuevoEstado.equals("Cancelado");

        return false;

    }

}