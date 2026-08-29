import java.util.Scanner;

public class GeneradorSimple {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        System.out.print("Ingresa la semilla: ");
        int semilla = teclado.nextInt();
        
        // Arreglo para contar cuántas veces sale cada número del 0 al 19
        int[] frecuencias = new int[20];
        
        System.out.println("\nPrimeros 10 números generados:");
        
        // Generamos 100000 números para evaluar estadísticamente su distribución
        for (int i = 0; i < 100000; i++) {
            int mult = (int) Math.cbrt(semilla);
            int incremento = (int) Math.pow(semilla, mult);
            
            // Tu ecuación con módulo 20
            semilla = (mult * semilla + incremento) % 20;
            
            // Registrar el número en nuestro contador de frecuencias
            frecuencias[semilla]++;
            
            // Imprimir solo los primeros 10 para ver la secuencia inicial
            if (i < 10) {
                System.out.print(semilla + " ");
            }
        }
        
        // VERIFICACIÓN DE DISTRIBUCIÓN
        System.out.println("\n\n=== VERIFICACIÓN DE DISTRIBUCIÓN ===");
        
        System.out.println("En un algoritmo perfecto, cada número debería salir unas 5000 veces.");
        
        for (int numero = 0; numero < 20; numero++) {
            System.out.println("Número " + numero + ": salió " + frecuencias[numero] + " veces");
        }
        
        teclado.close();
    }
}