import java.util.ArrayList;
import java.util.List;
public class Grafo {
    private List<Vertice> vertices;
    private List<Relacion> relaciones;
    public Grafo() {
        vertices = new ArrayList<Vertice>();
        relaciones = new ArrayList<Relacion>();
    }
    private Vertice buscarVertice(String id) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);
            if (vertice.getId().equals(id)) {
                return vertice;
            }
        }
        return null;
    }
    private Relacion buscarRelacion(String id) {
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            if (relacion.getId().equals(id)) {
                return relacion;
            }
        }
        return null;
    }
    public boolean agregarVertice(String id, String dato) {
        if (buscarVertice(id) != null) {
            return false;
        }
        Vertice nuevoVertice = new Vertice(id, dato);
        vertices.add(nuevoVertice);
        return true;
    }
    public boolean eliminarVertice(String id) {
        Vertice vertice = buscarVertice(id);
        if (vertice == null) {
            return false;
        }
        for (int i = relaciones.size() - 1; i >= 0; i--) {
            Relacion relacion = relaciones.get(i);
            if (relacion.contieneVertice(id)) {
                relaciones.remove(i);
            }
        }
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                vertices.remove(i);
                break;
            }
        }
        return true;
    }
    public boolean actualizarVertice(String id, String nuevoDato) {
        Vertice vertice = buscarVertice(id);
        if (vertice == null) {
            return false;
        }
        vertice.setDato(nuevoDato);
        return true;
    }
    public boolean agregarRelacion(String id, String vertice1, String vertice2, double peso) {
        if (buscarRelacion(id) != null) {
            return false;
        }
        if (buscarVertice(vertice1) == null) {
            return false;
        }
        if (buscarVertice(vertice2) == null) {
            return false;
        }
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            boolean mismaRelacion = false;
            if (relacion.getVertice1().equals(vertice1) && relacion.getVertice2().equals(vertice2)) {
                mismaRelacion = true;
            }
            if (relacion.getVertice1().equals(vertice2) && relacion.getVertice2().equals(vertice1)) {
                mismaRelacion = true;
            }
            if (mismaRelacion) {
                return false;
            }
        }
        Relacion nuevaRelacion = new Relacion(id, vertice1, vertice2, peso);
        relaciones.add(nuevaRelacion);
        return true;
    }
    public boolean eliminarRelacion(String id) {
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            if (relacion.getId().equals(id)) {
                relaciones.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean actualizarRelacion(String id, double nuevoPeso) {
        Relacion relacion = buscarRelacion(id);
        if (relacion == null) {
            return false;
        }
        relacion.setPeso(nuevoPeso);
        return true;
    }
    public void mostrarVertices() {
        System.out.println();
        System.out.println("========== VERTICES ==========");
        if (vertices.size() == 0) {
            System.out.println("No hay vertices.");
            return;
        }
        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);
            System.out.println(vertice.getId() + " -> " + vertice.getDato());
        }
    }
    public void mostrarRelaciones() {
        System.out.println();
        System.out.println("========== RELACIONES ==========");
        if (relaciones.size() == 0) {
            System.out.println("No hay relaciones.");
            return;
        }
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            System.out.println(relacion);
        }
    }
    public void mostrarMatrizAdyacencia() {
        System.out.println();
        System.out.println("======================================");
        System.out.println("       MATRIZ DE ADYACENCIA");
        System.out.println("======================================");
        if (vertices.size() == 0) {
            System.out.println("No hay vertices.");
            return;
        }
        int cantidadVertices = vertices.size();
        double[][] matriz = new double[cantidadVertices][cantidadVertices];
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            int posicion1 = obtenerIndiceVertice(relacion.getVertice1());
            int posicion2 = obtenerIndiceVertice(relacion.getVertice2());
            matriz[posicion1][posicion2] = relacion.getPeso();
            matriz[posicion2][posicion1] = relacion.getPeso();
        }
        System.out.printf("%12s", "");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.printf("%12s", vertices.get(i).getId());
        }
        System.out.println();
        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf("%12s", vertices.get(i).getId());
            for (int j = 0; j < cantidadVertices; j++) {
                System.out.printf("%12s", formatearNumero(matriz[i][j]));
            }
            System.out.println();
        }
    }
    public void mostrarListaAdyacencia() {
        System.out.println();
        System.out.println("======================================");
        System.out.println("        LISTA DE ADYACENCIA");
        System.out.println("======================================");
        if (vertices.size() == 0) {
            System.out.println("No hay vertices.");
            return;
        }
        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);
            System.out.print(vertice.getId() + " -> ");
            boolean tieneRelaciones = false;
            for (int j = 0; j < relaciones.size(); j++) {
                Relacion relacion = relaciones.get(j);
                if (relacion.contieneVertice(vertice.getId())) {
                    String vecino = relacion.obtenerOtroVertice(vertice.getId());
                    if (tieneRelaciones) {
                        System.out.print(" , ");
                    }
                    System.out.print("(" + vecino + ", peso=" + formatearNumero(relacion.getPeso()) + ")");
                    tieneRelaciones = true;
                }
            }
            if (!tieneRelaciones) {
                System.out.print("∅");
            }
            System.out.println();
        }
    }
    public void mostrarMatrizIncidencia() {
        System.out.println();
        System.out.println("======================================");
        System.out.println("        MATRIZ DE INCIDENCIA");
        System.out.println("======================================");
        if (relaciones.size() == 0) {
            System.out.println("No hay relaciones.");
            return;
        }
        int cantidadRelaciones = relaciones.size();
        int cantidadVertices = vertices.size();
        double[][] matriz = new double[cantidadRelaciones][cantidadVertices];
        for (int i = 0; i < cantidadRelaciones; i++) {
            Relacion relacion = relaciones.get(i);
            int posicion1 = obtenerIndiceVertice(relacion.getVertice1());
            int posicion2 = obtenerIndiceVertice(relacion.getVertice2());
            matriz[i][posicion1] = relacion.getPeso();
            matriz[i][posicion2] = relacion.getPeso();
        }
        System.out.printf("%12s", "Relacion");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.printf("%12s", vertices.get(i).getId());
        }
        System.out.println();
        for (int i = 0; i < cantidadRelaciones; i++) {
            System.out.printf("%12s", relaciones.get(i).getId());
            for (int j = 0; j < cantidadVertices; j++) {
                System.out.printf("%12s", formatearNumero(matriz[i][j]));
            }
            System.out.println();
        }
    }
    public void mostrarListaIncidencia() {
        System.out.println();
        System.out.println("======================================");
        System.out.println("         LISTA DE INCIDENCIA");
        System.out.println("======================================");
        if (relaciones.size() == 0) {
            System.out.println("No hay relaciones.");
            return;
        }
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);
            System.out.println(relacion.getId() + " -> " + relacion.getVertice1() + " -- " + formatearNumero(relacion.getPeso()) + " -- " + relacion.getVertice2());
        }
    }
    private int obtenerIndiceVertice(String id) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
    private String formatearNumero(double numero) {
        if (numero == (long) numero) {
            return String.valueOf((long) numero);
        }
        return String.valueOf(numero);
    }
}
