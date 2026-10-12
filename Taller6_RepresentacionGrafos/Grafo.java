
import java.util.ArrayList;
import java.util.List;

public class Grafo {

    private List<Vertice> vertices;
    private List<Relacion> relaciones;
    private boolean dirigido;

    public Grafo(boolean dirigido) {
        vertices = new ArrayList<Vertice>();
        relaciones = new ArrayList<Relacion>();
        this.dirigido = dirigido;
    }

    public boolean esDirigido() {
        return dirigido;
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

    // CRUD DE VERTICES

    public boolean agregarVertice(String id, String dato) {
        if (id == null || id.trim().isEmpty()
                || buscarVertice(id) != null) {
            return false;
        }

        vertices.add(new Vertice(id, dato));
        return true;
    }

    public boolean eliminarVertice(String id) {
        if (buscarVertice(id) == null) {
            return false;
        }

        for (int i = relaciones.size() - 1; i >= 0; i--) {
            if (relaciones.get(i).contieneVertice(id)) {
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

    // CRUD DE RELACIONES

    public boolean agregarRelacion(String id, String vertice1,
                                   String vertice2, double peso) {

        if (id == null || id.trim().isEmpty()
                || buscarRelacion(id) != null) {
            return false;
        }

        if (buscarVertice(vertice1) == null
                || buscarVertice(vertice2) == null) {
            return false;
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            boolean mismaDireccion =
                    relacion.getVertice1().equals(vertice1)
                    && relacion.getVertice2().equals(vertice2);

            boolean direccionInversa =
                    relacion.getVertice1().equals(vertice2)
                    && relacion.getVertice2().equals(vertice1);

            if (mismaDireccion || (!dirigido && direccionInversa)) {
                return false;
            }
        }

        relaciones.add(
                new Relacion(id, vertice1, vertice2, peso)
        );

        return true;
    }

    public boolean eliminarRelacion(String id) {
        for (int i = 0; i < relaciones.size(); i++) {
            if (relaciones.get(i).getId().equals(id)) {
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

    // MOSTRAR VERTICES

    public void mostrarVertices() {
        System.out.println("\n========== VERTICES ==========");

        if (vertices.isEmpty()) {
            System.out.println("No hay vertices.");
            return;
        }

        for (int i = 0; i < vertices.size(); i++) {
            System.out.println(vertices.get(i));
        }
    }

    // MOSTRAR RELACIONES

    public void mostrarRelaciones() {
        System.out.println("\n========== RELACIONES ==========");

        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones.");
            return;
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            String separador = dirigido ? " -> " : " -- ";

            System.out.println(
                    relacion.getId() + ": "
                    + relacion.getVertice1()
                    + separador
                    + formatearNumero(relacion.getPeso())
                    + separador
                    + relacion.getVertice2()
            );
        }
    }

    // MATRIZ DE ADYACENCIA

    public void mostrarMatrizAdyacencia() {
        System.out.println("\n========== MATRIZ DE ADYACENCIA ==========");

        if (vertices.isEmpty()) {
            System.out.println("No hay vertices.");
            return;
        }

        int cantidadVertices = vertices.size();

        double[][] matriz =
                new double[cantidadVertices][cantidadVertices];

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            int posicion1 =
                    obtenerIndiceVertice(relacion.getVertice1());

            int posicion2 =
                    obtenerIndiceVertice(relacion.getVertice2());

            matriz[posicion1][posicion2] = relacion.getPeso();

            if (!dirigido) {
                matriz[posicion2][posicion1] = relacion.getPeso();
            }
        }

        System.out.printf("%12s", "");

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf("%12s", vertices.get(i).getId());
        }

        System.out.println();

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf("%12s", vertices.get(i).getId());

            for (int j = 0; j < cantidadVertices; j++) {
                System.out.printf(
                        "%12s",
                        formatearNumero(matriz[i][j])
                );
            }

            System.out.println();
        }
    }

    // LISTA DE ADYACENCIA

    public void mostrarListaAdyacencia() {
        System.out.println("\n========== LISTA DE ADYACENCIA ==========");

        if (vertices.isEmpty()) {
            System.out.println("No hay vertices.");
            return;
        }

        for (int i = 0; i < vertices.size(); i++) {
            Vertice vertice = vertices.get(i);
            String id = vertice.getId();

            System.out.print(id + " -> ");

            boolean tieneRelaciones = false;

            for (int j = 0; j < relaciones.size(); j++) {
                Relacion relacion = relaciones.get(j);
                String vecino = null;

                if (relacion.saleDe(id)) {
                    vecino = relacion.getVertice2();
                } else if (!dirigido
                        && relacion.llegaA(id)) {
                    vecino = relacion.getVertice1();
                }

                if (vecino != null) {
                    if (tieneRelaciones) {
                        System.out.print(", ");
                    }

                    System.out.print(
                            "(" + vecino
                            + ", peso="
                            + formatearNumero(relacion.getPeso())
                            + ")"
                    );

                    tieneRelaciones = true;
                }
            }

            if (!tieneRelaciones) {
                System.out.print("∅");
            }

            System.out.println();
        }
    }

    // MATRIZ DE INCIDENCIA
    // Filas: relaciones. Columnas: vertices.

    public void mostrarMatrizIncidencia() {
        System.out.println("\n========== MATRIZ DE INCIDENCIA ==========");

        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones.");
            return;
        }

        int cantidadRelaciones = relaciones.size();
        int cantidadVertices = vertices.size();

        double[][] matriz =
                new double[cantidadRelaciones][cantidadVertices];

        for (int i = 0; i < cantidadRelaciones; i++) {
            Relacion relacion = relaciones.get(i);

            int posicion1 =
                    obtenerIndiceVertice(relacion.getVertice1());

            int posicion2 =
                    obtenerIndiceVertice(relacion.getVertice2());

            if (posicion1 == posicion2) {
                // Convencion para un bucle dirigido:
                // su contribucion de incidencia es cero.
                continue;
            }

            if (dirigido) {
                matriz[i][posicion1] = -relacion.getPeso();
                matriz[i][posicion2] = relacion.getPeso();
            } else {
                matriz[i][posicion1] = relacion.getPeso();
                matriz[i][posicion2] = relacion.getPeso();
            }
        }

        System.out.printf("%12s", "Relacion");

        for (int i = 0; i < cantidadVertices; i++) {
            System.out.printf("%12s", vertices.get(i).getId());
        }

        System.out.println();

        for (int i = 0; i < cantidadRelaciones; i++) {
            System.out.printf(
                    "%12s", relaciones.get(i).getId()
            );

            for (int j = 0; j < cantidadVertices; j++) {
                System.out.printf(
                        "%12s",
                        formatearNumero(matriz[i][j])
                );
            }

            System.out.println();
        }
    }

    // LISTA DE INCIDENCIA

    public void mostrarListaIncidencia() {
        System.out.println("\n========== LISTA DE INCIDENCIA ==========");

        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones.");
            return;
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            String separador = dirigido ? " -> " : " -- ";

            System.out.println(
                    relacion.getId() + ": "
                    + relacion.getVertice1()
                    + separador
                    + formatearNumero(relacion.getPeso())
                    + separador
                    + relacion.getVertice2()
            );
        }
    }
}
