public class Relacion {

    private String id;
    private String vertice1;
    private String vertice2;
    private double peso;

    public Relacion(String id,String vertice1,String vertice2,double peso) {
        this.id = id;
        this.vertice1 = vertice1;
        this.vertice2 = vertice2;
        this.peso = peso;
    }
    public String getId() {
        return id;
    }
    public String getVertice1() {
        return vertice1;
    }
    public String getVertice2() {
        return vertice2;
    }
    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }
    public boolean contieneVertice(String id) {
        return vertice1.equals(id) || vertice2.equals(id);
    }
    public String obtenerOtroVertice(String id) {

        if (vertice1.equals(id)) {
            return vertice2;
        }
        if (vertice2.equals(id)) {
            return vertice1;
        }
        return null;
    }
    @Override
    public String toString() {
        return id +": " +vertice1 +" -- " + peso +" -- " +vertice2;
    }
}
