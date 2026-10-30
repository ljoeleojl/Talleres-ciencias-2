public class ResultadoFlujo {

    private final double flujoMaximo;
    private final long tiempoNanosegundos;
    private final long iteraciones;
    private final long inspeccionesAristas;

    public ResultadoFlujo(
            double flujoMaximo,
            long tiempoNanosegundos,
            long iteraciones,
            long inspeccionesAristas) {

        this.flujoMaximo = flujoMaximo;
        this.tiempoNanosegundos = tiempoNanosegundos;
        this.iteraciones = iteraciones;
        this.inspeccionesAristas = inspeccionesAristas;
    }

    public double getFlujoMaximo() {
        return flujoMaximo;
    }

    public long getTiempoNanosegundos() {
        return tiempoNanosegundos;
    }

    public double getTiempoMilisegundos() {
        return tiempoNanosegundos / 1_000_000.0;
    }

    public long getIteraciones() {
        return iteraciones;
    }

    public long getInspeccionesAristas() {
        return inspeccionesAristas;
    }
}