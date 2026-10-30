
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FordFulkerson {

    private static final double EPSILON = 1e-9;

    // ==========================================
    // EJECUTAR FORD-FULKERSON
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

        while (buscarCaminoDFS(
                residual, s, t, padre)) {

            double flujoCamino =
                    Double.POSITIVE_INFINITY;

            // Encontrar capacidad minima
            // del camino encontrado.

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
    // BUSQUEDA DFS
    // ==========================================

    private static boolean buscarCaminoDFS(
            double[][] residual,
            int origen,
            int destino,
            int[] padre) {

        int n = residual.length;

        boolean[] visitado = new boolean[n];

        for (int i = 0; i < n; i++) {
            padre[i] = -1;
        }

        return dfs(
                residual,
                origen,
                destino,
                padre,
                visitado
        );
    }

    private static boolean dfs(
            double[][] residual,
            int actual,
            int destino,
            int[] padre,
            boolean[] visitado) {

        visitado[actual] = true;

        if (actual == destino) {
            return true;
        }

        for (int vecino = 0;
                vecino < residual.length;
                vecino++) {

            if (!visitado[vecino]
                    && residual[actual][vecino] > EPSILON) {

                padre[vecino] = actual;

                if (dfs(
                        residual,
                        vecino,
                        destino,
                        padre,
                        visitado)) {

                    return true;
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
                    "Ford-Fulkerson requiere un grafo dirigido."
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
