
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {

    private static final double INFINITO =
            Double.POSITIVE_INFINITY;

    private static class NodoDistancia {
        private String vertice;
        private double distancia;

        public NodoDistancia(String vertice, double distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }
    }

    public static void ejecutar(Grafo grafo, String origen) {

        List<Vertice> vertices = grafo.getVertices();

        if (!existeVertice(vertices, origen)) {
            System.out.println("El vertice de origen no existe.");
            return;
        }

        Map<String, List<Relacion>> adyacencias =
                construirListaAdyacencia(grafo);

        // Dijkstra no admite pesos negativos.
        for (Map.Entry<String, List<Relacion>> entrada
                : adyacencias.entrySet()) {

            List<Relacion> relaciones = entrada.getValue();

            for (int i = 0; i < relaciones.size(); i++) {
                if (relaciones.get(i).getPeso() < 0) {
                    System.out.println(
                            "Dijkstra no admite pesos negativos."
                    );
                    return;
                }
            }
        }

        Map<String, Double> distancias =
                new LinkedHashMap<String, Double>();

        Map<String, String> predecesores =
                new HashMap<String, String>();

        for (int i = 0; i < vertices.size(); i++) {
            String id = vertices.get(i).getId();

            distancias.put(id, INFINITO);
            predecesores.put(id, null);
        }

        distancias.put(origen, 0.0);

        PriorityQueue<NodoDistancia> cola =
                new PriorityQueue<NodoDistancia>(
                        (a, b) -> Double.compare(
                                a.distancia,
                                b.distancia
                        )
                );

        cola.add(new NodoDistancia(origen, 0.0));

        while (!cola.isEmpty()) {

            NodoDistancia actual = cola.poll();

            double mejorDistancia =
                    distancias.get(actual.vertice);

            // Ignora entradas antiguas de la cola.
            if (actual.distancia > mejorDistancia) {
                continue;
            }

            List<Relacion> relaciones =
                    adyacencias.get(actual.vertice);

            for (int i = 0; i < relaciones.size(); i++) {
                Relacion relacion = relaciones.get(i);

                String destino;

                if (relacion.getVertice1().equals(actual.vertice)) {
                    destino = relacion.getVertice2();
                } else {
                    destino = relacion.getVertice1();
                }

                double nuevaDistancia =
                        actual.distancia + relacion.getPeso();

                if (nuevaDistancia < distancias.get(destino)) {
                    distancias.put(destino, nuevaDistancia);
                    predecesores.put(destino, actual.vertice);

                    cola.add(
                            new NodoDistancia(
                                    destino,
                                    nuevaDistancia
                            )
                    );
                }
            }
        }

        System.out.println("\n========== DIJKSTRA ==========");
        System.out.println("Vertice de origen: " + origen);

        mostrarResultados(
                vertices,
                origen,
                distancias,
                predecesores
        );
    }

    private static Map<String, List<Relacion>>
            construirListaAdyacencia(Grafo grafo) {

        Map<String, List<Relacion>> adyacencias =
                new LinkedHashMap<String, List<Relacion>>();

        List<Vertice> vertices = grafo.getVertices();
        List<Relacion> relaciones = grafo.getRelaciones();

        for (int i = 0; i < vertices.size(); i++) {
            String id = vertices.get(i).getId();

            adyacencias.put(id, new ArrayList<Relacion>());
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            adyacencias.get(relacion.getVertice1()).add(relacion);

            if (!grafo.esDirigido()) {
                adyacencias.get(relacion.getVertice2()).add(relacion);
            }
        }

        return adyacencias;
    }

    private static boolean existeVertice(
            List<Vertice> vertices,
            String id) {

        if (id == null) {
            return false;
        }

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    private static void mostrarResultados(
            List<Vertice> vertices,
            String origen,
            Map<String, Double> distancias,
            Map<String, String> predecesores) {

        for (int i = 0; i < vertices.size(); i++) {
            String destino = vertices.get(i).getId();
            double distancia = distancias.get(destino);

            System.out.print(origen + " -> " + destino + ": ");

            if (distancia == INFINITO) {
                System.out.println("No existe camino.");
            } else {
                List<String> camino = reconstruirCamino(
                        origen,
                        destino,
                        predecesores
                );

                System.out.println(
                        String.join(" -> ", camino)
                        + " | Distancia: " + distancia
                );
            }
        }
    }

    private static List<String> reconstruirCamino(
            String origen,
            String destino,
            Map<String, String> predecesores) {

        List<String> camino = new ArrayList<String>();
        String actual = destino;

        while (actual != null) {
            camino.add(0, actual);

            if (actual.equals(origen)) {
                return camino;
            }

            actual = predecesores.get(actual);
        }

        return new ArrayList<String>();
    }
}
