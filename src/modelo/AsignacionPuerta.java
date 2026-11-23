package modelo;



public class AsignacionPuerta {

    private String codigoVuelo;

    private String idPuerta;

    private double horaInicio;

    private double horaFin;



    public AsignacionPuerta(String codigoVuelo, String idPuerta, double horaInicio, double horaFin) {

        this.codigoVuelo = codigoVuelo;

        this.idPuerta = idPuerta;

        this.horaInicio = horaInicio;

        this.horaFin = horaFin;

    }



    public String getCodigoVuelo() {

        return codigoVuelo;

    }



    public void setCodigoVuelo(String codigoVuelo) {

        this.codigoVuelo = codigoVuelo;

    }



    public String getIdPuerta() {

        return idPuerta;

    }



    public void setIdPuerta(String idPuerta) {

        this.idPuerta = idPuerta;

    }



    public double getHoraInicio() {

        return horaInicio;

    }



    public void setHoraInicio(double horaInicio) {

        this.horaInicio = horaInicio;

    }



    public double getHoraFin() {

        return horaFin;

    }



    public void setHoraFin(double horaFin) {

        this.horaFin = horaFin;

    }



    public boolean seCruzaCon(double inicioBuscado, double finBuscado) {

        if (inicioBuscado < horaFin && finBuscado > horaInicio) {

            return true;

        }

        return false;

    }

}