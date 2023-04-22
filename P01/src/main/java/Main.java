import java.util.Vector;

import threads.DecThread;
import threads.Entero;
import threads.IdThread;
import threads.IncThread;
import threads.MatrixThread;

public class Main {

	private static void ejercicio1() throws InterruptedException {

		long time = 5;
		int numThreads = 20;

		Vector<Thread> ts = new Vector<Thread>();

		System.out.println("Creating threads");
		for (int i = 0; i < numThreads; i++) {
			IdThread t = new IdThread(time);
			t.setName("Thread" + i);
			ts.add(t);
			t.start();

		}

		for (int i = 0; i < numThreads; ++i) {
			ts.get(i).join();
		}

		System.out.println("All threads stopped");
	}

	private static void ejercicio2() throws InterruptedException {

		int numThreads = 100;
		Entero e = new Entero();

		Vector<Thread> its = new Vector<Thread>();
		Vector<Thread> dts = new Vector<Thread>();

		for (int i = 0; i < numThreads; ++i) {
			its.add(new IncThread(e));
			dts.add(new DecThread(e));

			its.get(i).start();
			dts.get(i).start();

		}

		for (int i = 0; i < numThreads; ++i) {
			its.get(i).join();
			dts.get(i).join();
		}

		System.out.println(e.getValor());
	}

	private static void ejercicio3() throws InterruptedException {

		int numElems = 10;
		Vector<Thread> mts = new Vector<Thread>();

		int A[][] = new int[numElems][numElems];
		int B[][] = new int[numElems][numElems];
		int C[][] = new int[numElems][numElems];

		int val = 0;

		for (int i = 0; i < numElems; ++i)
			for (int j = 0; j < numElems; ++j) {
				A[i][j] = val;
				++val;
			}

		for (int i = 0; i < numElems; ++i)
			for (int j = 0; j < numElems; ++j) {
				B[i][j] = val;
				++val;
			}

		for (int i = 0; i < numElems; ++i)
			mts.add(new MatrixThread(numElems, i, A, B, C));

		for (int i = 0; i < numElems; ++i)
			mts.get(i).start();

		for (int i = 0; i < numElems; ++i)
			mts.get(i).join();

		System.out.println("A:");

		for (int i = 0; i < numElems; ++i) {

			for (int j = 0; j < numElems - 1; ++j)
				System.out.printf("%2d ", A[i][j]);

			System.out.printf("%2d\n", A[i][numElems - 1]);

		}

		System.out.println();

		System.out.println("B:");

		for (int i = 0; i < numElems; ++i) {

			for (int j = 0; j < numElems; ++j)
				System.out.printf("%3d ", B[i][j]);

			System.out.printf("%3d\n", B[i][numElems - 1]);

		}

		System.out.println();

		System.out.println("C:");

		for (int i = 0; i < numElems; ++i) {

			for (int j = 0; j < numElems - 1; ++j)
				System.out.printf("%6d ", C[i][j]);

			System.out.printf("%6d\n", C[i][numElems - 1]);

		}

	}

	public static void main(String[] args) throws InterruptedException {

		if (args.length > 1) {
			throw new IllegalArgumentException("Demasiados argumentos.\n Argumentos validos <1, 2, 3>");
		}

		switch (Integer.parseInt(args[0])) {
			case 1:
				ejercicio1();
				break;
			case 2:
				ejercicio2();
				break;
			case 3:
				ejercicio3();
				break;

			default:
				throw new IllegalArgumentException("Argumento no válido.\n Argumentos validos <1, 2, 3>");
		}
	}

}
