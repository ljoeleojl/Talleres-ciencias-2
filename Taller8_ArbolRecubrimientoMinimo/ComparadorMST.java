import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComparadorMST {

    private static final int REPETICIONES = 5;

    private interface Algoritmo {
        List<Relacion> ejecutar(
                List<Vertice> vertices,
                List<Relacion> relaciones);
    }

    private static class Medicion {
        private double milisegundos;
        private long kilobytes;
        private double pesoTotal;
    }

    // =========================
    // EJECUTAR COMPARACION
    // =========================

    public static void ejecutar() {

        int[] tamanos = {100, 500, 1000, 2000};

        System.out.println("\n========== COMPARACION PRIM VS KRUSKAL ==========");
        System.out.println("Cada caso se repite " + REPETICIONES
                + " veces y se muestra el promedio.");
        System.out.println("Generando grafos aleatorios, espere...\n");

        System.out.printf(
                "%-8s %-9s %-12s %12s %12s %12s %12s %-9s %-8s%n",
                "Vertices", "Aristas", "Tipo",
                "Prim (ms)", "Kruskal (ms)",
                "Prim (KB)", "Kruskal (KB)",
                "Mismo peso", "Mas rapido"
        );

        Random azar = new Random(42);

        for (int i = 0; i < tamanos.length; i++) {
            int n = tamanos[i];

            // Poco denso: unas 3 aristas por vertice.
            comparar(n, 3 * n, "Poco denso", azar);

            // Denso: 12.5% de todas las aristas posibles.
            comparar(n, n * (n - 1) / 8, "Denso", azar);
        }

    }

    // =========================
    // COMPARAR UN CASO
    // =========================

    private static void comparar(
            int n,
            int m,
            String tipo,
            Random azar) {

        List<Vertice> vertices = new ArrayList<Vertice>();
        List<Relacion> relaciones = new ArrayList<Relacion>();

        generarGrafo(n, m, azar, vertices, relaciones);

        Algoritmo prim = (v, r) -> Prim.calcular(v, r);
        Algoritmo kruskal = (v, r) -> Kruskal.calcular(v, r);

        Medicion medicionPrim = medir(prim, vertices, relaciones);
        Medicion medicionKruskal = medir(kruskal, vertices, relaciones);

        boolean mismoPeso =
                Math.abs(
                        medicionPrim.pesoTotal
                        - medicionKruskal.pesoTotal
                ) < 1e-9;

        String ganador;

        if (medicionPrim.milisegundos < medicionKruskal.milisegundos) {
            ganador = "Prim";
        } else {
            ganador = "Kruskal";
        }

        System.out.printf(
                "%-8d %-9d %-12s %12.3f %12.3f %12s %12s %-9s %-8s%n",
                n,
                relaciones.size(),
                tipo,
                medicionPrim.milisegundos,
                medicionKruskal.milisegundos,
                formatearKilobytes(medicionPrim.kilobytes),
                formatearKilobytes(medicionKruskal.kilobytes),
                mismoPeso ? "Si" : "No",
                ganador
        );
    }

    // =========================
    // GENERAR GRAFO ALEATORIO CONEXO
    // =========================

    private static void generarGrafo(
            int n,
            int m,
            Random azar,
            List<Vertice> vertices,
            List<Relacion> relaciones) {

        String[] ids = new String[n];

        for (int i = 0; i < n; i++) {
            ids[i] = "v" + i;
            vertices.add(new Vertice(ids[i], "dato"));
        }

        Set<Long> usados = new HashSet<Long>();

        // Primero un arbol aleatorio, para garantizar que sea conexo.
        for (int i = 1; i < n; i++) {
            int j = azar.nextInt(i);

            usados.add((long) j * n + i);

            relaciones.add(
                    new Relacion(
                            "e" + relaciones.size(),
                            ids[i],
                            ids[j],
                            1 + azar.nextInt(1000)
                    )
            );
        }

        // Despues aristas aleatorias sin repetir pares.
        while (relaciones.size() < m) {
            int a = azar.nextInt(n);
            int b = azar.nextInt(n);

            if (a == b) {
                continue;
            }

            int menor = Math.min(a, b);
            int mayor = Math.max(a, b);

            if (usados.add((long) menor * n + mayor)) {
                relaciones.add(
                        new Relacion(
                                "e" + relaciones.size(),
                                ids[a],
                                ids[b],
                                1 + azar.nextInt(1000)
                        )
                );
            }
        }
    }

    // =========================
    // MEDIR TIEMPO Y MEMORIA
    // =========================

    private static Medicion medir(
            Algoritmo algoritmo,
            List<Vertice> vertices,
            List<Relacion> relaciones) {

        // Una ejecucion de calentamiento, que no se mide.
        algoritmo.ejecutar(vertices, relaciones);

        long tiempoTotal = 0;
        long bytesTotal = 0;
        boolean memoriaDisponible = true;
        List<Relacion> resultado = null;

        for (int i = 0; i < REPETICIONES; i++) {
            long bytesAntes = bytesAsignados();
            long inicio = System.nanoTime();

            resultado = algoritmo.ejecutar(vertices, relaciones);

            long fin = System.nanoTime();
            long bytesDespues = bytesAsignados();

            tiempoTotal += fin - inicio;

            if (bytesAntes < 0 || bytesDespues < 0) {
                memoriaDisponible = false;
            } else {
                bytesTotal += bytesDespues - bytesAntes;
            }
        }

        Medicion medicion = new Medicion();

        medicion.milisegundos =
                tiempoTotal / (double) REPETICIONES / 1000000.0;

        if (memoriaDisponible) {
            medicion.kilobytes = bytesTotal / REPETICIONES / 1024;
        } else {
            medicion.kilobytes = -1;
        }

        double peso = 0;

        for (int i = 0; i < resultado.size(); i++) {
            peso += resultado.get(i).getPeso();
        }

        medicion.pesoTotal = peso;

        return medicion;
    }

    // Bytes asignados por el hilo actual, o -1 si la JVM no lo permite.
    @SuppressWarnings("deprecation")
    private static long bytesAsignados() {
        try {
            com.sun.management.ThreadMXBean bean =
                    (com.sun.management.ThreadMXBean)
                    ManagementFactory.getThreadMXBean();

            if (!bean.isThreadAllocatedMemorySupported()) {
                return -1;
            }

            if (!bean.isThreadAllocatedMemoryEnabled()) {
                bean.setThreadAllocatedMemoryEnabled(true);
            }

            return bean.getThreadAllocatedBytes(
                    Thread.currentThread().getId()
            );
        } catch (Throwable e) {
            return -1;
        }
    }

    private static String formatearKilobytes(long kilobytes) {
        if (kilobytes < 0) {
            return "n/d";
        }

        return String.valueOf(kilobytes);
    }
}