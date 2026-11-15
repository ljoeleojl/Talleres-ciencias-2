/*
 * Metricas de una ejecucion de un algoritmo de coloreado.
 */
public class ResultadoColoreado {

    private final String nombre;
    private final int cantidadColores;
    private final double tiempoMilisegundos;
    private final long inspecciones;
    private final long comparaciones;
    private final boolean valido;
    private final int[] colores;

    public ResultadoColoreado(
            String nombre,
            int cantidadColores,
            double tiempoMilisegundos,
            long inspecciones,
            long comparaciones,
            boolean valido,
            int[] colores) {

        this.nombre = nombre;
        this.cantidadColores = cantidadColores;
        this.tiempoMilisegundos = tiempoMilisegundos;
        this.inspecciones = inspecciones;
        this.comparaciones = comparaciones;
        this.valido = valido;
        this.colores = colores.clone();
    }

    public String getNombre() {
        return nombre;
    }

    // Cantidad de colores distintos utilizados.
    public int getCantidadColores() {
        return cantidadColores;
    }

    // Tiempo de calcularColoreado(), en milisegundos.
    public double getTiempoMilisegundos() {
        return tiempoMilisegundos;
    }

    // Celdas de la matriz de adyacencia leidas durante el algoritmo.
    public long getInspecciones() {
        return inspecciones;
    }

    // Comparaciones realizadas al ordenar los vertices (si aplica).
    public long getComparaciones() {
        return comparaciones;
    }

    // true si ningun par de vertices adyacentes comparte color.
    public boolean esValido() {
        return valido;
    }

    // Color (desde 0) de cada vertice, segun su posicion en el grafo.
    public int[] getColores() {
        return colores.clone();
    }
}