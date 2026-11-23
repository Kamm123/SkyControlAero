package modelo;



public class Usuario {

    private String documento;

    private String nombre;

    private String clave;



    public Usuario(String documento, String nombre, String clave) {

        this.documento = documento;

        this.nombre = nombre;

        this.clave = clave;

    }



    public String getDocumento() {

        return documento;

    }



    public void setDocumento(String documento) {

        this.documento = documento;

    }



    public String getNombre() {

        return nombre;

    }



    public void setNombre(String nombre) {

        this.nombre = nombre;

    }



    public String getClave() {

        return clave;

    }



    public void setClave(String clave) {

        this.clave = clave;

    }

}

