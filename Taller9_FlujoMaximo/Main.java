
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================");
        System.out.println("          SISTEMA DE GRAFOS");
        System.out.println("==================================");

        System.out.println("1. Grafo dirigido");
        System.out.println("2. Grafo no dirigido");

        System.out.print("Seleccione el tipo de grafo: ");

        int tipo = leerEntero(scanner);

        while (tipo != 1 && tipo != 2) {

            System.out.println("Opcion no valida.");

            System.out.print("Seleccione 1 o 2: ");

            tipo = leerEntero(scanner);
        }

        Grafo grafo = new Grafo(tipo == 1);

        int opcion;

        do {

            System.out.println("\n==================================");
            System.out.println("               MENU");
            System.out.println("==================================");

            System.out.println("1. Generar grafo de ejemplo");

            System.out.println("2. Agregar vertice");
            System.out.println("3. Actualizar vertice");
            System.out.println("4. Eliminar vertice");

            System.out.println("5. Agregar relacion");
            System.out.println("6. Actualizar relacion");
            System.out.println("7. Eliminar relacion");

            System.out.println("----------------------------------");

            System.out.println("8. Mostrar vertices");
            System.out.println("9. Mostrar relaciones");

            System.out.println("10. Matriz de adyacencia");
            System.out.println("11. Lista de adyacencia");

            System.out.println("12. Matriz de incidencia");
            System.out.println("13. Lista de incidencia");

            System.out.println("----------------------------------");

            System.out.println("14. Ford-Fulkerson");
            System.out.println("15. Edmonds-Karp");

            System.out.println("0. Salir");

            System.out.println("==================================");

            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero(scanner);

            switch (opcion) {

                // ==================================
                // GENERAR GRAFO
                // ==================================

                case 1: {

                    if (!grafo.getVertices().isEmpty()) {

                        System.out.print(
                                "Se reemplazara el grafo actual. "
                                + "Continuar? (s/n): "
                        );

                        String respuesta = scanner.nextLine();

                        if (!respuesta.equalsIgnoreCase("s")) {
                            break;
                        }
                    }

                    grafo = crearGrafoEjemplo();

                    System.out.println(
                            "\nGrafo de ejemplo generado."
                    );

                    grafo.mostrarListaAdyacencia();

                    break;
                }

                // ==================================
                // AGREGAR VERTICE
                // ==================================

                case 2: {

                    System.out.print("ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Dato: ");
                    String dato = scanner.nextLine();

                    if (grafo.agregarVertice(id, dato)) {
                        System.out.println("Vertice agregado.");
                    } else {
                        System.out.println("No se pudo agregar.");
                    }

                    break;
                }

                // ==================================
                // ACTUALIZAR VERTICE
                // ==================================

                case 3: {

                    System.out.print("ID del vertice: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo dato: ");
                    String dato = scanner.nextLine();

                    if (grafo.actualizarVertice(id, dato)) {
                        System.out.println("Vertice actualizado.");
                    } else {
                        System.out.println("No se pudo actualizar.");
                    }

                    break;
                }

                // ==================================
                // ELIMINAR VERTICE
                // ==================================

                case 4: {

                    System.out.print("ID del vertice: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarVertice(id)) {
                        System.out.println("Vertice eliminado.");
                    } else {
                        System.out.println("Vertice inexistente.");
                    }

                    break;
                }

                // ==================================
                // AGREGAR RELACION
                // ==================================

                case 5: {

                    System.out.print("ID de la relacion: ");
                    String id = scanner.nextLine();

                    System.out.print("Vertice origen: ");
                    String origen = scanner.nextLine();

                    System.out.print("Vertice destino: ");
                    String destino = scanner.nextLine();

                    System.out.print("Peso/capacidad: ");
                    double peso = leerDouble(scanner);

                    if (grafo.agregarRelacion(
                            id, origen, destino, peso)) {

                        System.out.println("Relacion agregada.");

                    } else {

                        System.out.println(
                                "No se pudo agregar la relacion."
                        );
                    }

                    break;
                }

                // ==================================
                // ACTUALIZAR RELACION
                // ==================================

                case 6: {

                    System.out.print("ID de la relacion: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo peso/capacidad: ");
                    double peso = leerDouble(scanner);

                    if (grafo.actualizarRelacion(id, peso)) {
                        System.out.println("Relacion actualizada.");
                    } else {
                        System.out.println("No se pudo actualizar.");
                    }

                    break;
                }

                // ==================================
                // ELIMINAR RELACION
                // ==================================

                case 7: {

                    System.out.print("ID de la relacion: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarRelacion(id)) {
                        System.out.println("Relacion eliminada.");
                    } else {
                        System.out.println("Relacion inexistente.");
                    }

                    break;
                }

                // ==================================
                // MOSTRAR VERTICES
                // ==================================

                case 8: {

                    grafo.mostrarVertices();

                    break;
                }

                // ==================================
                // MOSTRAR RELACIONES
                // ==================================

                case 9: {

                    grafo.mostrarRelaciones();

                    break;
                }

                // ==================================
                // MATRIZ DE ADYACENCIA
                // ==================================

                case 10: {

                    grafo.mostrarMatrizAdyacencia();

                    break;
                }

                // ==================================
                // LISTA DE ADYACENCIA
                // ==================================

                case 11: {

                    grafo.mostrarListaAdyacencia();

                    break;
                }

                // ==================================
                // MATRIZ DE INCIDENCIA
                // ==================================

                case 12: {

                    grafo.mostrarMatrizIncidencia();

                    break;
                }

                // ==================================
                // LISTA DE INCIDENCIA
                // ==================================

                case 13: {

                    grafo.mostrarListaIncidencia();

                    break;
                }

                // ==================================
                // FORD-FULKERSON
                // ==================================

                case 14: {

                    ejecutarFordFulkerson(scanner, grafo);

                    break;
                }

                // ==================================
                // EDMONDS-KARP
                // ==================================

                case 15: {

                    ejecutarEdmondsKarp(scanner, grafo);

                    break;
                }

                // ==================================
                // SALIR
                // ==================================

                case 0: {

                    System.out.println("Programa finalizado.");

                    break;
                }

                default: {

                    System.out.println("Opcion no valida.");

                    break;
                }
            }

        } while (opcion != 0);

        scanner.close();
    }

    // ==========================================
    // EJECUTAR FORD-FULKERSON
    // ==========================================

    private static void ejecutarFordFulkerson(
            Scanner scanner,
            Grafo grafo) {

        System.out.println("\n--- FORD-FULKERSON ---");

        try {

            System.out.print("Vertice origen: ");
            String origen = scanner.nextLine();

            System.out.print("Vertice destino: ");
            String destino = scanner.nextLine();

            ResultadoFlujo resultado =
                    FordFulkerson.ejecutar(
                            grafo,
                            origen,
                            destino
                    );

            resultado.mostrarResultado();

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // EJECUTAR EDMONDS-KARP
    // ==========================================

    private static void ejecutarEdmondsKarp(
            Scanner scanner,
            Grafo grafo) {

        System.out.println("\n--- EDMONDS-KARP ---");

        try {

            System.out.print("Vertice origen: ");
            String origen = scanner.nextLine();

            System.out.print("Vertice destino: ");
            String destino = scanner.nextLine();

            ResultadoFlujo resultado =
                    EdmondsKarp.ejecutar(
                            grafo,
                            origen,
                            destino
                    );

            resultado.mostrarResultado();

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // CREAR GRAFO DEL EJERCICIO
    // ==========================================

    private static Grafo crearGrafoEjemplo() {

        Grafo grafo = new Grafo(true);

        // VERTICES

        grafo.agregarVertice("S", "Origen");

        for (int i = 1; i <= 11; i++) {

            grafo.agregarVertice(
                    "v" + i,
                    "Vertice " + i
            );
        }

        grafo.agregarVertice("T", "Destino");

        // RELACIONES DESDE S

        grafo.agregarRelacion("r1", "S", "v1", 17);
        grafo.agregarRelacion("r2", "S", "v2", 14);
        grafo.agregarRelacion("r3", "S", "v3", 14);
        grafo.agregarRelacion("r4", "S", "v4", 9);

        // RELACIONES DESDE v1

        grafo.agregarRelacion("r5", "v1", "v5", 5);
        grafo.agregarRelacion("r6", "v1", "v2", 3);
        grafo.agregarRelacion("r7", "v1", "v6", 7);

        // RELACIONES DESDE v2

        grafo.agregarRelacion("r8", "v2", "v6", 4);
        grafo.agregarRelacion("r9", "v2", "v7", 5);

        // RELACIONES DESDE v5 Y v6

        grafo.agregarRelacion("r10", "v5", "v6", 10);
        grafo.agregarRelacion("r11", "v5", "v10", 17);
        grafo.agregarRelacion("r12", "v6", "v10", 11);

        // RELACIONES DESDE v3

        grafo.agregarRelacion("r13", "v3", "v7", 4);
        grafo.agregarRelacion("r14", "v3", "v8", 7);
        grafo.agregarRelacion("r15", "v3", "v4", 5);

        // RELACIONES DESDE v7

        grafo.agregarRelacion("r16", "v7", "v8", 2);
        grafo.agregarRelacion("r17", "v7", "v11", 7);

        // RELACIONES DESDE v8

        grafo.agregarRelacion("r18", "v8", "v11", 15);

        // RELACIONES DESDE v4

        grafo.agregarRelacion("r19", "v4", "v8", 7);
        grafo.agregarRelacion("r20", "v4", "v9", 8);

        // RELACIONES DESDE v9

        grafo.agregarRelacion("r21", "v9", "v11", 9);

        // RELACIONES DESDE v11

        grafo.agregarRelacion("r22", "v11", "v10", 6);
        grafo.agregarRelacion("r23", "v10", "T", 25);
        grafo.agregarRelacion("r24", "v11", "T", 24);

        return grafo;
    }

    // ==========================================
    // LEER ENTERO
    // ==========================================

    private static int leerEntero(Scanner scanner) {

        while (!scanner.hasNextInt()) {

            System.out.println("Ingrese un numero entero.");

            scanner.nextLine();

            System.out.print("Intente nuevamente: ");
        }

        int numero = scanner.nextInt();

        scanner.nextLine();

        return numero;
    }

    // ==========================================
    // LEER DOUBLE
    // ==========================================

    private static double leerDouble(Scanner scanner) {

        while (!scanner.hasNextDouble()) {

            System.out.println("Ingrese un numero valido.");

            scanner.nextLine();

            System.out.print("Intente nuevamente: ");
        }

        double numero = scanner.nextDouble();

        scanner.nextLine();

        return numero;
    }
}
