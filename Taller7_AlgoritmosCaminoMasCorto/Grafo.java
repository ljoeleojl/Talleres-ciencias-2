import java.util.ArrayList;

public class Grafo {

    private ArrayList<Vertice> vertices;
    private ArrayList<Relacion> relaciones;
    private boolean dirigido;

    public Grafo(boolean dirigido) {
        this.vertices = new ArrayList<Vertice>();
        this.relaciones = new ArrayList<Relacion>();
        this.dirigido = dirigido;
    }

    public boolean esDirigido() {
        return dirigido;
    }

    public ArrayList<Vertice> getVertices() {
        return new ArrayList<Vertice>(vertices);
    }

    public ArrayList<Relacion> getRelaciones() {
        return new ArrayList<Relacion>(relaciones);
    }

    // =========================
    // VALIDAR PESO
    // =========================

    /*
     * El peso debe ser un numero finito.
     * Los pesos negativos solo se aceptan en grafos dirigidos:
     * en un grafo no dirigido, una relacion negativa siempre
     * forma un ciclo negativo (A -> B -> A -> ...).
     */
    private boolean pesoValido(double peso) {
        if (!Double.isFinite(peso)) {
            return false;
        }

        if (!dirigido && peso < 0) {
            return false;
        }

        return true;
    }

    // =========================
    // BUSCAR VERTICE
    // =========================

