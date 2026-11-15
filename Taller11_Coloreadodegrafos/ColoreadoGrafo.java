import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Clase base de los algoritmos de coloreado de grafos.
 *
 * Un coloreado propio asigna un color a cada vertice de modo que
 * dos vertices adyacentes nunca compartan color.
 *
 * - La direccion de las relaciones y los pesos se ignoran: solo
 *   importa si dos vertices son adyacentes. En un grafo dirigido,
 *   A -> B y B -> A cuentan como una sola adyacencia.
 * - Los algoritmos son heuristicas: dan un coloreado valido con a lo
 *   sumo (grado maximo + 1) colores, pero no garantizan el minimo
 *   (numero cromatico).
 *
 * Cada subclase (Voraz, WelshPowell, DSatur) solo define el orden
 * en que se procesan los vertices en calcularColoreado().
 *
 * La clase toma una "foto" del grafo al construirse; si el grafo
 * cambia despues, hay que crear una nueva instancia.
 */
public abstract class ColoreadoGrafo {

    protected final List<Vertice> vertices;
    protected final boolean[][] adyacente;
    protected final int[] grado;

    // Metricas de la ultima ejecucion (se reinician en ejecutar()).
    protected long inspecciones;
    protected long comparaciones;

    public ColoreadoGrafo(Grafo grafo) {
        this.vertices = grafo.getVertices();
        List<Relacion> relaciones = grafo.getRelaciones();

        int n = vertices.size();

        Map<String, Integer> indice = new HashMap<String, Integer>();

        for (int i = 0; i < n; i++) {
            indice.put(vertices.get(i).getId(), i);
        }

        boolean[][] matriz = new boolean[n][n];

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            Integer u = indice.get(relacion.getVertice1());
            Integer v = indice.get(relacion.getVertice2());

            // Se descartan extremos inexistentes y bucles (un bucle
            // nunca admite coloreado propio).
            if (u == null || v == null || u.intValue() == v.intValue()) {
                continue;
            }

            matriz[u][v] = true;
            matriz[v][u] = true;
        }

        int[] grados = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriz[i][j]) {
                    grados[i]++;
                }
            }
        }

        this.adyacente = matriz;
        this.grado = grados;
    }

    // =========================
    // A DEFINIR EN CADA ALGORITMO
    // =========================

    // Nombre del algoritmo (titulos y tablas).
    protected abstract String getNombre();

    // Devuelve el color (desde 0) de cada vertice, segun su posicion.
    protected abstract int[] calcularColoreado();

    // =========================
    // EJECUCION
    // =========================

    // Ejecuta el algoritmo y devuelve sus metricas, sin imprimir nada.
    public ResultadoColoreado ejecutar() {
        inspecciones = 0;
        comparaciones = 0;

        long inicio = System.nanoTime();
        int[] color = calcularColoreado();
        long fin = System.nanoTime();

        int cantidadColores = 0;

        for (int i = 0; i < color.length; i++) {
            if (color[i] + 1 > cantidadColores) {
                cantidadColores = color[i] + 1;
            }
        }

        return new ResultadoColoreado(
                getNombre(),
                cantidadColores,
                (fin - inicio) / 1_000_000.0,
                inspecciones,
                comparaciones,
                esColoreadoValido(color),
                color);
    }

    // Ejecuta el algoritmo y muestra el resultado por consola.
    public void colorear() {
        System.out.println("\n--- COLOREADO " + getNombre() + " ---");

        if (vertices.isEmpty()) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        mostrarResultado(ejecutar());
    }

    // =========================
    // AUXILIARES
    // =========================

    // Menor color (desde 0) que no usa ningun vecino ya coloreado.
    protected int menorColorDisponible(int[] color, int v) {
        int n = adyacente.length;
        boolean[] usado = new boolean[n + 1];

        for (int j = 0; j < n; j++) {
            inspecciones++;

            if (adyacente[v][j] && color[j] != -1) {
                usado[color[j]] = true;
            }
        }

        int c = 0;

        while (usado[c]) {
            c++;
        }

        return c;
    }

    // true si todos tienen color y ningun par adyacente lo comparte.
    private boolean esColoreadoValido(int[] color) {
        int n = color.length;

        for (int i = 0; i < n; i++) {
            if (color[i] < 0) {
                return false;
            }

            for (int j = i + 1; j < n; j++) {
                if (adyacente[i][j] && color[i] == color[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // Imprime el resultado (colores numerados desde 1).
    private void mostrarResultado(ResultadoColoreado resultado) {
        int[] color = resultado.getColores();
        int n = color.length;
        int cantidadColores = resultado.getCantidadColores();

        for (int i = 0; i < n; i++) {
            System.out.println(
                    "Vertice " + vertices.get(i).getId()
                            + " (grado " + grado[i] + ")"
                            + " -> Color " + (color[i] + 1));
        }

        System.out.println("\nColores utilizados: " + cantidadColores);

        for (int c = 0; c < cantidadColores; c++) {
            System.out.print("Color " + (c + 1) + ": ");

            boolean primero = true;

            for (int i = 0; i < n; i++) {
                if (color[i] == c) {
                    if (!primero) {
                        System.out.print(", ");
                    }

                    System.out.print(vertices.get(i).getId());
                    primero = false;
                }
            }

            System.out.println();
        }

        System.out.println(
                "Coloreado valido: "
                        + (resultado.esValido() ? "Si" : "No"));

        System.out.printf(
                "Tiempo: %.6f ms%n", resultado.getTiempoMilisegundos());

        System.out.println(
                "Inspecciones de adyacencia: "
                        + resultado.getInspecciones());
    }
}