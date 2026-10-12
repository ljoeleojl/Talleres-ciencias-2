
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("       REPRESENTACIONES DE GRAFOS");
        System.out.println("======================================");
        System.out.println("1. Grafo dirigido y ponderado");
        System.out.println("2. Grafo no dirigido y ponderado");

        int tipo;

        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de grafo: ");

            if (tipo != 1 && tipo != 2) {
                System.out.println("Seleccione 1 o 2.");
            }
        } while (tipo != 1 && tipo != 2);

        Grafo grafo = new Grafo(tipo == 1);

        int opcion;

        do {
            mostrarMenuPrincipal(grafo);

            opcion = leerEntero(scanner, "Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    menuVertices(grafo, scanner);
                    break;

                case 2:
                    menuRelaciones(grafo, scanner);
                    break;

                case 3:
                    grafo.mostrarMatrizAdyacencia();
                    break;

                case 4:
                    grafo.mostrarListaAdyacencia();
                    break;

                case 5:
                    grafo.mostrarMatrizIncidencia();
                    break;

                case 6:
                    grafo.mostrarListaIncidencia();
                    break;

                case 7:
                    grafo.mostrarVertices();
                    grafo.mostrarRelaciones();
                    break;

                case 0:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenuPrincipal(Grafo grafo) {
        System.out.println("\n======================================");
        System.out.println("       REPRESENTACIONES DE GRAFOS");

        if (grafo.esDirigido()) {
            System.out.println("       GRAFO DIRIGIDO Y PONDERADO");
        } else {
            System.out.println("     GRAFO NO DIRIGIDO Y PONDERADO");
        }

        System.out.println("======================================");
        System.out.println("1. Gestionar vertices");
        System.out.println("2. Gestionar relaciones");
        System.out.println("3. Mostrar matriz de adyacencia");
        System.out.println("4. Mostrar lista de adyacencia");
        System.out.println("5. Mostrar matriz de incidencia");
        System.out.println("6. Mostrar lista de incidencia");
        System.out.println("7. Mostrar grafo completo");
        System.out.println("0. Salir");
        System.out.println("======================================");
    }

    private static void menuVertices(Grafo grafo, Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n========== GESTION DE VERTICES ==========");
            System.out.println("1. Agregar vertice");
            System.out.println("2. Eliminar vertice");
            System.out.println("3. Actualizar vertice");
            System.out.println("4. Mostrar vertices");
            System.out.println("0. Volver");

            opcion = leerEntero(scanner, "Seleccione una opcion: ");

            switch (opcion) {
                case 1: {
                    System.out.print("ID del vertice: ");
                    String id = scanner.nextLine();

                    System.out.print("Dato del vertice: ");
                    String dato = scanner.nextLine();

                    if (grafo.agregarVertice(id, dato)) {
                        System.out.println("Vertice agregado correctamente.");
                    } else {
                        System.out.println(
                                "No se pudo agregar. "
                                + "El ID esta vacio o ya existe."
                        );
                    }

                    break;
                }

                case 2: {
                    System.out.print("ID del vertice a eliminar: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarVertice(id)) {
                        System.out.println("Vertice eliminado correctamente.");
                    } else {
                        System.out.println("El vertice no existe.");
                    }

                    break;
                }

                case 3: {
                    System.out.print("ID del vertice a actualizar: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo dato: ");
                    String dato = scanner.nextLine();

                    if (grafo.actualizarVertice(id, dato)) {
                        System.out.println("Vertice actualizado correctamente.");
                    } else {
                        System.out.println("El vertice no existe.");
                    }

                    break;
                }

                case 4:
                    grafo.mostrarVertices();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void menuRelaciones(Grafo grafo, Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n========== GESTION DE RELACIONES ==========");
            System.out.println("1. Agregar relacion");
            System.out.println("2. Eliminar relacion");
            System.out.println("3. Actualizar relacion");
            System.out.println("4. Mostrar relaciones");
            System.out.println("0. Volver");

            opcion = leerEntero(scanner, "Seleccione una opcion: ");

            switch (opcion) {
                case 1: {
                    System.out.print("ID de la relacion: ");
                    String id = scanner.nextLine();

                    System.out.print("Vertice de origen: ");
                    String vertice1 = scanner.nextLine();

                    System.out.print("Vertice de destino: ");
                    String vertice2 = scanner.nextLine();

                    double peso = leerDouble(
                            scanner, "Peso de la relacion: "
                    );

                    if (grafo.agregarRelacion(
                            id, vertice1, vertice2, peso)) {
                        System.out.println("Relacion agregada correctamente.");
                    } else {
                        System.out.println(
                                "No se pudo agregar la relacion. "
                                + "Verifique los ID, los vertices "
                                + "y que no exista una relacion duplicada."
                        );
                    }

                    break;
                }

                case 2: {
                    System.out.print("ID de la relacion a eliminar: ");
                    String id = scanner.nextLine();

                    if (grafo.eliminarRelacion(id)) {
                        System.out.println("Relacion eliminada correctamente.");
                    } else {
                        System.out.println("La relacion no existe.");
                    }

                    break;
                }

                case 3: {
                    System.out.print("ID de la relacion a actualizar: ");
                    String id = scanner.nextLine();

                    double peso = leerDouble(scanner, "Nuevo peso: ");

                    if (grafo.actualizarRelacion(id, peso)) {
                        System.out.println("Relacion actualizada correctamente.");
                    } else {
                        System.out.println("La relacion no existe.");
                    }

                    break;
                }

                case 4:
                    grafo.mostrarRelaciones();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static double leerDouble(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double numero = Double.parseDouble(scanner.nextLine());

                if (Double.isNaN(numero) || Double.isInfinite(numero)) {
                    System.out.println("Ingrese un numero finito.");
                    continue;
                }

                return numero;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }
}
