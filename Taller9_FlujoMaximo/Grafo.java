
import java.util.ArrayList;

public class Grafo {

    private ArrayList<Vertice> vertices;
    private ArrayList<Relacion> relaciones;
    private boolean dirigido;

    public Grafo(boolean dirigido) {
        this.vertices = new ArrayList<>();
        this.relaciones = new ArrayList<>();
        this.dirigido = dirigido;
    }

    public boolean esDirigido() {
        return dirigido;
    }

    public ArrayList<Vertice> getVertices() {
        return new ArrayList<>(vertices);
    }

    public ArrayList<Relacion> getRelaciones() {
        return new ArrayList<>(relaciones);
    }

    // ==========================================
    // BUSCAR VERTICE
    // ==========================================

    public Vertice buscarVertice(String id) {

        if (id == null) {
            return null;
        }

        for (Vertice v : vertices) {

            if (v.getId().equals(id)) {
                return v;
            }
        }

        return null;
    }

    // ==========================================
    // BUSCAR RELACION
    // ==========================================

    public Relacion buscarRelacion(String id) {

        if (id == null) {
            return null;
        }

        for (Relacion r : relaciones) {

            if (r.getId().equals(id)) {
                return r;
            }
        }

        return null;
    }

    // ==========================================
    // AGREGAR VERTICE
    // ==========================================

    public boolean agregarVertice(String id, String dato) {

        if (id == null || id.trim().isEmpty()
                || dato == null
                || buscarVertice(id) != null) {
            return false;
        }

        vertices.add(new Vertice(id, dato));

        return true;
    }

    // ==========================================
    // ACTUALIZAR VERTICE
    // ==========================================

    public boolean actualizarVertice(
            String id,
            String nuevoDato) {

        Vertice v = buscarVertice(id);

        if (v == null || nuevoDato == null) {
            return false;
        }

        v.setDato(nuevoDato);

        return true;
    }

    // ==========================================
    // ELIMINAR VERTICE
    // ==========================================

    public boolean eliminarVertice(String id) {

        Vertice v = buscarVertice(id);

        if (v == null) {
            return false;
        }

        relaciones.removeIf(r ->
                r.getVertice1().equals(id)
                || r.getVertice2().equals(id)
        );

        vertices.remove(v);

        return true;
    }

    // ==========================================
    // VALIDAR PESO
    // ==========================================

    private boolean pesoValido(double peso) {

        if (!Double.isFinite(peso) || peso < 0) {
            return false;
        }

        return true;
    }

    // ==========================================
    // AGREGAR RELACION
    // ==========================================

    public boolean agregarRelacion(
            String id,
            String vertice1,
            String vertice2,
            double peso) {

        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        if (buscarRelacion(id) != null) {
            return false;
        }

        if (buscarVertice(vertice1) == null
                || buscarVertice(vertice2) == null) {
            return false;
        }

        if (!pesoValido(peso)) {
            return false;
        }

        for (Relacion r : relaciones) {

            boolean mismaDireccion =
                    r.getVertice1().equals(vertice1)
                    && r.getVertice2().equals(vertice2);

            boolean direccionInversa =
                    r.getVertice1().equals(vertice2)
                    && r.getVertice2().equals(vertice1);

            if (mismaDireccion
                    || (!dirigido && direccionInversa)) {
                return false;
            }
        }

        relaciones.add(
                new Relacion(
                        id,
                        vertice1,
                        vertice2,
                        peso
                )
        );

        return true;
    }

    // ==========================================
    // ACTUALIZAR RELACION
    // ==========================================

    public boolean actualizarRelacion(
            String id,
            double nuevoPeso) {

        Relacion r = buscarRelacion(id);

        if (r == null || !pesoValido(nuevoPeso)) {
            return false;
        }

        r.setPeso(nuevoPeso);

        return true;
    }

    // ==========================================
    // ELIMINAR RELACION
    // ==========================================

    public boolean eliminarRelacion(String id) {

        Relacion r = buscarRelacion(id);

        if (r == null) {
            return false;
        }

        relaciones.remove(r);

        return true;
    }

    // ==========================================
    // OBTENER INDICE DE VERTICE
    // ==========================================

    public int obtenerIndiceVertice(String id) {

        for (int i = 0; i < vertices.size(); i++) {

            if (vertices.get(i).getId().equals(id)) {
                return i;
            }
        }

        return -1;
    }

    // ==========================================
    // MOSTRAR VERTICES
    // ==========================================

    public void mostrarVertices() {

        System.out.println("\n--- VERTICES ---");

        if (vertices.isEmpty()) {
            System.out.println("No hay vertices.");
            return;
        }

        for (Vertice v : vertices) {
            System.out.println(v);
        }
    }

