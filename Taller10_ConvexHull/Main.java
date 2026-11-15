
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {

    // Cantidad de puntos que se utilizaran en el laboratorio.
    private static final int CANTIDAD_PUNTOS = 1000000;

    // Cantidad de repeticiones para cada algoritmo.
    private static final int REPETICIONES = 3;

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       LABORATORIO CONVEX HULL");
        System.out.println("======================================");

        // Generar la nube de puntos.
        System.out.println("\nGenerando nube de puntos...");

        List<Punto> puntos = generarPuntos(CANTIDAD_PUNTOS);

        System.out.println("Puntos generados: " + puntos.size());

        // Realizar un calentamiento con una parte de la nube.
        // Esto permite que la JVM optimice el codigo antes
        // de comenzar las mediciones principales.
        System.out.println("\nRealizando calentamiento...");

        int cantidadCalentamiento = 20000;

        List<Punto> muestra = new ArrayList<Punto>(
            puntos.subList(0, cantidadCalentamiento)
        );

        GrahamScan.calcular(muestra);
        MonotoneChain.calcular(muestra);
        DivideYVenceras.calcular(muestra);

        System.out.println("Calentamiento finalizado.");

        // Arreglos para almacenar los tiempos de ejecucion.
        long[] tiemposGraham = new long[REPETICIONES];
        long[] tiemposMonotone = new long[REPETICIONES];
        long[] tiemposDivide = new long[REPETICIONES];

        // Variables para almacenar los resultados.
        List<Punto> resultadoGraham = null;
        List<Punto> resultadoMonotone = null;
        List<Punto> resultadoDivide = null;

        // Ejecutar Graham Scan varias veces.
        System.out.println("\nEjecutando Graham Scan...");

        for (int i = 0; i < REPETICIONES; i++) {

            long inicio = System.nanoTime();

            resultadoGraham = GrahamScan.calcular(puntos);

            long fin = System.nanoTime();

            tiemposGraham[i] = fin - inicio;

            System.out.println(
                "Repeticion " + (i + 1) + ": "
                + convertirMilisegundos(tiemposGraham[i]) + " ms"
            );
        }

        // Ejecutar Monotone Chain varias veces.
        System.out.println("\nEjecutando Monotone Chain...");

        for (int i = 0; i < REPETICIONES; i++) {

            long inicio = System.nanoTime();

            resultadoMonotone = MonotoneChain.calcular(puntos);

            long fin = System.nanoTime();

            tiemposMonotone[i] = fin - inicio;

            System.out.println(
                "Repeticion " + (i + 1) + ": "
                + convertirMilisegundos(tiemposMonotone[i]) + " ms"
            );
        }

        // Ejecutar Divide and Conquer varias veces.
        System.out.println("\nEjecutando Divide and Conquer...");

        for (int i = 0; i < REPETICIONES; i++) {

            long inicio = System.nanoTime();

            resultadoDivide = DivideYVenceras.calcular(puntos);

            long fin = System.nanoTime();

            tiemposDivide[i] = fin - inicio;

            System.out.println(
                "Repeticion " + (i + 1) + ": "
                + convertirMilisegundos(tiemposDivide[i]) + " ms"
            );
        }

        // Calcular la mediana de los tiempos.
        double medianaGraham = calcularMediana(tiemposGraham);
        double medianaMonotone = calcularMediana(tiemposMonotone);
        double medianaDivide = calcularMediana(tiemposDivide);

        // Mostrar los resultados finales.
        System.out.println("\n======================================");
        System.out.println("          RESULTADOS FINALES");
        System.out.println("======================================");

        System.out.println("\nAlgoritmo        Tiempo mediano (ms)");

        System.out.println(
            "Graham Scan      " + String.format("%.3f", medianaGraham)
        );

        System.out.println(
            "Monotone Chain   " + String.format("%.3f", medianaMonotone)
        );

        System.out.println(
            "Divide Conquer   " + String.format("%.3f", medianaDivide)
        );

        System.out.println("\nVertices encontrados:");

        System.out.println(
            "Graham Scan: " + resultadoGraham.size()
        );

        System.out.println(
            "Monotone Chain: " + resultadoMonotone.size()
        );

        System.out.println(
            "Divide and Conquer: " + resultadoDivide.size()
        );

        // Comprobar que los tres algoritmos obtengan
        // exactamente los mismos vertices.
        boolean resultadosIguales = sonIguales(
            resultadoGraham,
            resultadoMonotone
        );

        resultadosIguales = resultadosIguales
            && sonIguales(resultadoGraham, resultadoDivide);

        System.out.println("\nVerificacion de resultados:");

        if (resultadosIguales) {
            System.out.println(
                "CORRECTO: Los tres algoritmos obtuvieron "
                + "la misma envolvente convexa."
            );
        } else {
            System.out.println(
                "ERROR: Los resultados de los algoritmos "
                + "son diferentes."
            );
        }

        // Determinar cual algoritmo obtuvo el menor tiempo.
        System.out.println("\nAlgoritmo mas rapido:");

        if (medianaGraham < medianaMonotone
                && medianaGraham < medianaDivide) {

            System.out.println("Graham Scan");

        } else if (medianaMonotone < medianaGraham
                && medianaMonotone < medianaDivide) {

            System.out.println("Monotone Chain");

        } else if (medianaDivide < medianaGraham
                && medianaDivide < medianaMonotone) {

            System.out.println("Divide and Conquer");

        } else {

            System.out.println(
                "Existe un empate en los tiempos medidos."
            );
        }

        System.out.println("\n======================================");
        System.out.println("       FIN DEL LABORATORIO");
        System.out.println("======================================");
    }

    /*
     * Genera una nube de puntos aleatorios.
     *
     * Se utiliza una semilla fija para que los experimentos
     * puedan repetirse con los mismos datos.
     */
    public static List<Punto> generarPuntos(int cantidad) {

        List<Punto> puntos = new ArrayList<Punto>();

        Random aleatorio = new Random(12345);

        for (int i = 0; i < cantidad; i++) {

            double x = aleatorio.nextDouble() * 1000000;
            double y = aleatorio.nextDouble() * 1000000;

            puntos.add(new Punto(x, y));
        }

        return puntos;
    }

    /*
     * Convierte nanosegundos a milisegundos.
     *
     * Recibe double para permitir tanto tiempos long
     * como resultados decimales de la mediana.
     */
    public static double convertirMilisegundos(double tiempo) {

        return tiempo / 1000000.0;
    }

    /*
     * Calcula la mediana de los tiempos registrados.
     *
     * Primero ordena los tiempos y despues selecciona
     * el valor central.
     */
    public static double calcularMediana(long[] tiempos) {

        long[] copia = Arrays.copyOf(tiempos, tiempos.length);

        Arrays.sort(copia);

        int medio = copia.length / 2;

        if (copia.length % 2 == 1) {

            return convertirMilisegundos(copia[medio]);

        } else {

            double suma = (double) copia[medio - 1] + copia[medio];

            return convertirMilisegundos(suma / 2.0);
        }
    }

    /*
     * Comprueba si dos envolventes contienen exactamente
     * los mismos puntos, independientemente de su orden.
     */
    public static boolean sonIguales(
            List<Punto> primera,
            List<Punto> segunda) {

        if (primera.size() != segunda.size()) {
            return false;
        }

        // Crear copias para ordenar los resultados.
        List<Punto> copiaPrimera =
            new ArrayList<Punto>(primera);

        List<Punto> copiaSegunda =
            new ArrayList<Punto>(segunda);

        // Comparador por coordenadas X e Y.
        Comparator<Punto> comparador = new Comparator<Punto>() {

            @Override
            public int compare(Punto a, Punto b) {

                int comparacionX = Double.compare(a.x, b.x);

                if (comparacionX == 0) {
                    return Double.compare(a.y, b.y);
                }

                return comparacionX;
            }
        };

        Collections.sort(copiaPrimera, comparador);
        Collections.sort(copiaSegunda, comparador);

        // Comparar las coordenadas de cada posicion.
        for (int i = 0; i < copiaPrimera.size(); i++) {

            Punto a = copiaPrimera.get(i);
            Punto b = copiaSegunda.get(i);

            if (Double.compare(a.x, b.x) != 0) {
                return false;
            }

            if (Double.compare(a.y, b.y) != 0) {
                return false;
            }
        }

        return true;
    }
}
