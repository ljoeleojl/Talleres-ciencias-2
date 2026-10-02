
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = new Grafo();
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero(scanner,"Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    menuVertices(grafo,scanner);
                    break;
                case 2:
                    menuRelaciones(grafo,scanner);
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
                    System.out.println("\nPrograma finalizado.");
                    break;
                default:
                    System.out.println("\nOpcion no valida.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    private static void mostrarMenuPrincipal() {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("       REPRESENTACIONES DE GRAFOS");
        System.out.println( "        GRAFO NO DIRIGIDO Y PONDERADO");
        System.out.println("==============================================");
        System.out.println("1. Gestionar vertices");
        System.out.println( "2. Gestionar relaciones");
        System.out.println("3. Mostrar matriz de adyacencia");
        System.out.println("4. Mostrar lista de adyacencia");
        System.out.println("5. Mostrar matriz de incidencia");
        System.out.println("6. Mostrar lista de incidencia");
        System.out.println("7. Mostrar grafo completo");
        System.out.println("0. Salir");
        System.out.println("==============================================");
    }

    private static void menuVertices(Grafo grafo,Scanner scanner) {
        int opcion;
        do {
            System.out.println();
            System.out.println("======================================");
            System.out.println("          GESTION DE VERTICES");
            System.out.println( "======================================");
            System.out.println( "1. Agregar vertice");
            System.out.println("2. Eliminar vertice");
            System.out.println( "3. Actualizar vertice");
            System.out.println("4. Mostrar vertices");
            System.out.println("0. Volver");
            System.out.println("======================================");
            opcion = leerEntero( scanner,"Seleccione una opcion: "
            );
            switch (opcion) {
                case 1:
                    System.out.print("ID del vertice: ");
                    String id = scanner.nextLine();
                    System.out.print( "Dato del vertice: " );
                    String dato = scanner.nextLine();
                    if (grafo.agregarVertice(id,dato)) {
                        System.out.println("Vertice agregado correctamente.");
                    } else {
                        System.out.println("Ya existe un vertice con ese ID.");
                    }
                    break;
                case 2:
                    System.out.print( "ID del vertice a eliminar: ");
                    id = scanner.nextLine();
                    if (grafo.eliminarVertice(id)) {
                        System.out.println("Vertice eliminado correctamente.");
                    } else {
                        System.out.println("El vertice no existe.");
                    }
                    break;
                case 3:
                    System.out.print("ID del vertice a actualizar: ");
                    id = scanner.nextLine();
                    System.out.print("Nuevo dato: ");
                    dato = scanner.nextLine();
                    if (grafo.actualizarVertice(id,dato)) {
                        System.out.println("Vertice actualizado correctamente.");
                    } else {
                        System.out.println("El vertice no existe.");
                    }
                    break;
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
    private static void menuRelaciones(Grafo grafo,Scanner scanner) {
        int opcion;
        do {
            System.out.println();
            System.out.println("======================================");
            System.out.println("        GESTION DE RELACIONES");
            System.out.println("======================================");
            System.out.println("1. Agregar relacion");
            System.out.println("2. Eliminar relacion");
            System.out.println("3. Actualizar relacion");
            System.out.println("4. Mostrar relaciones");
            System.out.println("0. Volver");
            System.out.println("======================================");
            opcion = leerEntero(scanner,"Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    System.out.print("ID de la relacion: ");
                    String id = scanner.nextLine();
                    System.out.print( "Primer vertice: ");
                    String vertice1 = scanner.nextLine();
                    System.out.print("Segundo vertice: ");
                    String vertice2 = scanner.nextLine();
                    double peso = leerDouble(scanner,"Peso de la relacion: ");
                    if (grafo.agregarRelacion(id,vertice1,vertice2,peso)) {
                        System.out.println("Relacion agregada correctamente.");
                    } else {
                        System.out.println("No se pudo agregar la relacion.");
                        System.out.println("Verifique que los vertices "+ "existan y que no exista "+ "ya esa relacion.");
                    }
                    break;
                case 2:
                    System.out.print("ID de la relacion a eliminar: ");
                    id  = scanner.nextLine();
                    if (grafo.eliminarRelacion(id)) {
                        System.out.println("Relacion eliminada correctamente.");
                    } else {
                        System.out.println("La relacion no existe.");
                    }
                    break;
                case 3:
                    System.out.print("ID de la relacion a actualizar: ");
                    id = scanner.nextLine();
                    peso = leerDouble(scanner,"Nuevo peso: ");
                    if (grafo.actualizarRelacion(id,peso)) {
                        System.out.println("Relacion actualizada correctamente.");
                    } else {
                        System.out.println("La relacion no existe.");
                    }
                    break;
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

    private static int leerEntero(Scanner scanner,String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static double leerDouble(Scanner scanner,String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }
}
