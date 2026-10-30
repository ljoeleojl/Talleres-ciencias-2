
public class Relacion {

    private String id;
    private String vertice1;
    private String vertice2;
    private double peso;

    public Relacion(
            String id,
            String vertice1,
            String vertice2,
            double peso) {

        this.id = id;
        this.vertice1 = vertice1;
        this.vertice2 = vertice2;
        this.peso = peso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVertice1() {
        return vertice1;
    }

    public void setVertice1(String vertice1) {
        this.vertice1 = vertice1;
    }

    public String getVertice2() {
        return vertice2;
    }

    public void setVertice2(String vertice2) {
        this.vertice2 = vertice2;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public boolean saleDe(String idVertice) {
        return vertice1.equals(idVertice);
    }

    public boolean llegaA(String idVertice) {
        return vertice2.equals(idVertice);
    }

    public String obtenerOtroVertice(String idVertice) {

        if (vertice1.equals(idVertice)) {
            return vertice2;
        }

        if (vertice2.equals(idVertice)) {
            return vertice1;
        }

        return null;
    }

    @Override
    public String toString() {
        return "Relacion{" +
                "id='" + id + '\'' +
                ", vertice1='" + vertice1 + '\'' +
                ", vertice2='" + vertice2 + '\'' +
                ", peso=" + peso +
                '}';
    }
}
