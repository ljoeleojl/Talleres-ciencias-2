import java.util.Locale;

public class Xorshift {

	long estado;

	public Xorshift(long semilla) {
		this.estado = semilla & 0xFFFFFFFFL;
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

	public double siguienteUniforme() {
		long entero = siguienteEntero();

		return entero / 4294967296.0; // 2^32
	}

	public double[] generarNumeros(int cantidad, double a, double b) {
		double[] resultados = new double[cantidad];

		for (int i = 0; i < cantidad; i++) {
			double u = siguienteUniforme();

			resultados[i] = a + u * (b - a);
		}

		return resultados;
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

	public static void main(String[] args) {

		// Semilla utilizada
		long semilla = 150;

		// Cantidad de numeros para la prueba
		int cantidad = 100000;

		// Espacio de valores
		int min = 1;
		int max = 20;

		// Crear el generador
		Xorshift generador = new Xorshift(semilla);

		// Generar los numeros entre 1 y 20
		int[] numeros =
				generador.generarEnteros(cantidad, min, max);

		// Arreglo para contar las veces que aparece cada numero
		int[] frecuencias = new int[20];

		// Contar las frecuencias
		for (int i = 0; i < numeros.length; i++) {

			int numero = numeros[i];

			frecuencias[numero - 1]++;
		}

		// Calcular la frecuencia esperada
		double frecuenciaEsperada =
				(double) cantidad / 20;

		System.out.println("======================================");
		System.out.println("       GENERADOR XORSHIFT");
		System.out.println("======================================");

		System.out.println("Semilla: " + semilla);
		System.out.println("Cantidad de numeros: " + cantidad);
		System.out.println("Espacio de valores: 1 - 20");

		System.out.println();

		System.out.println(
				"Frecuencia esperada: " +
				frecuenciaEsperada
		);

		System.out.println();

		System.out.println(
				"Valor\tFrecuencia\tDiferencia"
		);

		System.out.println(
				"--------------------------------------"
		);

		// Mostrar resultados
		for (int i = 0; i < frecuencias.length; i++) {

			int frecuencia = frecuencias[i];

			double diferencia =
					frecuencia - frecuenciaEsperada;

			System.out.printf(
					Locale.US,
					"%2d\t%10d\t%10.0f%n",
					i + 1,
					frecuencia,
					diferencia
			);
		}

		System.out.println();
		System.out.println("======================================");
		System.out.println("          RESULTADO DE LA PRUEBA");
		System.out.println("======================================");

		System.out.println(
				"Cada valor deberia aparecer aproximadamente " +
				(int) frecuenciaEsperada + " veces."
		);

		
	}
}