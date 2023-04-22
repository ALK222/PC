import java.util.Vector;

import locks.Lock;
import locks.Lock2;
import locks.LockBakery;
import locks.LockRompeEmpate;
import locks.LockTicket;
import threads.DecThread;
import threads.Entero;
import threads.IncThread;

public class Main {

	public boolean lock = false;

	private static void ejercicio1() throws InterruptedException {
		int numThreads = 1;
		Lock l = new Lock2();
		Entero e = new Entero();

		Vector<Thread> its = new Vector<Thread>();

		Thread t1 = new IncThread(e, 1, l);
		Thread t2 = new DecThread(e, 2, l);
		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println(e.getValor());
	}

	public static void ejercicio2() throws InterruptedException {

		System.out.println("Probando lock rompe-empate de M elementos");

		int numThreads = 5;

		LockRompeEmpate l = new LockRompeEmpate((numThreads * 2) + 1);
		Entero e = new Entero();

		Vector<Thread> its = new Vector<Thread>();

		for (int i = 1; i <= numThreads; i++) {
			Thread t1 = new IncThread(e, i, l);
			its.add(t1);
		}
		for (int i = numThreads + 1; i < (numThreads * 2) + 1; i++) {
			Thread t2 = new DecThread(e, i, l);
			its.add(t2);
		}

		for (Thread thread : its) {
			thread.start();

		}
		for (Thread thread : its) {
			thread.join();
		}

		System.out.println(e.getValor());

		System.out.println("Probando lock ticket de M elementos");

		LockTicket lt = new LockTicket((numThreads * 2) + 1);
		Entero e1 = new Entero();

		Vector<Thread> its2 = new Vector<Thread>();

		for (int i = 1; i <= numThreads; i++) {
			Thread t1 = new IncThread(e1, i, lt);
			its2.add(t1);
		}
		for (int i = numThreads + 1; i < (numThreads * 2) + 1; i++) {
			Thread t2 = new DecThread(e1, i, lt);
			its2.add(t2);
		}

		for (Thread thread : its2) {
			thread.start();

		}
		for (Thread thread : its2) {
			thread.join();
		}

		System.out.println(e.getValor());
		
		System.out.println("Probando lock backery de M elementos");

		LockBakery lb = new LockBakery((numThreads * 2) + 1);
		Entero e2 = new Entero();

		Vector<Thread> its3 = new Vector<Thread>();

		for (int i = 1; i <= numThreads; i++) {
			Thread t1 = new IncThread(e2, i, lb);
			its3.add(t1);
		}
		for (int i = numThreads + 1; i < (numThreads * 2) + 1; i++) {
			Thread t2 = new DecThread(e2, i, lb);
			its3.add(t2);
		}

		for (Thread thread : its3) {
			thread.start();

		}
		for (Thread thread : its3) {
			thread.join();
		}

		System.out.println(e.getValor());

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

		default:
			throw new IllegalArgumentException("Argumento no válido.\n Argumentos validos <1, 2, 3>");
		}
	}

}
