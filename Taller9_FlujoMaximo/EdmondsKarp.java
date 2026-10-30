import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class EdmondsKarp {

    // Margen de tolerancia para comparar valores decimales.
    private static final double EPSILON = 1e-9;

    public static ResultadoFlujo ejecutar(
            Grafo grafo, String origen, String destino) {

        // Comprueba que el grafo sea válido y que los vértices
        // de origen y destino existan.
        FordFulkerson.validarEntrada(grafo, origen, destino);

        // Registra el instante inicial para medir el tiempo de ejecución.
        long inicio = System.nanoTime();

        // Obtiene la lista de vértices y su cantidad.
        List<Vertice> vertices = grafo.getVertices();
        int n = vertices.size();

        // Convierte los identificadores de origen y destino
        // en sus posiciones dentro de la lista de vértices.
        int indiceOrigen =
                FordFulkerson.obtenerIndice(vertices, origen);

        int indiceDestino =
                FordFulkerson.obtenerIndice(vertices, destino);

        // Construye la matriz residual con las capacidades iniciales.
        // Esta matriz representa la capacidad que todavía se puede utilizar
        // para enviar flujo entre cada par de vértices.
        double[][] residual =
                FordFulkerson.construirMatrizResidual(
                        grafo, vertices);

        // Inicializa las variables que almacenan los resultados.
        double flujoMaximo = 0;
        long iteraciones = 0;
        long inspecciones = 0;

        // Busca caminos aumentantes hasta que ya no exista
        // un camino disponible desde el origen hasta el destino.
        while (true) {

            // El arreglo padre permite reconstruir el camino encontrado.
            // padre[v] almacena el vértice anterior a v en el camino.
            int[] padre = new int[n];
            Arrays.fill(padre, -1);

            // Contador de posiciones de la matriz residual inspeccionadas
            // durante la búsqueda BFS.
            long[] contador = new long[1];

            // Busca un camino aumentante utilizando BFS.
            boolean encontrado = bfs(
                    indiceOrigen,
                    indiceDestino,
                    residual,
                    padre,
                    contador);

            // Acumula las inspecciones realizadas en esta búsqueda.
            inspecciones += contador[0];

            // Si no se encontró un camino, se alcanzó el flujo máximo.
            if (!encontrado) {
                break;
            }

            // Inicializa el flujo aumentante con infinito.
            // Después se calculará el mínimo de las capacidades
            // disponibles en todas las aristas del camino.
            double flujoAumentante = Double.POSITIVE_INFINITY;

            // Comienza a recorrer el camino desde el destino
            // hasta llegar al origen, utilizando el arreglo padre.
            int actual = indiceDestino;

            while (actual != indiceOrigen) {

                // Obtiene el vértice anterior en el camino.
                int anterior = padre[actual];

                // Actualiza el flujo aumentante con la menor capacidad
                // residual encontrada hasta el momento.
                flujoAumentante = Math.min(
                        flujoAumentante,
                        residual[anterior][actual]);

                // Continúa hacia el vértice anterior.
                actual = anterior;
            }

            // Evita procesar flujos nulos, demasiado pequeños
            // o valores que no sean finitos.
            if (flujoAumentante <= EPSILON
                    || !Double.isFinite(flujoAumentante)) {
                break;
            }

            // Recorre nuevamente el camino para actualizar
            // las capacidades de la matriz residual.
            actual = indiceDestino;

            while (actual != indiceOrigen) {

                // Obtiene el vértice anterior en el camino.
                int anterior = padre[actual];

                // Reduce la capacidad disponible de la arista utilizada,
                // porque se acaba de enviar flujo por ella.
                residual[anterior][actual] -= flujoAumentante;

                // Si el valor residual es prácticamente cero,
                // lo establece exactamente en cero para evitar
                // errores de precisión con números decimales.
                if (Math.abs(residual[anterior][actual]) < EPSILON) {
                    residual[anterior][actual] = 0;
                }

                // Aumenta la capacidad residual en sentido contrario.
                // Esto permite devolver flujo o modificar decisiones
                // tomadas en caminos aumentantes anteriores.
                residual[actual][anterior] += flujoAumentante;

                // Continúa recorriendo el camino hacia el origen.
                actual = anterior;
            }

            // Suma el flujo enviado por el camino encontrado
            // al flujo máximo acumulado.
            flujoMaximo += flujoAumentante;

            // Cuenta una iteración por cada camino aumentante utilizado.
            iteraciones++;
        }

        // Calcula el tiempo total de ejecución en nanosegundos.
        long tiempo = System.nanoTime() - inicio;

        // Devuelve el flujo máximo y las métricas de ejecución.
        return new ResultadoFlujo(
                flujoMaximo,
                tiempo,
                iteraciones,
                inspecciones);
    }

    // Implementa la búsqueda en anchura (BFS).
    // Su objetivo es encontrar un camino desde el origen
    // hasta el destino utilizando únicamente capacidades residuales positivas.
    private static boolean bfs(
            int origen,
            int destino,
            double[][] residual,
            int[] padre,
            long[] inspecciones) {

        // Indica qué vértices ya fueron visitados para evitar
        // recorrer varias veces el mismo vértice.
        boolean[] visitado = new boolean[residual.length];

        // La cola permite procesar los vértices en orden de llegada,
        // característica principal de la búsqueda en anchura.
        Queue<Integer> cola = new ArrayDeque<>();

        // Introduce el vértice de origen en la cola y lo marca como visitado.
        cola.add(origen);
        visitado[origen] = true;

        // Continúa mientras existan vértices pendientes por explorar.
        while (!cola.isEmpty()) {

            // Extrae el primer vértice de la cola.
            int actual = cola.poll();

            // Examina las posibles conexiones del vértice actual
            // con todos los demás vértices.
            for (int siguiente = 0;
                    siguiente < residual.length;
                    siguiente++) {

                // Registra que se inspeccionó una posición de la matriz.
                inspecciones[0]++;

                // Comprueba que el siguiente vértice no haya sido visitado
                // y que exista capacidad residual para llegar hasta él.
                if (!visitado[siguiente]
                        && residual[actual][siguiente] > EPSILON) {

                    // Registra el vértice anterior para reconstruir el camino.
                    padre[siguiente] = actual;

                    // Marca el vértice como visitado.
                    visitado[siguiente] = true;

                    // Si se alcanzó el destino, se encontró un camino.
                    // Se termina la búsqueda inmediatamente.
                    if (siguiente == destino) {
                        return true;
                    }

                    // Si todavía no es el destino, se agrega a la cola
                    // para explorar sus conexiones posteriormente.
                    cola.add(siguiente);
                }
            }
        }

        // Si se vació la cola sin alcanzar el destino,
        // no existe un camino aumentante disponible.
        return false;
    }
}