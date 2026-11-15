import java.util.ArrayList;
import java.util.List;

public class Grafo {

    private final List<Vertice> vertices;
    private final List<Relacion> relaciones;
    private final boolean dirigido;

    public Grafo(boolean dirigido) {
        this.dirigido = dirigido;
        this.vertices = new ArrayList<>();
        this.relaciones = new ArrayList<>();
    }

    public boolean esDirigido() {
        return dirigido;
    }

    public List<Vertice> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Relacion> getRelaciones() {
        return new ArrayList<>(relaciones);
    }

    public Vertice buscarVertice(String id) {
        for (Vertice vertice : vertices) {
            if (vertice.getId().equals(id)) {
                return vertice;
            }
        }

        return null;
    }

    public Relacion buscarRelacion(String id) {
        for (Relacion relacion : relaciones) {
            if (relacion.getId().equals(id)) {
                return relacion;
            }
        }

        return null;
    }

    public boolean agregarVertice(String id, String dato) {
        if (id == null || id.trim().isEmpty()
                || buscarVertice(id) != null) {
            return false;
        }

        vertices.add(new Vertice(id, dato));
        return true;
    }

    public boolean actualizarVertice(
            String id, String nuevoDato) {

        Vertice vertice = buscarVertice(id);

        if (vertice == null) {
            return false;
        }

        vertice.setDato(nuevoDato);
        return true;
    }

    public boolean eliminarVertice(String id) {
        Vertice vertice = buscarVertice(id);

        if (vertice == null) {
            return false;
        }

        relaciones.removeIf(relacion ->
                relacion.getVertice1().equals(id)
                || relacion.getVertice2().equals(id));

        vertices.remove(vertice);
        return true;
    }

    public boolean agregarRelacion(
            String id,
            String origen,
            String destino,
            double capacidad) {

        if (id == null || id.trim().isEmpty()
                || buscarRelacion(id) != null) {
            return false;
        }

        if (buscarVertice(origen) == null
                || buscarVertice(destino) == null) {
            return false;
        }

        if (origen.equals(destino)
                || !Double.isFinite(capacidad)
                || capacidad < 0) {
            return false;
        }

        for (Relacion relacion : relaciones) {
            boolean mismaDireccion =
                    relacion.getVertice1().equals(origen)
                    && relacion.getVertice2().equals(destino);

            boolean direccionContraria =
                    relacion.getVertice1().equals(destino)
                    && relacion.getVertice2().equals(origen);

            if (mismaDireccion
                    || (!dirigido && direccionContraria)) {
                return false;
            }
        }

        relaciones.add(
                new Relacion(id, origen, destino, capacidad));

        return true;
    }

    public boolean actualizarRelacion(
            String id, double nuevaCapacidad) {

        Relacion relacion = buscarRelacion(id);

        if (relacion == null
                || !Double.isFinite(nuevaCapacidad)
                || nuevaCapacidad < 0) {
            return false;
        }

        relacion.setPeso(nuevaCapacidad);
        return true;
    }

    public boolean eliminarRelacion(String id) {
        Relacion relacion = buscarRelacion(id);

        if (relacion == null) {
            return false;
        }

        relaciones.remove(relacion);
        return true;
    }

    public void mostrarMatrizAdyacencia() {
        int n = vertices.size();
        double[][] matriz = new double[n][n];

        for (Relacion relacion : relaciones) {
            int i = indiceVertice(relacion.getVertice1());
            int j = indiceVertice(relacion.getVertice2());

            matriz[i][j] = relacion.getPeso();

            if (!dirigido) {
                matriz[j][i] = relacion.getPeso();
            }
        }

        System.out.println("\nMATRIZ DE ADYACENCIA");
        imprimirEncabezado();

        for (int i = 0; i < n; i++) {
            System.out.printf("%-8s", vertices.get(i).getId());

            for (int j = 0; j < n; j++) {
                System.out.printf("%-8.1f", matriz[i][j]);
            }

            System.out.println();
        }
    }

    public void mostrarListaAdyacencia() {
        System.out.println("\nLISTA DE ADYACENCIA");

        for (Vertice vertice : vertices) {
            System.out.print(vertice.getId() + " -> ");

            boolean primero = true;

            for (Relacion relacion : relaciones) {
                if (relacion.getVertice1()
                        .equals(vertice.getId())) {

                    if (!primero) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            relacion.getVertice2()
                            + "(" + relacion.getPeso() + ")");

                    primero = false;
                }

                if (!dirigido
                        && relacion.getVertice2()
                        .equals(vertice.getId())) {

                    if (!primero) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            relacion.getVertice1()
                            + "(" + relacion.getPeso() + ")");

                    primero = false;
                }
            }

            System.out.println();
        }
    }

    public void mostrarMatrizIncidencia() {
        int n = vertices.size();
        int m = relaciones.size();
        double[][] matriz = new double[n][m];

        for (int j = 0; j < m; j++) {
            Relacion relacion = relaciones.get(j);

            int origen = indiceVertice(relacion.getVertice1());
            int destino = indiceVertice(relacion.getVertice2());

            matriz[origen][j] = dirigido ? -relacion.getPeso()
                    : relacion.getPeso();

            matriz[destino][j] = relacion.getPeso();
        }

        System.out.println("\nMATRIZ DE INCIDENCIA");

        System.out.printf("%-8s", "");

        for (Relacion relacion : relaciones) {
            System.out.printf("%-10s", relacion.getId());
        }

        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%-8s", vertices.get(i).getId());

            for (int j = 0; j < m; j++) {
                System.out.printf("%-10.1f", matriz[i][j]);
            }

            System.out.println();
        }
    }

    public void mostrarListaIncidencia() {
        System.out.println("\nLISTA DE INCIDENCIA");

        for (Vertice vertice : vertices) {
            System.out.print(vertice.getId() + " -> ");

            boolean primero = true;

            for (Relacion relacion : relaciones) {
                if (relacion.getVertice1().equals(vertice.getId())
                        || relacion.getVertice2()
                        .equals(vertice.getId())) {

                    if (!primero) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            relacion.getId() + ":"
                            + relacion.getVertice1()
                            + "->" + relacion.getVertice2()
                            + "(" + relacion.getPeso() + ")");

                    primero = false;
                }
            }

            System.out.println();
        }
    }

    private int indiceVertice(String id) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return i;
            }
        }

        return -1;
    }

    private void imprimirEncabezado() {
        System.out.printf("%-8s", "");

        for (Vertice vertice : vertices) {
            System.out.printf("%-8s", vertice.getId());
        }

        System.out.println();
    }
}