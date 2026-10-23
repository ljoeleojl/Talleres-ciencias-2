import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {

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
                    "Kruskal solo aplica a grafos no dirigidos."
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

        List<Arista> aristas = new ArrayList<Arista>();

        for (int i = 0; i < relaciones.size(); i++) {
            Relacion relacion = relaciones.get(i);

            Integer indiceA = indices.get(relacion.getVertice1());
            Integer indiceB = indices.get(relacion.getVertice2());

            if (indiceA == null || indiceB == null) {
                continue;
            }

            aristas.add(
                    new Arista(
                            indiceA,
                            indiceB,
                            relacion.getPeso(),
                            relacion
                    )
            );
        }

        // Se ordenan las aristas de menor a mayor peso.
        aristas.sort((x, y) -> Double.compare(x.peso, y.peso));

        // Union-Find: cada vertice empieza en su propio conjunto.
        int[] padre = new int[cantidad];
        int[] rango = new int[cantidad];

        for (int i = 0; i < cantidad; i++) {
            padre[i] = i;
        }

        List<Relacion> resultado = new ArrayList<Relacion>();

        for (int i = 0; i < aristas.size(); i++) {

            // Un arbol de V vertices tiene V - 1 relaciones.
            if (resultado.size() == cantidad - 1) {
                break;
            }

            Arista arista = aristas.get(i);

            int raizA = buscar(padre, arista.a);
            int raizB = buscar(padre, arista.b);

            // Si estan en el mismo conjunto, la arista formaria un ciclo.
            if (raizA != raizB) {
                unir(padre, rango, raizA, raizB);
                resultado.add(arista.relacion);
            }
        }

        return resultado;
    }

    // Busca la raiz del conjunto, comprimiendo el camino.
    private static int buscar(int[] padre, int x) {
        while (padre[x] != x) {
            padre[x] = padre[padre[x]];
            x = padre[x];
        }

        return x;
    }

    // Une dos conjuntos por rango.
    private static void unir(
            int[] padre,
            int[] rango,
            int raizA,
            int raizB) {

        if (rango[raizA] < rango[raizB]) {
            padre[raizA] = raizB;
        } else if (rango[raizA] > rango[raizB]) {
            padre[raizB] = raizA;
        } else {
            padre[raizB] = raizA;
            rango[raizA]++;
        }
    }

    // =========================
    // MOSTRAR RESULTADOS
    // =========================

    private static void mostrarResultados(
            int cantidadVertices,
            List<Relacion> resultado) {

        System.out.println("\n========== KRUSKAL ==========");

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