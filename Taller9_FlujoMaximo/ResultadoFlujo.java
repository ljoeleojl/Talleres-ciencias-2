
import java.util.ArrayList;
import java.util.List;

public class ResultadoFlujo {

    private double flujoMaximo;
    private List<String> caminos;
    private List<Double> flujosCaminos;

    public ResultadoFlujo(double flujoMaximo) {

        this.flujoMaximo = flujoMaximo;
        this.caminos = new ArrayList<>();
        this.flujosCaminos = new ArrayList<>();
    }

    public double getFlujoMaximo() {
        return flujoMaximo;
    }

    public List<String> getCaminos() {
        return new ArrayList<>(caminos);
    }

    public List<Double> getFlujosCaminos() {
        return new ArrayList<>(flujosCaminos);
    }

    public void agregarCamino(
            String camino,
            double flujo) {

        caminos.add(camino);
        flujosCaminos.add(flujo);
    }

    public void mostrarResultado() {

        System.out.println("\n==================================");
        System.out.println("          RESULTADO");
        System.out.println("==================================");

        if (caminos.isEmpty()) {

            System.out.println(
                    "No se encontraron caminos aumentantes."
            );

        } else {

            System.out.println("\nCaminos aumentantes:");

            for (int i = 0; i < caminos.size(); i++) {

                System.out.println(
                        (i + 1) + ". "
                        + caminos.get(i)
                        + " | Flujo enviado: "
                        + flujosCaminos.get(i)
                );
            }
        }

        System.out.println("\n----------------------------------");

        System.out.println(
                "FLUJO MAXIMO: " + flujoMaximo
        );

        System.out.println("==================================");
    }
}