    // ==========================================
    // MOSTRAR RELACIONES
    // ==========================================

    public void mostrarRelaciones() {

        System.out.println("\n--- RELACIONES ---");

        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones.");
            return;
        }

        for (Relacion r : relaciones) {

            String separador = dirigido ? " -> " : " -- ";

            System.out.println(
                    r.getId() + ": "
                    + r.getVertice1()
                    + separador
                    + r.getVertice2()
                    + " | Capacidad/Peso: "
                    + r.getPeso()
            );
        }
    }

    // ==========================================
    // MATRIZ DE ADYACENCIA
    // ==========================================

    public double[][] obtenerMatrizAdyacencia() {

        int n = vertices.size();

        double[][] matriz = new double[n][n];

        for (Relacion r : relaciones) {

            int origen =
                    obtenerIndiceVertice(r.getVertice1());

            int destino =
                    obtenerIndiceVertice(r.getVertice2());

            matriz[origen][destino] += r.getPeso();

            if (!dirigido) {
                matriz[destino][origen] += r.getPeso();
            }
        }

        return matriz;
    }

    public void mostrarMatrizAdyacencia() {

        double[][] matriz = obtenerMatrizAdyacencia();

        System.out.println("\n--- MATRIZ DE ADYACENCIA ---");

        System.out.printf("%12s", "");

        for (Vertice v : vertices) {
            System.out.printf("%12s", v.getId());
        }

        System.out.println();

        for (int i = 0; i < vertices.size(); i++) {

            System.out.printf(
                    "%12s",
                    vertices.get(i).getId()
            );

            for (int j = 0; j < vertices.size(); j++) {

                if (matriz[i][j] == 0) {
                    System.out.printf("%12s", "-");
                } else {
                    System.out.printf(
                            "%12.2f",
                            matriz[i][j]
                    );
                }
            }

            System.out.println();
        }
    }

    // ==========================================
    // LISTA DE ADYACENCIA
    // ==========================================

    public void mostrarListaAdyacencia() {

        System.out.println("\n--- LISTA DE ADYACENCIA ---");

        for (Vertice v : vertices) {

            System.out.print(v.getId() + ": ");

            boolean primera = true;

            for (Relacion r : relaciones) {

                if (r.getVertice1().equals(v.getId())) {

                    if (!primera) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            r.getVertice2()
                            + " (capacidad: "
                            + r.getPeso() + ")"
                    );

                    primera = false;
                }

                if (!dirigido
                        && r.getVertice2().equals(v.getId())) {

                    if (!primera) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            r.getVertice1()
                            + " (capacidad: "
                            + r.getPeso() + ")"
                    );

                    primera = false;
                }
            }

            if (primera) {
                System.out.print("Sin relaciones");
            }

            System.out.println();
        }
    }

    // ==========================================
    // MATRIZ DE INCIDENCIA
    // Filas: relaciones
    // Columnas: vertices
    // ==========================================

    public double[][] obtenerMatrizIncidencia() {

        int m = relaciones.size();
        int n = vertices.size();

        double[][] matriz = new double[m][n];

        for (int i = 0; i < m; i++) {

            Relacion r = relaciones.get(i);

            int origen =
                    obtenerIndiceVertice(r.getVertice1());

            int destino =
                    obtenerIndiceVertice(r.getVertice2());

            if (dirigido) {

                matriz[i][origen] = -1;
                matriz[i][destino] = 1;

            } else {

                matriz[i][origen] = 1;
                matriz[i][destino] = 1;
            }
        }

        return matriz;
    }

    public void mostrarMatrizIncidencia() {

        double[][] matriz = obtenerMatrizIncidencia();

        System.out.println("\n--- MATRIZ DE INCIDENCIA ---");

        System.out.printf("%12s", "");

        for (Vertice v : vertices) {
            System.out.printf("%12s", v.getId());
        }

        System.out.println();

        for (int i = 0; i < relaciones.size(); i++) {

            System.out.printf(
                    "%12s",
                    relaciones.get(i).getId()
            );

            for (int j = 0; j < vertices.size(); j++) {

                System.out.printf(
                        "%12.0f",
                        matriz[i][j]
                );
            }

            System.out.println();
        }
    }

    // ==========================================
    // LISTA DE INCIDENCIA
    // ==========================================

    public void mostrarListaIncidencia() {

        System.out.println("\n--- LISTA DE INCIDENCIA ---");

        for (Relacion r : relaciones) {

            System.out.println(
                    r.getId()
                    + ": "
                    + r.getVertice1()
                    + (dirigido ? " -> " : " -- ")
                    + r.getVertice2()
                    + " | Capacidad: "
                    + r.getPeso()
            );
        }
    }
}
