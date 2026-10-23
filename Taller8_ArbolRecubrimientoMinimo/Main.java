import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Grafo grafo;

        System.out.println("================================");
        System.out.println("       SISTEMA DE GRAFOS");
        System.out.println("================================");
        System.out.println("1. Grafo dirigido");
        System.out.println("2. Grafo no dirigido");
        System.out.print("Seleccione el tipo de grafo: ");

        int tipoGrafo = leerEntero(scanner);

        while (tipoGrafo != 1 && tipoGrafo != 2) {
            System.out.println("Opcion no valida.");
            System.out.print("Seleccione 1 o 2: ");

            tipoGrafo = leerEntero(scanner);
        }

        if (tipoGrafo == 1) {
            grafo = new Grafo(true);
            System.out.println("Se creo un grafo dirigido.");
        } else {
            grafo = new Grafo(false);
            System.out.println("Se creo un grafo no dirigido.");
        }

        int opcion;

        do {
            System.out.println("\n================================");
            System.out.println("             MENU");
            System.out.println("================================");
            System.out.println("1. Generar grafo de ejemplo");
            System.out.println("2. Agregar vertice");
            System.out.println("3. Actualizar vertice");
            System.out.println("4. Eliminar vertice");
            System.out.println("5. Agregar relacion");
            System.out.println("6. Actualizar peso de relacion");
            System.out.println("7. Eliminar relacion");
            System.out.println("8. Mostrar matriz de adyacencia");
            System.out.println("9. Mostrar lista de adyacencia");
            System.out.println("10. Mostrar matriz de incidencia");
            System.out.println("11. Mostrar lista de incidencia");
            System.out.println("12. Algoritmo de Prim");
            System.out.println("13. Algoritmo de Kruskal");
            System.out.println("14. Comparar Prim y Kruskal");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero(scanner);

            switch (opcion) {

                case 1: {
                    if (!grafo.getVertices().isEmpty()) {
                        System.out.print(
                                "El grafo actual se reemplazara "
                                + "por el de ejemplo. Continuar? (s/n): "
                        );

                        String respuesta = scanner.nextLine().trim();

                        if (!respuesta.equalsIgnoreCase("s")) {
                            System.out.println("Operacion cancelada.");
                            break;
                        }
                    }

                    grafo = crearGrafoEjemplo(grafo.esDirigido());

                    System.out.println(
                            "Grafo de ejemplo generado: "
                            + "6 vertices (A-F) y 9 relaciones (r1-r9)."
                    );

                    break;
                }

                case 2: {
                    System.out.print("Ingrese el ID del vertice: ");
                    String id = scanner.nextLine();

                    System.out.print("Ingrese el dato del vertice: ");
                    String dato = scanner.nextLine();

                    if (grafo.agregarVertice(id, dato)) {
                        System.out.println(
                                "Vertice agregado correctamente."
                        );
                    } else {
                        System.out.println(
                                "No se pudo agregar el vertice. "
                                + "Verifique que el ID no este vacio "
                                + "y no exista previamente."
                        );
                    }

                    break;
                }

                case 3: {
                    System.out.print("Ingrese el ID del vertice: ");
                    String id = scanner.nextLine();

                    System.out.print("Ingrese el nuevo dato: ");
                    String nuevoDato = scanner.nextLine();

                    if (grafo.actualizarVertice(id, nuevoDato)) {
                        System.out.println(
                                "Vertice actualizado correctamente."
                        );
                    } else {
                        System.out.println(
                                "No se pudo actualizar el vertice."
                        );
                    }

                    break;
                }

                case 4: {
                    System.out.print("Ingrese el ID del vertice: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarVertice(id)) {
                        System.out.println(
                                "Vertice eliminado. "
                                + "Sus relaciones tambien se eliminaron."
                        );
                    } else {
                        System.out.println("El vertice no existe.");
                    }

                    break;
                }

                case 5: {
                    System.out.print("Ingrese el ID de la relacion: ");
                    String id = scanner.nextLine();

                    System.out.print("Ingrese el ID del primer vertice: ");
                    String vertice1 = scanner.nextLine();

                    System.out.print("Ingrese el ID del segundo vertice: ");
                    String vertice2 = scanner.nextLine();

                    System.out.print("Ingrese el peso de la relacion: ");
                    double peso = leerDouble(scanner);

                    if (grafo.agregarRelacion(
                            id, vertice1, vertice2, peso)) {

                        System.out.println(
                                "Relacion agregada correctamente."
                        );

                    } else {
                        System.out.println(
                                "No se pudo agregar la relacion."
                        );

                        System.out.println(
                                "Verifique que el ID no exista, "
                                + "que ambos vertices existan "
                                + "y que no haya otra relacion entre "
                                + "el mismo par de vertices."
                        );

                        if (!grafo.esDirigido()) {
                            System.out.println(
                                    "Recuerde que en un grafo no "
                                    + "dirigido no se aceptan "
                                    + "pesos negativos."
                            );
                        }
                    }

                    break;
                }

                case 6: {
                    System.out.print("Ingrese el ID de la relacion: ");
                    String id = scanner.nextLine();

                    System.out.print("Ingrese el nuevo peso: ");
                    double nuevoPeso = leerDouble(scanner);

                    if (grafo.actualizarRelacion(id, nuevoPeso)) {
                        System.out.println(
                                "Peso actualizado correctamente."
                        );
                    } else {
                        System.out.println(
                                "No se pudo actualizar. "
                                + "Compruebe que la relacion exista."
                        );

                        if (!grafo.esDirigido()) {
                            System.out.println(
                                    "Recuerde que en un grafo no "
                                    + "dirigido no se aceptan "
                                    + "pesos negativos."
                            );
                        }
                    }

                    break;
                }

                case 7: {
                    System.out.print("Ingrese el ID de la relacion: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarRelacion(id)) {
                        System.out.println(
                                "Relacion eliminada correctamente."
                        );
                    } else {
                        System.out.println("La relacion no existe.");
                    }

                    break;
                }

                case 8: {
                    grafo.mostrarMatrizAdyacencia();
                    break;
                }

                case 9: {
                    grafo.mostrarListaAdyacencia();
                    break;
                }

                case 10: {
                    grafo.mostrarMatrizIncidencia();
                    break;
                }

                case 11: {
                    grafo.mostrarListaIncidencia();
                    break;
                }

                case 12: {
                    Prim.ejecutar(grafo);
                    break;
                }

                case 13: {
                    Kruskal.ejecutar(grafo);
                    break;
                }

                case 14: {
                    ComparadorMST.ejecutar();
                    break;
                }

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

    // =========================
    // CREAR GRAFO DE EJEMPLO
    // 6 vertices y 9 relaciones, todas con peso positivo.
    // Funciona igual en grafos dirigidos y no dirigidos.
    // =========================

    private static Grafo crearGrafoEjemplo(boolean dirigido) {
        Grafo grafo = new Grafo(dirigido);

        grafo.agregarVertice("A", "Nodo A");
        grafo.agregarVertice("B", "Nodo B");
        grafo.agregarVertice("C", "Nodo C");
        grafo.agregarVertice("D", "Nodo D");
        grafo.agregarVertice("E", "Nodo E");
        grafo.agregarVertice("F", "Nodo F");

        grafo.agregarRelacion("r1", "A", "B", 4);
        grafo.agregarRelacion("r2", "A", "C", 2);
        grafo.agregarRelacion("r3", "B", "C", 1);
        grafo.agregarRelacion("r4", "B", "D", 5);
        grafo.agregarRelacion("r5", "C", "D", 8);
        grafo.agregarRelacion("r6", "C", "E", 10);
        grafo.agregarRelacion("r7", "D", "E", 2);
        grafo.agregarRelacion("r8", "D", "F", 6);
        grafo.agregarRelacion("r9", "E", "F", 3);

        return grafo;
    }

    // =========================
    // LEER ENTERO
    // =========================

    private static int leerEntero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un numero entero.");
            scanner.nextLine();
            System.out.print("Intente nuevamente: ");
        }

        int numero = scanner.nextInt();
        scanner.nextLine();

        return numero;
    }

    // =========================
    // LEER NUMERO DECIMAL
    // =========================

    private static double leerDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Debe ingresar un numero valido.");
            scanner.nextLine();
            System.out.print("Intente nuevamente: ");
        }

        double numero = scanner.nextDouble();
        scanner.nextLine();

        return numero;
    }
}