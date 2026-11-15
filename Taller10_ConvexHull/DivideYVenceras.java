
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DivideYVenceras {

    private static final double EPSILON = 1e-10;

    public static List<Punto> calcular(List<Punto> puntos) {

        if (puntos == null || puntos.isEmpty()) {
            return new ArrayList<>();
        }

        // Ordenar los puntos por coordenada X y luego por Y.
        List<Punto> ordenados = new ArrayList<>(puntos);

        Collections.sort(ordenados, new Comparator<Punto>() {
            @Override
            public int compare(Punto p1, Punto p2) {

                int comparacionX = Double.compare(p1.x, p2.x);

                if (comparacionX != 0) {
                    return comparacionX;
                }

                return Double.compare(p1.y, p2.y);
            }
        });

        return resolver(ordenados, 0, ordenados.size());
    }

    private static List<Punto> resolver(
            List<Punto> puntos, int inicio, int fin) {

        int cantidad = fin - inicio;

        // Caso base: resolver directamente grupos pequeños.
        if (cantidad <= 3) {

            List<Punto> grupo = new ArrayList<>(
                    puntos.subList(inicio, fin));

            return MonotoneChain.calcular(grupo);
        }

        // Dividir los puntos en dos mitades.
        int medio = inicio + cantidad / 2;

        List<Punto> izquierda = resolver(
                puntos, inicio, medio);

        List<Punto> derecha = resolver(
                puntos, medio, fin);

        // Combinar las dos envolventes mediante tangentes.
        return combinar(izquierda, derecha);
    }

    private static List<Punto> combinar(
            List<Punto> izquierda,
            List<Punto> derecha) {

        if (izquierda.isEmpty()) {
            return new ArrayList<>(derecha);
        }

        if (derecha.isEmpty()) {
            return new ArrayList<>(izquierda);
        }

        /*
         * Si alguna envolvente tiene menos de tres vértices,
         * se utiliza Monotone Chain para manejar el caso
         * degenerado.
         */
        if (izquierda.size() < 3 || derecha.size() < 3) {
            return combinarCasoDegenerado(izquierda, derecha);
        }

        /*
         * Las dos mitades deben estar separadas horizontalmente.
         * Si comparten coordenadas X en el límite, se utiliza
         * el método de respaldo.
         */
        Punto extremoIzquierdo = puntoDerechoMasBajo(izquierda);
        Punto extremoDerecho = puntoIzquierdoMasBajo(derecha);

        if (extremoIzquierdo.x >= extremoDerecho.x) {
            return combinarCasoDegenerado(izquierda, derecha);
        }

        // Encontrar la tangente superior.
        int[] tangenteSuperior = encontrarTangenteSuperior(
                izquierda, derecha);

        // Encontrar la tangente inferior.
        int[] tangenteInferior = encontrarTangenteInferior(
                izquierda, derecha);

        int indiceSuperiorIzquierda = tangenteSuperior[0];
        int indiceSuperiorDerecha = tangenteSuperior[1];

        int indiceInferiorIzquierda = tangenteInferior[0];
        int indiceInferiorDerecha = tangenteInferior[1];

        List<Punto> resultado = new ArrayList<>();

        /*
         * Recorrer la envolvente izquierda desde el punto
         * de la tangente superior hasta el de la inferior.
         */
        int i = indiceSuperiorIzquierda;

        while (true) {

            resultado.add(izquierda.get(i));

            if (i == indiceInferiorIzquierda) {
                break;
            }

            i = (i + 1) % izquierda.size();
        }

        /*
         * Recorrer la envolvente derecha desde el punto
         * de la tangente inferior hasta el de la superior.
         */
        int j = indiceInferiorDerecha;

        while (true) {

            resultado.add(derecha.get(j));

            if (j == indiceSuperiorDerecha) {
                break;
            }

            j = (j + 1) % derecha.size();
        }

        return resultado;
    }

    private static int[] encontrarTangenteSuperior(
            List<Punto> izquierda,
            List<Punto> derecha) {

        int n = izquierda.size();
        int m = derecha.size();

        // Comenzar en el extremo derecho de la izquierda
        // y en el extremo izquierdo de la derecha.
        int i = indiceDerechoMasBajo(izquierda);
        int j = indiceIzquierdoMasBajo(derecha);

        boolean cambio = true;

        while (cambio) {

            cambio = false;

            // Ajustar el punto de la envolvente izquierda.
            for (int pasos = 0; pasos < n; pasos++) {

                int siguiente = (i + 1) % n;

                double orientacion = Punto.orientacion(
                        derecha.get(j),
                        izquierda.get(i),
                        izquierda.get(siguiente));

                if (orientacion < -EPSILON) {
                    i = siguiente;
                    cambio = true;
                } else {
                    break;
                }
            }

            // Ajustar el punto de la envolvente derecha.
            for (int pasos = 0; pasos < m; pasos++) {

                int anterior = (j - 1 + m) % m;

                double orientacion = Punto.orientacion(
                        izquierda.get(i),
                        derecha.get(j),
                        derecha.get(anterior));

                if (orientacion > EPSILON) {
                    j = anterior;
                    cambio = true;
                } else {
                    break;
                }
            }
        }

        return new int[] { i, j };
    }

    private static int[] encontrarTangenteInferior(
            List<Punto> izquierda,
            List<Punto> derecha) {

        int n = izquierda.size();
        int m = derecha.size();

        int i = indiceDerechoMasBajo(izquierda);
        int j = indiceIzquierdoMasBajo(derecha);

        boolean cambio = true;

        while (cambio) {

            cambio = false;

            // Ajustar el punto de la envolvente izquierda.
            for (int pasos = 0; pasos < n; pasos++) {

                int anterior = (i - 1 + n) % n;

                double orientacion = Punto.orientacion(
                        derecha.get(j),
                        izquierda.get(i),
                        izquierda.get(anterior));

                if (orientacion > EPSILON) {
                    i = anterior;
                    cambio = true;
                } else {
                    break;
                }
            }

            // Ajustar el punto de la envolvente derecha.
            for (int pasos = 0; pasos < m; pasos++) {

                int siguiente = (j + 1) % m;

                double orientacion = Punto.orientacion(
                        izquierda.get(i),
                        derecha.get(j),
                        derecha.get(siguiente));

                if (orientacion < -EPSILON) {
                    j = siguiente;
                    cambio = true;
                } else {
                    break;
                }
            }
        }

        return new int[] { i, j };
    }

    // Devuelve el índice del punto más a la derecha.
    // En caso de empate, selecciona el de menor Y.
    private static int indiceDerechoMasBajo(
            List<Punto> puntos) {

        int indice = 0;

        for (int i = 1; i < puntos.size(); i++) {

            Punto actual = puntos.get(i);
            Punto seleccionado = puntos.get(indice);

            if (actual.x > seleccionado.x
                    || (actual.x == seleccionado.x
                    && actual.y < seleccionado.y)) {

                indice = i;
            }
        }

        return indice;
    }

    // Devuelve el índice del punto más a la izquierda.
    // En caso de empate, selecciona el de menor Y.
    private static int indiceIzquierdoMasBajo(
            List<Punto> puntos) {

        int indice = 0;

        for (int i = 1; i < puntos.size(); i++) {

            Punto actual = puntos.get(i);
            Punto seleccionado = puntos.get(indice);

            if (actual.x < seleccionado.x
                    || (actual.x == seleccionado.x
                    && actual.y < seleccionado.y)) {

                indice = i;
            }
        }

        return indice;
    }

    private static Punto puntoDerechoMasBajo(
            List<Punto> puntos) {

        return puntos.get(indiceDerechoMasBajo(puntos));
    }

    private static Punto puntoIzquierdoMasBajo(
            List<Punto> puntos) {

        return puntos.get(indiceIzquierdoMasBajo(puntos));
    }

    // Método de respaldo para casos degenerados.
    private static List<Punto> combinarCasoDegenerado(
            List<Punto> izquierda,
            List<Punto> derecha) {

        List<Punto> puntos = new ArrayList<>();

        puntos.addAll(izquierda);
        puntos.addAll(derecha);

        return MonotoneChain.calcular(puntos);
    }
}
