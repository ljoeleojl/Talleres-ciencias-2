import java.util.Arrays;
import java.util.List;

/*
 * Ejecuta los tres algoritmos de coloreado sobre el mismo grafo y
 * muestra sus metricas en una tabla.
 *
 * El grafo es pequeno y cada ejecucion dura microsegundos, asi que
 * el tiempo se mide sobre varias repeticiones y se reporta la
 * mediana (mas robusta que el promedio ante pausas del recolector de
 * basura o del sistema), despues de un calentamiento para que la JVM
 * compile el codigo antes de medir.
 */
public class ComparadorColoreado {

    private static final int CALENTAMIENTO = 200;
    private static final int REPETICIONES = 1000;

    public static void ejecutar(Grafo grafo) {
        if (grafo.getVertices().isEmpty()) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        ColoreadoGrafo[] algoritmos = {
                new Voraz(grafo),
                new WelshPowell(grafo),
                new DSatur(grafo)
        };

        // Calentamiento (no se mide).
        for (int k = 0; k < algoritmos.length; k++) {
            for (int r = 0; r < CALENTAMIENTO; r++) {
                algoritmos[k].ejecutar();
            }
        }

        // Medicion: tiempo mediano; las demas metricas son
        // deterministas y se toman de la ultima repeticion.
        ResultadoColoreado[] resultados = new ResultadoColoreado[algoritmos.length];
        double[] tiempoMediano = new double[algoritmos.length];

        for (int k = 0; k < algoritmos.length; k++) {
            double[] tiempos = new double[REPETICIONES];
            ResultadoColoreado ultimo = null;

            for (int r = 0; r < REPETICIONES; r++) {
                ultimo = algoritmos[k].ejecutar();
                tiempos[r] = ultimo.getTiempoMilisegundos();
            }

            Arrays.sort(tiempos);

            resultados[k] = ultimo;
            tiempoMediano[k] = tiempos[REPETICIONES / 2];
        }

        imprimirTabla(resultados, tiempoMediano);
        imprimirClases(grafo, resultados);
    }

    private static void imprimirTabla(
            ResultadoColoreado[] resultados, double[] tiempoMediano) {

        String formatoFila = "%-32s%-16s%-16s%-16s%n";

        System.out.println("\n=== COMPARACION DE ALGORITMOS DE COLOREADO ===");

        System.out.printf(formatoFila, "Metrica",
                resultados[0].getNombre(),
                resultados[1].getNombre(),
                resultados[2].getNombre());

        System.out.printf(formatoFila, "Colores utilizados",
                resultados[0].getCantidadColores(),
                resultados[1].getCantidadColores(),
                resultados[2].getCantidadColores());

        System.out.printf(formatoFila, "Coloreado valido",
                resultados[0].esValido() ? "Si" : "No",
                resultados[1].esValido() ? "Si" : "No",
                resultados[2].esValido() ? "Si" : "No");

        System.out.printf(formatoFila, "Tiempo mediano (ms)",
                String.format("%.6f", tiempoMediano[0]),
                String.format("%.6f", tiempoMediano[1]),
                String.format("%.6f", tiempoMediano[2]));

        System.out.printf(formatoFila, "Inspecciones de adyacencia",
                resultados[0].getInspecciones(),
                resultados[1].getInspecciones(),
                resultados[2].getInspecciones());

        System.out.printf(formatoFila, "Comparaciones de ordenamiento",
                resultados[0].getComparaciones(),
                resultados[1].getComparaciones(),
                resultados[2].getComparaciones());

        int minimo = resultados[0].getCantidadColores();

        for (int k = 1; k < resultados.length; k++) {
            if (resultados[k].getCantidadColores() < minimo) {
                minimo = resultados[k].getCantidadColores();
            }
        }

        StringBuilder mejores = new StringBuilder();

        for (int k = 0; k < resultados.length; k++) {
            if (resultados[k].getCantidadColores() == minimo) {
                if (mejores.length() > 0) {
                    mejores.append(", ");
                }

                mejores.append(resultados[k].getNombre());
            }
        }

        System.out.println("\nMenor cantidad de colores: " + minimo
                + " (" + mejores + ")");

        System.out.println("Tiempo mediano de " + REPETICIONES
                + " ejecuciones, tras " + CALENTAMIENTO
                + " de calentamiento.");
    }

    // Muestra, por algoritmo, los vertices agrupados por color.
    private static void imprimirClases(
            Grafo grafo, ResultadoColoreado[] resultados) {

        List<Vertice> vertices = grafo.getVertices();

        for (int k = 0; k < resultados.length; k++) {
            int[] color = resultados[k].getColores();

            System.out.println("\n" + resultados[k].getNombre() + ":");

            for (int c = 0; c < resultados[k].getCantidadColores(); c++) {
                System.out.print("  Color " + (c + 1) + ": ");

                boolean primero = true;

                for (int i = 0; i < color.length; i++) {
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
        }
    }
}