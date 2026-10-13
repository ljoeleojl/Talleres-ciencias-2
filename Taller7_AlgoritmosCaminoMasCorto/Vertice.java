
public class Vertice {

    private String id;
    private String dato;

    public Vertice(String id, String dato) {
        this.id = id;
        this.dato = dato;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "Vertice{" +
                "id='" + id + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}
