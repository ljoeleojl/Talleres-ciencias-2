import java.util.Locale;

public class CifradoTextoRGB {

	long estado;

	public CifradoTextoRGB(long semilla) {
		this.estado = semilla & 0xFFFFFFFFL;
		// Proteccion contra estado 0: xorshift queda "atascado" en 0 para
		// siempre si el estado llega a 0 (0 XOR cualquier shift de 0 = 0).
		if (this.estado == 0) {
			this.estado = 0xA5A5A5A5L;
		}
	}

	// Convierte un texto en una semilla numerica de 32 bits bien "mezclada",
	// usando XOR + rotacion de bits + multiplicacion por una constante grande.
	// Esto asegura que un cambio minimo en el texto (un solo caracter)
	// produzca una semilla completamente distinta (efecto avalancha).
	public static long textoASemilla(String texto) {
		long semilla = 0xA5A5A5A5L; // valor inicial no-cero
		for (int i = 0; i < texto.length(); i++) {
			char c = texto.charAt(i);
			semilla ^= (long) c;
			semilla = ((semilla << 13) | (semilla >>> 19)) & 0xFFFFFFFFL;
			semilla = (semilla * 2654435761L) & 0xFFFFFFFFL;
		}
		if (semilla == 0) {
			semilla = 0xA5A5A5A5L;
		}
		return semilla;
	}

	private long siguienteEntero() {
		long x = estado;
		x ^= (x << 13) & 0xFFFFFFFFL;
		x ^= (x >>> 17);
		x ^= (x << 5) & 0xFFFFFFFFL;
		x &= 0xFFFFFFFFL;
		estado = x;
		return x;
	}

	// "Calienta" el generador descartando las primeras iteraciones,
	// para mejorar la calidad de difusion antes de generar los colores.
	public void calentar(int rondas) {
		for (int i = 0; i < rondas; i++) {
			siguienteEntero();
		}
	}

	public double siguienteUniforme() {
		long entero = siguienteEntero();
		return entero / 4294967296.0; // 2^32
	}

	public int[] generarEnteros(int cantidad, int min, int max) {
		int[] resultados = new int[cantidad];
		int amplitud = max - min + 1;
		for (int i = 0; i < cantidad; i++) {
			double u = siguienteUniforme();
			resultados[i] =
					min + (int) Math.floor(u * amplitud);
		}
		return resultados;
	}

	// Cifra un texto como una matriz lado x lado de colores RGB (0-255).
	public static int[][][] cifrarComoMatrizRGB(String texto, int lado, int rondasCalentamiento) {
		long semilla = textoASemilla(texto);
		CifradoTextoRGB generador = new CifradoTextoRGB(semilla);
		generador.calentar(rondasCalentamiento);

		int[][][] matriz = new int[lado][lado][3];
		for (int fila = 0; fila < lado; fila++) {
			for (int col = 0; col < lado; col++) {
				int[] rgb = generador.generarEnteros(3, 0, 255);
				matriz[fila][col][0] = rgb[0]; // R
				matriz[fila][col][1] = rgb[1]; // G
				matriz[fila][col][2] = rgb[2]; // B
			}
		}
		return matriz;
	}

	// Imprime la matriz resultante en formato (R,G,B) por celda.
	public static void imprimirMatrizRGB(int[][][] matriz) {
		for (int fila = 0; fila < matriz.length; fila++) {
			for (int col = 0; col < matriz[fila].length; col++) {
				int[] px = matriz[fila][col];
				System.out.printf(
						Locale.US,
						"(%3d,%3d,%3d) ",
						px[0], px[1], px[2]
				);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {

		String texto = "ciencias de la computacion 2";
		int lado = 4; // matriz 4x4 -> 16 pixeles -> 48 valores RGB
		int rondasCalentamiento = 10;

		int[][][] matriz = cifrarComoMatrizRGB(texto, lado, rondasCalentamiento);

		System.out.println("======================================");
		System.out.println("      CIFRADO DE TEXTO A MATRIZ RGB");
		System.out.println("======================================");
		System.out.println("Texto de entrada: \"" + texto + "\"");
		System.out.println("Semilla generada: " + textoASemilla(texto));
		System.out.println();

		imprimirMatrizRGB(matriz);
	}
}