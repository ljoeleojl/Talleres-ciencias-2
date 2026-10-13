
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    private static final double INFINITO =
            Double.POSITIVE_INFINITY;

    public static void ejecutar(Grafo grafo) {

        List<Vertice> vertices = grafo.getVertices();
        List<Relacion> relaciones = grafo.getRelaciones();

        int cantidadVertices = vertices.size();

        if (cantidadVertices == 0) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        double[][] distancias =
                new double[cantidadVertices][cantidadVertices];

        int[][] siguiente =
                new int[cantidadVertices][cantidadVertices];

        Map<String, Integer> indices =
                new HashMap<String, Integer>();

        // Asigna un indice a cada vertice.
        for (int i = 0; i < cantidadVertices; i++) {
            indices.put(vertices.get(i).getId(), i);
        }

        // Inicializa las matrices.
        for (int i = 0; i < cantidadVertices; i++) {
            for (int j = 0; j < cantidadVertices; j++) {

                if (i == j) {
                    distancias[i][j] = 0.0;
                    siguiente[i][j] = i;
                } else {
                    distancias[i][j] = INFINITO;
                    siguiente[i][j] = -1;
                }
            }
        }

        // Carga las relaciones existentes.
        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            int origen = indices.get(relacion.getVertice1());
            int destino = indices.get(relacion.getVertice2());

            if (relacion.getPeso() < distancias[origen][destino]) {
                distancias[origen][destino] = relacion.getPeso();
                siguiente[origen][destino] = destino;
            }

            if (!grafo.esDirigido()) {
                if (relacion.getPeso()
                        < distancias[destino][origen]) {

                    distancias[destino][origen] = relacion.getPeso();
                    siguiente[destino][origen] = origen;
                }
            }
        }

        /*
         * Cada vertice k se considera como posible
         * intermediario entre i y j.
         */
        for (int k = 0; k < cantidadVertices; k++) {
            for (int i = 0; i < cantidadVertices; i++) {
                for (int j = 0; j < cantidadVertices; j++) {

                    if (distancias[i][k] == INFINITO
                            || distancias[k][j] == INFINITO) {
                        continue;
                    }

                    double nuevaDistancia =
                            distancias[i][k] + distancias[k][j];

                    if (nuevaDistancia < distancias[i][j]) {
                        distancias[i][j] = nuevaDistancia;
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }

        // Comprueba si existe algun ciclo negativo.
        for (int i = 0; i < cantidadVertices; i++) {
            if (distancias[i][i] < 0) {
                System.out.println("\n===== FLOYD-WARSHALL =====");
                System.out.println(
                        "Se detecto un ciclo negativo."
                );

                System.out.println(
                        "Las distancias minimas de algunos pares "
                        + "no estan bien definidas."
                );

                return;
            }
        }

        // Muestra la matriz de distancias minimas.
        System.out.println("\n===== FLOYD-WARSHALL =====");
        System.out.println("Matriz de distancias minimas:");

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
                if (distancias[i][j] == INFINITO) {
                    System.out.printf("%12s", "-");
                } else {
                    System.out.printf(
                            "%12.2f", distancias[i][j]
                    );
                }
            }

            System.out.println();
        }

        // Muestra los caminos minimos.
        System.out.println("\nCaminos minimos entre todos los pares:");

        for (int i = 0; i < cantidadVertices; i++) {
            for (int j = 0; j < cantidadVertices; j++) {

                String origen = vertices.get(i).getId();
                String destino = vertices.get(j).getId();

                System.out.print(origen + " -> " + destino + ": ");

                if (siguiente[i][j] == -1) {
                    System.out.println("No existe camino.");
                } else {
                    System.out.print(
                            reconstruirCamino(
                                    i,
                                    j,
                                    siguiente,
                                    vertices
                            )
                    );

                    System.out.println(
                            " | Distancia: " + distancias[i][j]
                    );
                }
            }
        }
    }

    private static String reconstruirCamino(
            int origen,
            int destino,
            int[][] siguiente,
            List<Vertice> vertices) {

        if (siguiente[origen][destino] == -1) {
            return "No existe camino.";
        }

        StringBuilder camino = new StringBuilder();

        int actual = origen;

        camino.append(vertices.get(actual).getId());

        int pasos = 0;

        while (actual != destino) {
            actual = siguiente[actual][destino];

            if (actual == -1) {
                return "No existe camino.";
            }

            camino.append(" -> ");
            camino.append(vertices.get(actual).getId());

            pasos++;

            if (pasos > vertices.size()) {
                return "No se pudo reconstruir el camino.";
            }
        }

        return camino.toString();
    }
}
