package launcher;

import Actors.Consumer;
import Actors.Producer;
import semaphores.Shared;

public class Main {
	
	private static int CAP = 5; // product buffer size
	private static int NUM_CONSUMERS = 14; // number of consummers
	private static int NUM_PRODUCTS = 300; // number of products
	private static int NUM_PRODUCERS = 7; // number of producers
	
	public static void main(String[] args) {
		
		Producer[] producers = new Producer[NUM_PRODUCERS];
		Consumer[] consumers = new Consumer[NUM_CONSUMERS];
		
		Shared s = new Shared(NUM_PRODUCERS + NUM_CONSUMERS, CAP);
		
		for(int i = 0; i < NUM_PRODUCERS; ++i) {
			producers[i] = new Producer(s, NUM_PRODUCTS / NUM_PRODUCERS);
		}
		
		for (int i = 0; i < NUM_CONSUMERS; ++i) {
			consumers[i] = new Consumer(s, NUM_PRODUCTS / NUM_CONSUMERS);
        }

        for (int i = 0; i < NUM_PRODUCERS; ++i) {
        	producers[i].start();
        }
        for (int i = 0; i < NUM_CONSUMERS; ++i) {
        	consumers[i].start();
        }

        for (int i = 0; i < NUM_PRODUCERS; ++i) {
            try {
            	producers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < NUM_CONSUMERS; ++i) {
            try {
            	consumers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Program finished without errors");
	}

}
