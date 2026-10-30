import java.util.Locale;
import java.util.Scanner;

public class ComparadorFlujo {

    public static void ejecutar(
            Grafo grafo, Scanner scanner) {

        System.out.println("\n=== COMPARACIÓN DE FLUJO MÁXIMO ===");

        System.out.print("Vértice de origen [S]: ");
        String origen = scanner.nextLine().trim();

        if (origen.isEmpty()) {
            origen = "S";
        }

        System.out.print("Vértice de destino [T]: ");
        String destino = scanner.nextLine().trim();

        if (destino.isEmpty()) {
            destino = "T";
        }

        try {
            comparar(grafo, origen, destino);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void comparar(
            Grafo grafo, String origen, String destino) {

        ResultadoFlujo resultadoFF =
                FordFulkerson.ejecutar(grafo, origen, destino);

        ResultadoFlujo resultadoEK =
                EdmondsKarp.ejecutar(grafo, origen, destino);

        System.out.println("\nORIGEN: " + origen);
        System.out.println("DESTINO: " + destino);

        System.out.println("\nRESULTADOS");

        System.out.printf(
                "%-30s %18s %18s%n",
                "Métrica",
                "Ford-Fulkerson",
                "Edmonds-Karp");

        System.out.println(
                "--------------------------------------------------------------------------");

        System.out.printf(
                Locale.US,
                "%-30s %18.4f %18.4f%n",
                "Flujo máximo",
                resultadoFF.getFlujoMaximo(),
                resultadoEK.getFlujoMaximo());

        System.out.printf(
                Locale.US,
                "%-30s %18.6f %18.6f%n",
                "Tiempo (ms)",
                resultadoFF.getTiempoMilisegundos(),
                resultadoEK.getTiempoMilisegundos());

        System.out.printf(
                "%-30s %18d %18d%n",
                "Iteraciones",
                resultadoFF.getIteraciones(),
                resultadoEK.getIteraciones());

        System.out.printf(
                "%-30s %18d %18d%n",
                "Inspecciones de aristas",
                resultadoFF.getInspeccionesAristas(),
                resultadoEK.getInspeccionesAristas());

        System.out.println(
                "--------------------------------------------------------------------------");

        if (Math.abs(
                resultadoFF.getFlujoMaximo()
                - resultadoEK.getFlujoMaximo()) < 1e-9) {

            System.out.println(
                    "Verificación: ambos algoritmos obtuvieron el mismo flujo máximo.");

        } else {
            System.out.println(
                    "ADVERTENCIA: los resultados de flujo máximo son diferentes.");
        }

        System.out.println(
                "\nNota: el tiempo depende del equipo y de las condiciones de ejecución.");
    }
}