package modelo;



public class Equipaje {

    private String etiqueta;

    private String pasaporte;

    private String nombrePasajero;

    private String codigoVuelo;

    private double pesoKg;

    private double costoExtra;

    private String estado;

    private String puntoControl;

    private String operadorResponsable;



    public Equipaje(String etiqueta, String pasaporte, String nombrePasajero, String codigoVuelo, double pesoKg, double costoExtra) {

        this.etiqueta = etiqueta;

        this.pasaporte = pasaporte;

        this.nombrePasajero = nombrePasajero;

        this.codigoVuelo = codigoVuelo;

        this.pesoKg = pesoKg;

        this.costoExtra = costoExtra;

        this.estado = "RECIBIDO";

        this.puntoControl = "Mostrador Check-in";

        this.operadorResponsable = "Sin operador";

    }



    public String getEtiqueta() { return etiqueta; }

    public String getPasaporte() { return pasaporte; }

    public String getNombrePasajero() { return nombrePasajero; }

    public String getCodigoVuelo() { return codigoVuelo; }

    public double getPesoKg() { return pesoKg; }

    public double getCostoExtra() { return costoExtra; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getPuntoControl() { return puntoControl; }

    public void setPuntoControl(String puntoControl) { this.puntoControl = puntoControl; }

    public String getOperadorResponsable() { return operadorResponsable; }

    public void setOperadorResponsable(String operadorResponsable) { this.operadorResponsable = operadorResponsable; }



    public boolean puedeCambiarA(String nuevoEstado) {

        if (estado.equals("ENTREGADO") || estado.equals("EXTRAVIADO") || estado.equals("RETENIDO")) return false;

        if (nuevoEstado.equals("EXTRAVIADO")) return true;

        if (estado.equals("RECIBIDO")) return nuevoEstado.equals("CARGADO");

        if (estado.equals("CARGADO")) return nuevoEstado.equals("EN TRANSITO");

        if (estado.equals("EN TRANSITO")) return nuevoEstado.equals("ENTREGADO");

        return false;

    }

}

