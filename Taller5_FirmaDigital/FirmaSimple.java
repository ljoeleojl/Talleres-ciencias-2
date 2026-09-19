import java.math.BigInteger;

public class FirmaSimple {

	// Clave publica: (modulo, clavePublica)   Clave privada: (modulo, clavePrivada)
	static BigInteger modulo;
	static BigInteger clavePublica;
	static BigInteger clavePrivada;

	// Arma un numero de 256 bits con 32 valores (0-255) de  xorshift,
	// y busca el primer primo que haya a partir de ahi.
	static BigInteger generarPrimo(CifradoTextoRGB generador) {
		int[] valores = generador.generarEnteros(32, 0, 255);
		BigInteger candidato = BigInteger.ZERO;
		for (int valor : valores) {
			candidato = candidato.multiply(BigInteger.valueOf(256)).add(BigInteger.valueOf(valor));
		}
		candidato = candidato.setBit(255); // asegura que el numero use los 256 bits
		return candidato.nextProbablePrime();
	}

	static void generarClaves(long semilla) {
		CifradoTextoRGB generador = new CifradoTextoRGB(semilla);
		generador.calentar(10);

		clavePublica = BigInteger.valueOf(65537);
		BigInteger primo1, primo2, numeroDeTrabajo;
		do {
			primo1 = generarPrimo(generador);
			primo2 = generarPrimo(generador);
			modulo = primo1.multiply(primo2);
			numeroDeTrabajo = primo1.subtract(BigInteger.ONE).multiply(primo2.subtract(BigInteger.ONE));
		} while (primo1.equals(primo2) || !clavePublica.gcd(numeroDeTrabajo).equals(BigInteger.ONE));
		clavePrivada = clavePublica.modInverse(numeroDeTrabajo);
	}

	// Recorre la matriz fila por fila, R, G, B, y arma el numero en base 256.
	static BigInteger matrizANumero(int[][][] matriz) {
		BigInteger numero = BigInteger.ZERO;
		for (int[][] fila : matriz) {
			for (int[] pixel : fila) {
				for (int valor : pixel) {
					numero = numero.multiply(BigInteger.valueOf(256)).add(BigInteger.valueOf(valor));
				}
			}
		}
		return numero;
	}

	// Emisor: firma = numero elevado a la clave privada, residuo entre el modulo
	static BigInteger firmar(int[][][] hash) {
		return matrizANumero(hash).modPow(clavePrivada, modulo);
	}

	// Receptor: la firma elevada a la clave publica debe dar el numero de SU hash recalculado
	static boolean verificar(int[][][] hashRecalculado, BigInteger firma) {
		BigInteger numeroRecuperado = firma.modPow(clavePublica, modulo);
		return numeroRecuperado.equals(matrizANumero(hashRecalculado));
	}

	public static void main(String[] args) {
		generarClaves(System.nanoTime());

		// EMISOR
		int[][][] hashEmisor = CifradoTextoRGB.cifrarComoMatrizRGB("ciencias de la computacion 2", 4, 10);
		BigInteger firma = firmar(hashEmisor);
		System.out.println("Firma: " + firma);

		// RECEPTOR
		int[][][] hashOriginal = CifradoTextoRGB.cifrarComoMatrizRGB("ciencias de la computacion 2", 4, 10);
		int[][][] hashAlterado = CifradoTextoRGB.cifrarComoMatrizRGB("ciencias de la computacion 3", 4, 10);
		System.out.println("Original: " + verificar(hashOriginal, firma));
		System.out.println("Alterado: " + verificar(hashAlterado, firma));
	}
}