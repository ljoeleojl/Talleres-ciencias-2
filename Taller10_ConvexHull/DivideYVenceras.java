
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DivideYVenceras {

    public static List<Punto> calcular(List<Punto> puntos) {

        // Si hay menos de tres puntos, se devuelven directamente.
        if (puntos.size() < 3) {
            return new ArrayList<Punto>(puntos);
        }

        // Crear una copia de los puntos originales.
        List<Punto> ordenados = new ArrayList<Punto>(puntos);

        // Ordenar los puntos por sus coordenadas X e Y.
        Collections.sort(ordenados, new Comparator<Punto>() {

            @Override
            public int compare(Punto a, Punto b) {

                int comparacionX = Double.compare(a.x, b.x);

                if (comparacionX == 0) {
                    return Double.compare(a.y, b.y);
                }

                return comparacionX;
            }
        });

        // Iniciar el proceso recursivo.
        return dividir(ordenados, 0, ordenados.size() - 1);
    }

    /*
     * Divide la lista de puntos en dos partes.
     *
     * Cada parte calcula su propia envolvente convexa.
     * Finalmente, se combinan ambas envolventes.
     */
    private static List<Punto> dividir(
            List<Punto> puntos,
            int inicio,
            int fin) {

        // Calcular la cantidad de puntos del segmento.
        int cantidad = fin - inicio + 1;

        // Caso base:
        // Si hay tres puntos o menos, calcular directamente
        // su envolvente utilizando Monotone Chain.
        if (cantidad <= 3) {

            List<Punto> pequenos = new ArrayList<Punto>();

            for (int i = inicio; i <= fin; i++) {
                pequenos.add(puntos.get(i));
            }

            return MonotoneChain.calcular(pequenos);
        }

        // Encontrar la mitad del segmento.
        int medio = inicio + (fin - inicio) / 2;

        // Dividir y calcular la envolvente izquierda.
        List<Punto> izquierda = dividir(
            puntos,
            inicio,
            medio
        );

        // Dividir y calcular la envolvente derecha.
        List<Punto> derecha = dividir(
            puntos,
            medio + 1,
            fin
        );

        // Combinar los vertices de ambas envolventes.
        List<Punto> candidatos = new ArrayList<Punto>();

        candidatos.addAll(izquierda);
        candidatos.addAll(derecha);

        // Calcular la envolvente definitiva de los candidatos.
        // Monotone Chain elimina los puntos interiores
        // y conserva solamente los vertices exteriores.
        return MonotoneChain.calcular(candidatos);
    }
}
