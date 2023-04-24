/*
 * 
 */
package utils;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Lock using a ticket system.
 */
public class LockTicket implements Lock {

	/** Number of total threads. */
	private int _numThreads;

	/** Number that keeps track of next ticket. */
	volatile private AtomicInteger _number;
	/** Number that keeps track of the next turn. */
	volatile private int _next;
	// volatile int _turn[]; // No usar, eficiencia mala comparada con la otra.
	// opcion

	private Entero _turn[]; // Al ser el valor volatil y no todo el array no tenemos necesidad de
							// reactualizar todo el array haciendo _turn = _turn.

	/**
	 * Instantiates a new ticket lock.
	 *
	 * @param numThreads number of possible threads.
	 */
	public LockTicket(int numThreads) {
		_numThreads = numThreads;

		_turn = new Entero[numThreads];
		_number = new AtomicInteger(1);

		_next = 1;

		for (int i = 0; i < _numThreads; i++) {
			_turn[i] = new Entero();
		}

	}

	public int takeLock(int pid) {
		_turn[pid].setValor(_number.getAndAdd(1));

		while (_turn[pid].getValor() != _next)
			;

		return 1;
	}

	public int releaseLock(int pid) {
		_next += 1;

		return 1;
	}

}
