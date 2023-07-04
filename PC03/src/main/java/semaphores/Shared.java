package semaphores;

import java.util.concurrent.Semaphore;

import Storage.AlmacenN;

public class Shared {
	
	public int numThreads;
	public Semaphore empty;
	public Semaphore full;
	public Semaphore mutexP;
	public Semaphore mutexC;
	public AlmacenN buffer;
	
	public Shared(int numThreads, int bufferSize) {
		this.numThreads = numThreads;
		this.empty = new Semaphore(bufferSize);
		this.full = new Semaphore(0);
		this.mutexP = new Semaphore(1);
		this.mutexC = new Semaphore(1);
		this.buffer = new AlmacenN(bufferSize);
	}

}
