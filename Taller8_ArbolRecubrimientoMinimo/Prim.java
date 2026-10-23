import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Prim {

    private static class Arista {
        private int a;
        private int b;
        private double peso;
        private Relacion relacion;

        public Arista(int a, int b, double peso, Relacion relacion) {
            this.a = a;
            this.b = b;
            this.peso = peso;
            this.relacion = relacion;
        }
    }

    // =========================
    // EJECUTAR (con salida en consola)
    // =========================

    public static void ejecutar(Grafo grafo) {

        if (grafo.esDirigido()) {
            System.out.println(
                    "Prim solo aplica a grafos no dirigidos."
            );
            return;
        }

        List<Vertice> vertices = grafo.getVertices();

        if (vertices.isEmpty()) {
            System.out.println("El grafo no tiene vertices.");
            return;
        }

        List<Relacion> resultado =
                calcular(vertices, grafo.getRelaciones());

        mostrarResultados(vertices.size(), resultado);
    }

    // =========================
    // CALCULAR (sin imprimir)
    // Devuelve las relaciones del arbol de expansion minima.
    // Si el grafo no es conexo, devuelve un bosque.
    // =========================

    public static List<Relacion> calcular(
            List<Vertice> vertices,
            List<Relacion> relaciones) {

        int cantidad = vertices.size();

        Map<String, Integer> indices =
                new HashMap<String, Integer>();

        for (int i = 0; i < cantidad; i++) {
            indices.put(vertices.get(i).getId(), i);
        }

        // Lista de adyacencia con indices numericos.
        List<List<Arista>> adyacencias =
                new ArrayList<List<Arista>>();

        for (int i = 0; i < cantidad; i++) {
            adyacencias.add(new ArrayList<Arista>());
        }

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            Integer indiceA = indices.get(relacion.getVertice1());
            Integer indiceB = indices.get(relacion.getVertice2());

            if (indiceA == null || indiceB == null) {
                continue;
            }

            int a = indiceA;
            int b = indiceB;

            Arista arista =
                    new Arista(a, b, relacion.getPeso(), relacion);

            adyacencias.get(a).add(arista);

            if (a != b) {
                adyacencias.get(b).add(arista);
            }
        }

        boolean[] visitado = new boolean[cantidad];

        List<Relacion> resultado = new ArrayList<Relacion>();

        PriorityQueue<Arista> cola =
                new PriorityQueue<Arista>(
                        (x, y) -> Double.compare(x.peso, y.peso)
                );

        /*
         * Se arranca desde cada vertice no visitado, asi que
         * si el grafo no es conexo se obtiene un bosque.
         */
        for (int inicio = 0; inicio < cantidad; inicio++) {

            if (visitado[inicio]) {
                continue;
            }

            visitado[inicio] = true;

            agregarAristas(inicio, adyacencias, visitado, cola);

            while (!cola.isEmpty()) {
                Arista arista = cola.poll();

                // Si ambos extremos ya estan en el arbol, formaria un ciclo.
                if (visitado[arista.a] && visitado[arista.b]) {
                    continue;
                }

                int nuevo;

                if (visitado[arista.a]) {
                    nuevo = arista.b;
                } else {
                    nuevo = arista.a;
                }

                visitado[nuevo] = true;
                resultado.add(arista.relacion);

                agregarAristas(nuevo, adyacencias, visitado, cola);
            }
        }

        return resultado;
    }

    // Agrega a la cola las aristas del vertice que llevan a vertices nuevos.
    private static void agregarAristas(
            int vertice,
            List<List<Arista>> adyacencias,
            boolean[] visitado,
            PriorityQueue<Arista> cola) {

        List<Arista> aristas = adyacencias.get(vertice);

        for (int i = 0; i < aristas.size(); i++) {
            Arista arista = aristas.get(i);

            int otro;

            if (arista.a == vertice) {
                otro = arista.b;
            } else {
                otro = arista.a;
            }

            if (!visitado[otro]) {
                cola.add(arista);
            }
        }
    }

    // =========================
    // MOSTRAR RESULTADOS
    // =========================

    private static void mostrarResultados(
            int cantidadVertices,
            List<Relacion> resultado) {

        System.out.println("\n========== PRIM ==========");

        if (resultado.isEmpty()) {
            System.out.println("No hay relaciones que seleccionar.");
        } else {
            System.out.println("Relaciones seleccionadas:");

            for (int i = 0; i < resultado.size(); i++) {
                Relacion relacion = resultado.get(i);

                System.out.println(
                        "  " + relacion.getId() + ": "
                        + relacion.getVertice1() + " -- "
                        + relacion.getVertice2()
                        + " | Peso: " + relacion.getPeso()
                );
            }
        }

        double total = 0;

        for (int i = 0; i < resultado.size(); i++) {
            total += resultado.get(i).getPeso();
        }

        System.out.println("Peso total: " + total);

        int componentes = cantidadVertices - resultado.size();

        if (componentes > 1) {
            System.out.println(
                    "El grafo no es conexo: se muestra un bosque "
                    + "de expansion minima con "
                    + componentes + " componentes."
            );
        } else {
            System.out.println(
                    "El grafo es conexo: se muestra un arbol "
                    + "de expansion minima."
            );
        }
    }
}