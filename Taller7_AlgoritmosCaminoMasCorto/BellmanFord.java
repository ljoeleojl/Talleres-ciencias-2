
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BellmanFord {

    private static final double INFINITO =
            Double.POSITIVE_INFINITY;

    private static class Arista {
        private String origen;
        private String destino;
        private double peso;

        public Arista(
                String origen,
                String destino,
                double peso) {

            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }

    public static void ejecutar(Grafo grafo, String origen) {

        List<Vertice> vertices = grafo.getVertices();

        if (!existeVertice(vertices, origen)) {
            System.out.println("El vertice de origen no existe.");
            return;
        }

        List<Arista> aristas = construirListaAristas(grafo);

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

        /*
         * Se relajan las aristas como maximo V - 1 veces.
         */
        for (int i = 0; i < vertices.size() - 1; i++) {

            boolean huboCambios = false;

            for (int j = 0; j < aristas.size(); j++) {
                Arista arista = aristas.get(j);

                double distanciaOrigen =
                        distancias.get(arista.origen);

                if (distanciaOrigen == INFINITO) {
                    continue;
                }

                double nuevaDistancia =
                        distanciaOrigen + arista.peso;

                if (nuevaDistancia
                        < distancias.get(arista.destino)) {

                    distancias.put(
                            arista.destino,
                            nuevaDistancia
                    );

                    predecesores.put(
                            arista.destino,
                            arista.origen
                    );

                    huboCambios = true;
                }
            }

            // Termina anticipadamente si las distancias no cambian.
            if (!huboCambios) {
                break;
            }
        }

        /*
         * Una mejora adicional indica un ciclo negativo
         * alcanzable desde el origen.
         */
        for (int i = 0; i < aristas.size(); i++) {
            Arista arista = aristas.get(i);

            double distanciaOrigen =
                    distancias.get(arista.origen);

            if (distanciaOrigen == INFINITO) {
                continue;
            }

            if (distanciaOrigen + arista.peso
                    < distancias.get(arista.destino)) {

                System.out.println("\n======= BELLMAN-FORD =======");
                System.out.println(
                        "Existe un ciclo negativo alcanzable "
                        + "desde el origen " + origen + "."
                );

                System.out.println(
                        "No se pueden garantizar distancias "
                        + "minimas bien definidas."
                );

                return;
            }
        }

        System.out.println("\n======= BELLMAN-FORD =======");
        System.out.println("Vertice de origen: " + origen);

        mostrarResultados(
                vertices,
                origen,
                distancias,
                predecesores
        );
    }

    private static List<Arista> construirListaAristas(
            Grafo grafo) {

        List<Arista> aristas = new ArrayList<Arista>();
        List<Relacion> relaciones = grafo.getRelaciones();

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            aristas.add(
                    new Arista(
                            relacion.getVertice1(),
                            relacion.getVertice2(),
                            relacion.getPeso()
                    )
            );

            if (!grafo.esDirigido()) {
                aristas.add(
                        new Arista(
                                relacion.getVertice2(),
                                relacion.getVertice1(),
                                relacion.getPeso()
                        )
                );
            }
        }

        return aristas;
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
