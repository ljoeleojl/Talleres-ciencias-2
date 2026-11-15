
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MonotoneChain {

    public static List<Punto> calcular(List<Punto> puntos) {

        // Si hay menos de tres puntos, se devuelven directamente.
        if (puntos.size() < 3) {
            return new ArrayList<Punto>(puntos);
        }

        // Crear una copia para conservar los puntos originales.
        List<Punto> ordenados = new ArrayList<Punto>(puntos);

        // Ordenar primero por X y despues por Y.
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

        // Construir la envolvente inferior.
        List<Punto> inferior = new ArrayList<Punto>();

        for (int i = 0; i < ordenados.size(); i++) {

            Punto actual = ordenados.get(i);

            while (inferior.size() >= 2) {

                Punto ultimo = inferior.get(inferior.size() - 1);
                Punto anterior = inferior.get(inferior.size() - 2);

                double giro = Punto.orientacion(
                    anterior,
                    ultimo,
                    actual
                );

                // Eliminar giros hacia la derecha
                // y puntos alineados.
                if (giro <= 0) {
                    inferior.remove(inferior.size() - 1);
                } else {
                    break;
                }
            }

            inferior.add(actual);
        }

        // Construir la envolvente superior.
        List<Punto> superior = new ArrayList<Punto>();

        for (int i = ordenados.size() - 1; i >= 0; i--) {

            Punto actual = ordenados.get(i);

            while (superior.size() >= 2) {

                Punto ultimo = superior.get(superior.size() - 1);
                Punto anterior = superior.get(superior.size() - 2);

                double giro = Punto.orientacion(
                    anterior,
                    ultimo,
                    actual
                );

                // Eliminar puntos que no pertenecen
                // al contorno superior.
                if (giro <= 0) {
                    superior.remove(superior.size() - 1);
                } else {
                    break;
                }
            }

            superior.add(actual);
        }

        // Eliminar el ultimo punto de cada recorrido
        // porque aparece repetido en ambas envolventes.
        inferior.remove(inferior.size() - 1);
        superior.remove(superior.size() - 1);

        // Unir la envolvente inferior y superior.
        List<Punto> resultado = new ArrayList<Punto>();

        resultado.addAll(inferior);
        resultado.addAll(superior);

        return resultado;
    }
}