    public Vertice buscarVertice(String id) {
        if (id == null) {
            return null;
        }

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return vertices.get(i);
            }
        }

        return null;
    }

    // =========================
    // BUSCAR RELACION
    // =========================

    public Relacion buscarRelacion(String id) {
        if (id == null) {
            return null;
        }

        for (int i = 0; i < relaciones.size(); i++) {
            if (relaciones.get(i).getId().equals(id)) {
                return relaciones.get(i);
            }
        }

        return null;
    }

    // =========================
    // AGREGAR VERTICE
    // =========================

    public boolean agregarVertice(String id, String dato) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        if (dato == null) {
            return false;
        }

        if (buscarVertice(id) != null) {
            return false;
        }

        Vertice nuevoVertice = new Vertice(id, dato);
        vertices.add(nuevoVertice);

        return true;
    }

    // =========================
    // ACTUALIZAR VERTICE
    // =========================

    public boolean actualizarVertice(String id, String nuevoDato) {
        Vertice vertice = buscarVertice(id);

        if (vertice == null || nuevoDato == null) {
            return false;
        }

        vertice.setDato(nuevoDato);

        return true;
    }

    // =========================
    // ELIMINAR VERTICE
    // =========================

    public boolean eliminarVertice(String id) {
        Vertice vertice = buscarVertice(id);

        if (vertice == null) {
            return false;
        }

        for (int i = relaciones.size() - 1; i >= 0; i--) {
            Relacion relacion = relaciones.get(i);

            if (relacion.getVertice1().equals(id)
                    || relacion.getVertice2().equals(id)) {
                relaciones.remove(i);
            }
        }

        vertices.remove(vertice);

        return true;
    }

    // =========================
    // AGREGAR RELACION
    // =========================

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

        /*
         * Solo se permite una relacion entre un par de vertices.
         * Se comprueban ambas direcciones, incluso en grafos dirigidos.
         */
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            boolean mismaDireccion =
                    relacion.getVertice1().equals(vertice1)
                    && relacion.getVertice2().equals(vertice2);

            boolean direccionInversa =
                    relacion.getVertice1().equals(vertice2)
                    && relacion.getVertice2().equals(vertice1);

            if (mismaDireccion || direccionInversa) {
                return false;
            }
        }

        Relacion nuevaRelacion =
                new Relacion(id, vertice1, vertice2, peso);

        relaciones.add(nuevaRelacion);

        return true;
    }

    // =========================
    // ACTUALIZAR RELACION
    // =========================

    public boolean actualizarRelacion(String id, double nuevoPeso) {
        Relacion relacion = buscarRelacion(id);

        if (relacion == null) {
            return false;
        }

        if (!pesoValido(nuevoPeso)) {
            return false;
        }

        relacion.setPeso(nuevoPeso);

        return true;
    }

    // =========================
    // ELIMINAR RELACION
    // =========================

    public boolean eliminarRelacion(String id) {
        Relacion relacion = buscarRelacion(id);

        if (relacion == null) {
            return false;
        }

        relaciones.remove(relacion);

        return true;
    }

    // =========================
    // MOSTRAR VERTICES
    // =========================

    public void mostrarVertices() {
        if (vertices.isEmpty()) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        System.out.println("\n--- VERTICES ---");

        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);

            System.out.println(
                    "ID: " + vertice.getId()
                    + " | Dato: " + vertice.getDato()
            );
        }
    }

    // =========================
    // MOSTRAR RELACIONES
    // =========================

    public void mostrarRelaciones() {
        if (relaciones.isEmpty()) {
            System.out.println("El grafo no tiene relaciones.");
            return;
        }

        System.out.println("\n--- RELACIONES ---");

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            String separador;

            if (dirigido) {
                separador = " -> ";
            } else {
                separador = " -- ";
            }

            System.out.println(
                    "ID: " + relacion.getId()
                    + " | " + relacion.getVertice1()
                    + separador
                    + relacion.getVertice2()
                    + " | Peso: " + relacion.getPeso()
            );
        }
    }

    // =========================
    // MATRIZ DE ADYACENCIA
    // =========================

    public void mostrarMatrizAdyacencia() {
        int cantidadVertices = vertices.size();

        Double[][] matriz =
                new Double[cantidadVertices][cantidadVertices];

        for (int i = 0; i < cantidadVertices; i++) {
            for (int j = 0; j < cantidadVertices; j++) {
                matriz[i][j] = null;
            }
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            int origen =
                    obtenerIndiceVertice(relacion.getVertice1());

            int destino =
                    obtenerIndiceVertice(relacion.getVertice2());

            if (origen != -1 && destino != -1) {
                matriz[origen][destino] = relacion.getPeso();

                if (!dirigido) {
                    matriz[destino][origen] = relacion.getPeso();
                }
            }
        }

        System.out.println("\n--- MATRIZ DE ADYACENCIA ---");

        System.out.printf("%12s", "");

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf(
                    "%12s", vertices.get(i).getId()
            );
        }

        System.out.println();

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf(
                    "%12s", vertices.get(i).getId()
            );

            for (int j = 0; j < cantidadVertices; j++) {
                if (matriz[i][j] == null) {
                    System.out.printf("%12s", "-");
                } else {
                    System.out.printf("%12s", matriz[i][j]);
                }
            }

            System.out.println();
        }
    }

    // =========================
    // LISTA DE ADYACENCIA
    // =========================

    public void mostrarListaAdyacencia() {
        System.out.println("\n--- LISTA DE ADYACENCIA ---");

        if (vertices.isEmpty()) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);

            System.out.print(vertice.getId() + ": ");

            boolean primeraRelacion = true;

            for (int j = 0; j < relaciones.size(); j++) {
                Relacion relacion = relaciones.get(j);

                if (relacion.getVertice1().equals(vertice.getId())) {
                    if (!primeraRelacion) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            relacion.getVertice2()
                            + " (peso: " + relacion.getPeso() + ")"
                    );

                    primeraRelacion = false;
                }

                if (!dirigido
                        && relacion.getVertice2().equals(vertice.getId())) {

                    if (!primeraRelacion) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            relacion.getVertice1()
                            + " (peso: " + relacion.getPeso() + ")"
                    );

                    primeraRelacion = false;
                }
            }

            if (primeraRelacion) {
                System.out.print("Sin relaciones");
            }

            System.out.println();
        }
    }

    // =========================
    // MATRIZ DE INCIDENCIA
    // Filas: relaciones
    // Columnas: vertices
    // =========================

    public void mostrarMatrizIncidencia() {
        int cantidadRelaciones = relaciones.size();
        int cantidadVertices = vertices.size();

        double[][] matriz =
                new double[cantidadRelaciones][cantidadVertices];

        for (int i = 0; i < cantidadRelaciones; i++) {
            Relacion relacion = relaciones.get(i);

            int origen =
                    obtenerIndiceVertice(relacion.getVertice1());

            int destino =
                    obtenerIndiceVertice(relacion.getVertice2());

            if (origen != -1 && destino != -1) {
                if (dirigido) {
                    matriz[i][origen] = -relacion.getPeso();
                    matriz[i][destino] = relacion.getPeso();
                } else {
                    matriz[i][origen] = relacion.getPeso();
                    matriz[i][destino] = relacion.getPeso();
                }
            }
        }

        System.out.println("\n--- MATRIZ DE INCIDENCIA ---");

        System.out.printf("%12s", "");

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf(
                    "%12s", vertices.get(i).getId()
            );
        }

        System.out.println();

        for (int i = 0; i < cantidadRelaciones; i++) {
            System.out.printf(
                    "%12s", relaciones.get(i).getId()
            );

            for (int j = 0; j < cantidadVertices; j++) {
                System.out.printf("%12s", matriz[i][j]);
            }

            System.out.println();
        }

        if (cantidadRelaciones == 0) {
            System.out.println("El grafo no tiene relaciones.");
        }
    }

    // =========================
    // LISTA DE INCIDENCIA
    // =========================

    public void mostrarListaIncidencia() {
        System.out.println("\n--- LISTA DE INCIDENCIA ---");

        if (relaciones.isEmpty()) {
            System.out.println("El grafo no tiene relaciones.");
            return;
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            System.out.print(
                    relacion.getId()
                    + " (peso: " + relacion.getPeso() + "): "
            );

            if (dirigido) {
                System.out.println(
                        relacion.getVertice1()
                        + " (-" + relacion.getPeso() + ") -> "
                        + relacion.getVertice2()
                        + " (+" + relacion.getPeso() + ")"
                );
            } else {
                System.out.println(
                        relacion.getVertice1()
                        + " (" + relacion.getPeso() + ") -- "
                        + relacion.getVertice2()
                        + " (" + relacion.getPeso() + ")"
                );
            }
        }
    }

    // =========================
    // OBTENER INDICE DE VERTICE
    // =========================

    private int obtenerIndiceVertice(String id) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return i;
            }
        }

        return -1;
    }
}
