import java.util.Arrays;
import java.util.List;

public class FordFulkerson {

    // Margen de tolerancia para comparar valores decimales.
    private static final double EPSILON = 1e-9;

    public static ResultadoFlujo ejecutar(
            Grafo grafo, String origen, String destino) {

        // Comprueba que el grafo sea válido y que los vértices
        // de origen y destino existan.
        validarEntrada(grafo, origen, destino);

        // Registra el instante inicial para medir el tiempo de ejecución.
        long inicio = System.nanoTime();

        // Obtiene la lista de vértices y su cantidad.
        List<Vertice> vertices = grafo.getVertices();
        int n = vertices.size();

        // Convierte los identificadores de origen y destino
        // en sus posiciones dentro de la lista de vértices.
        int indiceOrigen = obtenerIndice(vertices, origen);
        int indiceDestino = obtenerIndice(vertices, destino);

        // Construye la matriz residual con las capacidades iniciales.
        double[][] residual = construirMatrizResidual(
                grafo, vertices);

        // Inicializa las variables que almacenan los resultados.
        double flujoMaximo = 0;
        long iteraciones = 0;
        long inspecciones = 0;

        // Busca caminos aumentantes hasta que ya no exista
        // un camino disponible desde el origen hasta el destino.
        while (true) {

            // El arreglo padre permite reconstruir el camino encontrado.
            // Cada posición almacena el vértice anterior en el camino.
            int[] padre = new int[n];
            Arrays.fill(padre, -1);

            // Indica qué vértices ya fueron visitados durante la búsqueda DFS.
            boolean[] visitado = new boolean[n];

            // Contador de posiciones de la matriz residual inspeccionadas.
            long[] contador = new long[1];

            // Busca un camino aumentante utilizando DFS.
            boolean encontrado = dfs(
                    indiceOrigen,
                    indiceDestino,
                    residual,
                    padre,
                    visitado,
                    contador);

            // Acumula las inspecciones realizadas durante esta búsqueda.
            inspecciones += contador[0];

            // Si no se encontró un camino, se alcanzó el flujo máximo.
            if (!encontrado) {
                break;
            }

            // Inicializa el flujo aumentante con infinito.
            // Después se calculará el mínimo de las capacidades
            // disponibles en las aristas del camino encontrado.
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

    // Implementa la búsqueda en profundidad (DFS) de forma recursiva.
    // Explora una ruta todo lo posible antes de retroceder y probar
    // otras conexiones disponibles.
    private static boolean dfs(
            int actual,
            int destino,
            double[][] residual,
            int[] padre,
            boolean[] visitado,
            long[] inspecciones) {

        // Caso base: si el vértice actual es el destino,
        // significa que se encontró un camino aumentante completo.
        if (actual == destino) {
            return true;
        }

        // Marca el vértice actual como visitado para evitar ciclos
        // y recorridos repetidos durante esta búsqueda.
        visitado[actual] = true;

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

                // Registra el vértice actual como padre del siguiente.
                // Esto permitirá reconstruir el camino encontrado.
                padre[siguiente] = actual;

                // Continúa la búsqueda recursivamente desde el siguiente
                // vértice para profundizar en esa ruta.
                if (dfs(
                        siguiente,
                        destino,
                        residual,
                        padre,
                        visitado,
                        inspecciones)) {

                    // Si la llamada recursiva encontró el destino,
                    // devuelve true y termina la búsqueda.
                    return true;
                }
            }
        }

        // Si no se encontró una ruta desde este vértice,
        // devuelve false para permitir retroceder y explorar otra conexión.
        return false;
    }

    // Construye la matriz residual a partir de las relaciones del grafo.
    // Las filas representan los vértices de origen y las columnas
    // representan los vértices de destino.
    static double[][] construirMatrizResidual(
            Grafo grafo, List<Vertice> vertices) {

        // Obtiene la cantidad de vértices y crea una matriz cuadrada.
        int n = vertices.size();
        double[][] residual = new double[n][n];

        // Recorre todas las relaciones originales del grafo.
        for (Relacion relacion : grafo.getRelaciones()) {

            // Obtiene la posición del vértice de origen de la relación.
            int origen = obtenerIndice(
                    vertices, relacion.getVertice1());

            // Obtiene la posición del vértice de destino de la relación.
            int destino = obtenerIndice(
                    vertices, relacion.getVertice2());

            // Asigna la capacidad de la relación a la matriz residual.
            // Se utiliza += para acumular capacidades si corresponde
            // más de una relación a la misma posición.
            residual[origen][destino] += relacion.getPeso();
        }

        // Devuelve la matriz residual construida.
        return residual;
    }

    // Busca la posición de un vértice dentro de la lista
    // utilizando su identificador.
    static int obtenerIndice(
            List<Vertice> vertices, String id) {

        // Recorre todos los vértices de la lista.
        for (int i = 0; i < vertices.size(); i++) {

            // Compara el identificador buscado con el del vértice actual.
            if (vertices.get(i).getId().equals(id)) {

                // Devuelve la posición cuando encuentra una coincidencia.
                return i;
            }
        }

        // Devuelve -1 si el identificador no corresponde a ningún vértice.
        return -1;
    }

    // Comprueba las condiciones necesarias para ejecutar
    // el algoritmo de flujo máximo.
    static void validarEntrada(
            Grafo grafo, String origen, String destino) {

        // Verifica que el grafo no sea nulo.
        if (grafo == null) {
            throw new IllegalArgumentException(
                    "El grafo no puede ser null.");
        }

        // Ford-Fulkerson requiere un grafo dirigido.
        if (!grafo.esDirigido()) {
            throw new IllegalArgumentException(
                    "Ford-Fulkerson requiere un grafo dirigido.");
        }

        // Comprueba que origen y destino tengan valores válidos
        // y que no sean el mismo vértice.
        if (origen == null || destino == null
                || origen.equals(destino)) {
            throw new IllegalArgumentException(
                    "El origen y el destino deben ser diferentes.");
        }

        // Verifica que los dos vértices existan en el grafo.
        if (grafo.buscarVertice(origen) == null
                || grafo.buscarVertice(destino) == null) {
            throw new IllegalArgumentException(
                    "El origen o el destino no existe.");
        }

        // Recorre las relaciones para comprobar que sus capacidades
        // sean valores finitos y no negativos.
        for (Relacion relacion : grafo.getRelaciones()) {
            double capacidad = relacion.getPeso();

            if (!Double.isFinite(capacidad) || capacidad < 0) {
                throw new IllegalArgumentException(
                        "Las capacidades deben ser finitas y no negativas.");
            }
        }
    }
}