package modelo;

public class Puerta {
    private String idPuerta;
    private String terminal;
    private String estadoOperativo; // "Operativa" o "En mantenimiento"

    public Puerta(String idPuerta, String terminal, String estadoOperativo) {
        this.idPuerta = idPuerta;
        this.terminal = terminal;
        this.estadoOperativo = estadoOperativo;
    }

    public String getIdPuerta() {
        return idPuerta;
    }

    public void setIdPuerta(String idPuerta) {
        this.idPuerta = idPuerta;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

    public boolean estaOperativa() {
        return estadoOperativo.equals("Operativa");
    }
}
