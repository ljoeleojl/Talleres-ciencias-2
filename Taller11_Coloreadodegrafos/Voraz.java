import java.util.Arrays;

/*
 * Coloreado voraz: recorre los vertices en orden de insercion y
 * asigna a cada uno el menor color que no usan sus vecinos.
 */
public class Voraz extends ColoreadoGrafo {

    public Voraz(Grafo grafo) {
        super(grafo);
    }

    protected String getNombre() {
        return "VORAZ";
    }

    protected int[] calcularColoreado() {
        int n = vertices.size();
        int[] color = new int[n];
        Arrays.fill(color, -1);

        for (int v = 0; v < n; v++) {
            color[v] = menorColorDisponible(color, v);
        }

        return color;
    }
}