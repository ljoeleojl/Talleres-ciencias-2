
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GrahamScan {

    public static List<Punto> calcular(List<Punto> puntos) {

        // Si hay menos de tres puntos, no se puede
        // formar un poligono convexo.
        if (puntos.size() < 3) {
            return new ArrayList<Punto>(puntos);
        }

        // Crear una copia para no modificar la nube original.
        List<Punto> ordenados = new ArrayList<Punto>(puntos);

        // Buscar el punto con menor coordenada Y.
        // En caso de empate, se selecciona el de menor X.
        Punto pivote = ordenados.get(0);

        for (int i = 1; i < ordenados.size(); i++) {

            Punto actual = ordenados.get(i);

            if (actual.y < pivote.y) {
                pivote = actual;
            } else if (actual.y == pivote.y && actual.x < pivote.x) {
                pivote = actual;
            }
        }

        // Ordenar los puntos por su angulo polar respecto
        // al pivote. Si tienen el mismo angulo, se ordenan
        // por distancia al pivote.
        final Punto puntoBase = pivote;

        Collections.sort(ordenados, new Comparator<Punto>() {

            @Override
            public int compare(Punto a, Punto b) {

                double anguloA = Math.atan2(
                    a.y - puntoBase.y,
                    a.x - puntoBase.x
                );

                double anguloB = Math.atan2(
                    b.y - puntoBase.y,
                    b.x - puntoBase.x
                );

                int comparacion = Double.compare(anguloA, anguloB);

                if (comparacion == 0) {

                    double distanciaA =
                        Punto.distanciaCuadrada(puntoBase, a);

                    double distanciaB =
                        Punto.distanciaCuadrada(puntoBase, b);

                    return Double.compare(distanciaA, distanciaB);
                }

                return comparacion;
            }
        });

        // Crear la pila que almacenara la envolvente.
        List<Punto> pila = new ArrayList<Punto>();

        // Recorrer todos los puntos ordenados.
        for (int i = 0; i < ordenados.size(); i++) {

            Punto actual = ordenados.get(i);

            // Mientras exista un giro hacia la derecha
            // o los puntos esten alineados, eliminar
            // el ultimo punto de la pila.
            while (pila.size() >= 2) {

                Punto ultimo = pila.get(pila.size() - 1);
                Punto anterior = pila.get(pila.size() - 2);

                double giro = Punto.orientacion(
                    anterior,
                    ultimo,
                    actual
                );

                if (giro <= 0) {
                    pila.remove(pila.size() - 1);
                } else {
                    break;
                }
            }

            // Agregar el punto actual a la pila.
            pila.add(actual);
        }

        return pila;
    }
}
