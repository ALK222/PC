package Actors;

import Storage.Producto;
import semaphores.Shared;

public class Producer extends Thread {

    private Shared s;
    private int toProduce;

    public Producer(Shared s, int toProduce) {
        this.s = s;
        this.toProduce = toProduce;
    }

    @Override
    public void run() {
        for (int i = 0; i < toProduce; ++i) {
            try {
                s.empty.acquire();
                s.mutexP.acquire();
                s.buffer.almacenar(new Producto());
                s.mutexP.release();
                s.full.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
