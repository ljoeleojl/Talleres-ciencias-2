
public class Punto {

    // Coordenadas del punto en el plano cartesiano.
    public double x;
    public double y;

    // Constructor del punto.
    public Punto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /*
     * Calcula la orientación de tres puntos.
     *
     * Resultado:
     * Positivo: giro hacia la izquierda.
     * Negativo: giro hacia la derecha.
     * Cero: los tres puntos están alineados.
     *
     * Se utiliza el producto cruzado para realizar
     * este cálculo.
     */
    public static double orientacion(Punto a, Punto b, Punto c) {

        return (b.x - a.x) * (c.y - a.y)
             - (b.y - a.y) * (c.x - a.x);
    }

    // Calcula la distancia al cuadrado entre dos puntos.
    // No utiliza raiz cuadrada porque no es necesaria
    // para comparar distancias.
    public static double distanciaCuadrada(Punto a, Punto b) {

        double dx = a.x - b.x;
        double dy = a.y - b.y;

        return dx * dx + dy * dy;
    }

    // Representación del punto como coordenadas.
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
