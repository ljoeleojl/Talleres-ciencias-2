import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Grafo grafo = new Grafo(true);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    crearGrafoDirigido();
                    break;

                case 2:
                    crearGrafoNoDirigido();
                    break;

                case 3:
                    grafo = crearGrafoEjemplo();
                    System.out.println(
                            "Grafo de ejemplo generado correctamente.");
                    System.out.println("Origen: S | Destino: T");
                    System.out.println(
                            "Vértices: " + grafo.getVertices().size());
                    System.out.println(
                            "Relaciones: " + grafo.getRelaciones().size());
                    break;

                case 4:
                    agregarVertice();
                    break;

                case 5:
                    actualizarVertice();
                    break;

                case 6:
                    eliminarVertice();
                    break;

                case 7:
                    agregarRelacion();
                    break;

                case 8:
                    actualizarRelacion();
                    break;

                case 9:
                    eliminarRelacion();
                    break;

                case 10:
                    grafo.mostrarMatrizAdyacencia();
                    break;

                case 11:
                    grafo.mostrarListaAdyacencia();
                    break;

                case 12:
                    grafo.mostrarMatrizIncidencia();
                    break;

                case 13:
                    grafo.mostrarListaIncidencia();
                    break;

                case 14:
                    ejecutarFordFulkerson();
                    break;

                case 15:
                    ejecutarEdmondsKarp();
                    break;

                case 16:
                    compararAlgoritmos();
                    break;

                case 0:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println();

        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("========================================");
        System.out.println("             GESTIÓN DE GRAFOS");
        System.out.println("========================================");
        System.out.println("1. Crear grafo dirigido");
        System.out.println("2. Crear grafo no dirigido");
        System.out.println("3. Generar grafo de ejemplo (imagen)");
        System.out.println("4. Agregar vértice");
        System.out.println("5. Actualizar vértice");
        System.out.println("6. Eliminar vértice");
        System.out.println("7. Agregar relación");
        System.out.println("8. Actualizar capacidad/peso de relación");
        System.out.println("9. Eliminar relación");
        System.out.println("10. Mostrar matriz de adyacencia");
        System.out.println("11. Mostrar lista de adyacencia");
        System.out.println("12. Mostrar matriz de incidencia");
        System.out.println("13. Mostrar lista de incidencia");
        System.out.println("14. Ejecutar Ford-Fulkerson");
        System.out.println("15. Ejecutar Edmonds-Karp");
        System.out.println("16. Comparar algoritmos de flujo máximo");
        System.out.println("0. Salir");
        System.out.println("========================================");

        System.out.println("Tipo de grafo activo: "
                + (grafo.esDirigido() ? "DIRIGIDO" : "NO DIRIGIDO"));
    }

    private static void crearGrafoDirigido() {
        grafo = new Grafo(true);
        System.out.println("Se creó un grafo dirigido vacío.");
    }

    private static void crearGrafoNoDirigido() {
        grafo = new Grafo(false);
        System.out.println("Se creó un grafo no dirigido vacío.");
    }

    /*
     * Grafo dirigido basado en la imagen.
     * S es el origen y T es el destino.
     */
    private static Grafo crearGrafoEjemplo() {
        Grafo nuevoGrafo = new Grafo(true);

        String[] vertices = {
            "S", "V1", "V2", "V3", "V4", "V5", "V6",
            "V7", "V8", "V9", "V10", "V11", "T"
        };

        for (String id : vertices) {
            nuevoGrafo.agregarVertice(
                    id,
                    id.equals("S") ? "Origen"
                    : id.equals("T") ? "Destino"
                    : id
            );
        }

        // Aristas desde el origen
        agregarArista(nuevoGrafo, "R1", "S", "V1", 17);
        agregarArista(nuevoGrafo, "R2", "S", "V2", 14);
        agregarArista(nuevoGrafo, "R3", "S", "V3", 14);
        agregarArista(nuevoGrafo, "R4", "S", "V4", 9);

        // Zona superior
        agregarArista(nuevoGrafo, "R5", "V1", "V2", 3);
        agregarArista(nuevoGrafo, "R6", "V1", "V5", 5);
        agregarArista(nuevoGrafo, "R7", "V1", "V6", 7);

        // Zona central superior
        agregarArista(nuevoGrafo, "R8", "V2", "V6", 4);
        agregarArista(nuevoGrafo, "R9", "V2", "V7", 5);
        agregarArista(nuevoGrafo, "R10", "V6", "V5", 10); 
        agregarArista(nuevoGrafo, "R11", "V5", "V10", 17); 
        agregarArista(nuevoGrafo, "R12", "V6", "V10", 11);

        // Zona central
        agregarArista(nuevoGrafo, "R13", "V3", "V7", 4);
        agregarArista(nuevoGrafo, "R14", "V3", "V8", 7);
        agregarArista(nuevoGrafo, "R15", "V3", "V4", 5);
        agregarArista(nuevoGrafo, "R16", "V7", "V8", 2);
        agregarArista(nuevoGrafo, "R17", "V7", "V11", 7);

        // Zona inferior
        agregarArista(nuevoGrafo, "R18", "V4", "V8", 7);
        agregarArista(nuevoGrafo, "R19", "V4", "V9", 8);
        agregarArista(nuevoGrafo, "R20", "V8", "V11", 15);
        agregarArista(nuevoGrafo, "R21", "V9", "V11", 9); 

        // Zona derecha y destino
        agregarArista(nuevoGrafo, "R22", "V11", "V10", 6); 
        agregarArista(nuevoGrafo, "R23", "V10", "T", 25);
        agregarArista(nuevoGrafo, "R24", "V11", "T", 29); 

        return nuevoGrafo;
    }

    private static void agregarArista(
            Grafo grafo,
            String id,
            String origen,
            String destino,
            double capacidad) {

        if (!grafo.agregarRelacion(
                id, origen, destino, capacidad)) {
            throw new IllegalStateException(
                    "No se pudo agregar la relación: "
                    + origen + " -> " + destino);
        }
    }

    private static void agregarVertice() {
        System.out.print("ID del vértice: ");
        String id = scanner.nextLine().trim();

        System.out.print("Dato o nombre: ");
        String dato = scanner.nextLine().trim();

        System.out.println(
                grafo.agregarVertice(id, dato)
                ? "Vértice agregado."
                : "No se pudo agregar el vértice.");
    }

    private static void actualizarVertice() {
        System.out.print("ID del vértice: ");
        String id = scanner.nextLine().trim();

        System.out.print("Nuevo dato: ");
        String dato = scanner.nextLine().trim();

        System.out.println(
                grafo.actualizarVertice(id, dato)
                ? "Vértice actualizado."
                : "No existe ese vértice.");
    }

    private static void eliminarVertice() {
        System.out.print("ID del vértice: ");
        String id = scanner.nextLine().trim();

        System.out.println(
                grafo.eliminarVertice(id)
                ? "Vértice eliminado."
                : "No existe ese vértice.");
    }

    private static void agregarRelacion() {
        System.out.print("ID de la relación: ");
        String id = scanner.nextLine().trim();

        System.out.print("Vértice de origen: ");
        String origen = scanner.nextLine().trim();

        System.out.print("Vértice de destino: ");
        String destino = scanner.nextLine().trim();

        double peso = leerDouble("Peso o capacidad: ");

        System.out.println(
                grafo.agregarRelacion(id, origen, destino, peso)
                ? "Relación agregada."
                : "No se pudo agregar la relación.");
    }

    private static void actualizarRelacion() {
        System.out.print("ID de la relación: ");
        String id = scanner.nextLine().trim();

        double peso = leerDouble("Nuevo peso/capacidad: ");

        System.out.println(
                grafo.actualizarRelacion(id, peso)
                ? "Relación actualizada."
                : "No se pudo actualizar la relación.");
    }

    private static void eliminarRelacion() {
        System.out.print("ID de la relación: ");
        String id = scanner.nextLine().trim();

        System.out.println(
                grafo.eliminarRelacion(id)
                ? "Relación eliminada."
                : "No existe esa relación.");
    }

    private static boolean validarGrafoFlujo() {
        if (!grafo.esDirigido()) {
            System.out.println(
                    "Error: el flujo máximo requiere un grafo dirigido.");
            System.out.println(
                    "Cree un grafo dirigido o genere el grafo de ejemplo.");
            return false;
        }

        return true;
    }

    private static String[] solicitarExtremos() {
        System.out.print("Origen [S]: ");
        String origen = scanner.nextLine().trim();

        System.out.print("Destino [T]: ");
        String destino = scanner.nextLine().trim();

        if (origen.isEmpty()) {
            origen = "S";
        }

        if (destino.isEmpty()) {
            destino = "T";
        }

        return new String[]{origen, destino};
    }

    private static void ejecutarFordFulkerson() {
        if (!validarGrafoFlujo()) {
            return;
        }

        try {
            String[] extremos = solicitarExtremos();

            ResultadoFlujo resultado = FordFulkerson.ejecutar(
                    grafo, extremos[0], extremos[1]);

            mostrarResultado("FORD-FULKERSON", resultado);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void ejecutarEdmondsKarp() {
        if (!validarGrafoFlujo()) {
            return;
        }

        try {
            String[] extremos = solicitarExtremos();

            ResultadoFlujo resultado = EdmondsKarp.ejecutar(
                    grafo, extremos[0], extremos[1]);

            mostrarResultado("EDMONDS-KARP", resultado);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void compararAlgoritmos() {
        if (!validarGrafoFlujo()) {
            return;
        }

        ComparadorFlujo.ejecutar(grafo, scanner);
    }

    private static void mostrarResultado(
            String titulo, ResultadoFlujo resultado) {

        System.out.println("\n=== " + titulo + " ===");

        System.out.printf(
                "Flujo máximo: %.4f%n",
                resultado.getFlujoMaximo());

        System.out.printf(
                "Tiempo: %.6f ms%n",
                resultado.getTiempoMilisegundos());

        System.out.println(
                "Iteraciones: " + resultado.getIteraciones());

        System.out.println(
                "Inspecciones de aristas: "
                + resultado.getInspeccionesAristas());
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(
                        scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Ingrese un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                double valor = Double.parseDouble(
                        scanner.nextLine().trim().replace(',', '.'));

                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException ignored) {
                // Se muestra el mensaje de error abajo.
            }

            System.out.println(
                    "Ingrese un número finito no negativo.");
        }
    }
}