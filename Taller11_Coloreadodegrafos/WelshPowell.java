import java.util.Arrays;

/*
 * Coloreado de Welsh-Powell: igual que el voraz, pero procesando
 * los vertices por grado descendente.
 */
public class WelshPowell extends ColoreadoGrafo {

    public WelshPowell(Grafo grafo) {
        super(grafo);
    }

    protected String getNombre() {
        return "WELSH-POWELL";
    }

    protected int[] calcularColoreado() {
        int n = vertices.size();

        // El sort de objetos es estable: en empates se respeta
        // el orden de insercion de los vertices.
        Integer[] orden = new Integer[n];

        for (int i = 0; i < n; i++) {
            orden[i] = i;
        }

        Arrays.sort(orden, (a, b) -> grado[b] - grado[a]);

        int[] color = new int[n];
        Arrays.fill(color, -1);

        for (int k = 0; k < n; k++) {
            int v = orden[k];
            color[v] = menorColorDisponible(color, v);
        }

        return color;
    }
}