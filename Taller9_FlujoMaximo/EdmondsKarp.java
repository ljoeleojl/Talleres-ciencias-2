
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EdmondsKarp {

    private static final double EPSILON = 1e-9;

    // ==========================================
    // EJECUTAR EDMONDS-KARP
    // ==========================================

    public static ResultadoFlujo ejecutar(
            Grafo grafo,
            String origen,
            String destino) {

        validarEntrada(grafo, origen, destino);

        int n = grafo.getVertices().size();

        int s = grafo.obtenerIndiceVertice(origen);
        int t = grafo.obtenerIndiceVertice(destino);

        double[][] residual = construirRedResidual(grafo);

        double flujoMaximo = 0;

        ResultadoFlujo resultado =
                new ResultadoFlujo(0);

        int[] padre = new int[n];

        // ======================================
        // BUSCAR CAMINOS AUMENTANTES
        // ======================================

        while (buscarCaminoBFS(
                residual, s, t, padre)) {

            double flujoCamino =
                    Double.POSITIVE_INFINITY;

            // Encontrar capacidad minima.

            for (int v = t; v != s; v = padre[v]) {

                int u = padre[v];

                flujoCamino = Math.min(
                        flujoCamino,
                        residual[u][v]
                );
            }

            // Actualizar red residual.

            for (int v = t; v != s; v = padre[v]) {

                int u = padre[v];

                residual[u][v] -= flujoCamino;

                residual[v][u] += flujoCamino;
            }

            flujoMaximo += flujoCamino;

            // Guardar camino encontrado.

            String camino = construirCamino(
                    grafo,
                    padre,
                    s,
                    t
            );

            resultado.agregarCamino(
                    camino,
                    flujoCamino
            );
        }

        // ======================================
        // RESULTADO FINAL
        // ======================================

        ResultadoFlujo finalResultado =
                new ResultadoFlujo(flujoMaximo);

        for (int i = 0;
                i < resultado.getCaminos().size();
                i++) {

            finalResultado.agregarCamino(
                    resultado.getCaminos().get(i),
                    resultado.getFlujosCaminos().get(i)
            );
        }

        return finalResultado;
    }

    // ==========================================
    // BUSQUEDA BFS
    // ==========================================

    private static boolean buscarCaminoBFS(
            double[][] residual,
            int origen,
            int destino,
            int[] padre) {

        int n = residual.length;

        boolean[] visitado = new boolean[n];

        for (int i = 0; i < n; i++) {
            padre[i] = -1;
        }

        Queue<Integer> cola = new LinkedList<>();

        cola.add(origen);

        visitado[origen] = true;

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            for (int vecino = 0;
                    vecino < n;
                    vecino++) {

                if (!visitado[vecino]
                        && residual[actual][vecino] > EPSILON) {

                    padre[vecino] = actual;

                    visitado[vecino] = true;

                    if (vecino == destino) {
                        return true;
                    }

                    cola.add(vecino);
                }
            }
        }

        return false;
    }

    // ==========================================
    // CONSTRUIR RED RESIDUAL
    // ==========================================

    private static double[][] construirRedResidual(
            Grafo grafo) {

        int n = grafo.getVertices().size();

        double[][] residual = new double[n][n];

        for (Relacion r : grafo.getRelaciones()) {

            int origen =
                    grafo.obtenerIndiceVertice(
                            r.getVertice1()
                    );

            int destino =
                    grafo.obtenerIndiceVertice(
                            r.getVertice2()
                    );

            residual[origen][destino] += r.getPeso();
        }

        return residual;
    }

    // ==========================================
    // CONSTRUIR CAMINO
    // ==========================================

    private static String construirCamino(
            Grafo grafo,
            int[] padre,
            int origen,
            int destino) {

        List<String> camino = new ArrayList<>();

        for (int actual = destino;
                actual != -1;
                actual = padre[actual]) {

            camino.add(
                    grafo.getVertices()
                            .get(actual)
                            .getId()
            );

            if (actual == origen) {
                break;
            }
        }

        Collections.reverse(camino);

        return String.join(" -> ", camino);
    }

    // ==========================================
    // VALIDAR ENTRADA
    // ==========================================

    private static void validarEntrada(
            Grafo grafo,
            String origen,
            String destino) {

        if (grafo == null) {
            throw new IllegalArgumentException(
                    "El grafo no puede ser nulo."
            );
        }

        if (!grafo.esDirigido()) {
            throw new IllegalArgumentException(
                    "Edmonds-Karp requiere un grafo dirigido."
            );
        }

        if (origen == null || destino == null
                || grafo.buscarVertice(origen) == null
                || grafo.buscarVertice(destino) == null) {

            throw new IllegalArgumentException(
                    "El origen o destino no existe."
            );
        }

        if (origen.equals(destino)) {
            throw new IllegalArgumentException(
                    "El origen y el destino deben ser diferentes."
            );
        }
    }
}
