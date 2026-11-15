import java.util.Arrays;

/*
 * Coloreado DSATUR: en cada paso se elige el vertice sin colorear
 * con mayor saturacion (cantidad de colores distintos entre sus
 * vecinos ya coloreados). Los empates se rompen por mayor grado y,
 * si persisten, por orden de insercion.
 */
public class DSatur extends ColoreadoGrafo {

    public DSatur(Grafo grafo) {
        super(grafo);
    }

    protected String getNombre() {
        return "DSATUR";
    }

    protected int[] calcularColoreado() {
        int n = vertices.size();
        int[] color = new int[n];
        Arrays.fill(color, -1);

        for (int paso = 0; paso < n; paso++) {
            int elegido = -1;
            int mejorSaturacion = -1;

            for (int v = 0; v < n; v++) {
                if (color[v] != -1) {
                    continue;
                }

                int saturacion = calcularSaturacion(color, v);

                if (saturacion > mejorSaturacion
                        || (saturacion == mejorSaturacion
                                && grado[v] > grado[elegido])) {

                    elegido = v;
                    mejorSaturacion = saturacion;
                }
            }

            color[elegido] = menorColorDisponible(color, elegido);
        }

        return color;
    }

    // Cantidad de colores distintos entre los vecinos coloreados.
    private int calcularSaturacion(int[] color, int v) {
        int n = adyacente.length;
        boolean[] visto = new boolean[n + 1];
        int saturacion = 0;

        for (int j = 0; j < n; j++) {
            if (adyacente[v][j] && color[j] != -1 && !visto[color[j]]) {
                visto[color[j]] = true;
                saturacion++;
            }
        }

        return saturacion;
    }
}