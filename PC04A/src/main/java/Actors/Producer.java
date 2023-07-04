package Actors;

import Storage.Producto;
import monitors.MonitorSynch;

public class Producer extends Thread {

    private MonitorSynch s;
    private int toProduce;
    private int batch;
    
    public Producer(MonitorSynch s, int toProduce, int batch) {
        this.s = s;
        this.toProduce = toProduce;
        this.batch = batch;
    }
    
    public Producer(MonitorSynch s, int toProduce) {
        this.s = s;
        this.toProduce = toProduce;
    }

    @Override
    public void run() {
    	// MONITOR SYNC
        for (int i = 0; i < toProduce; ++i) {
            s.almacenar(new Producto());
        }
        // MONITOR CONDITION
        /*for (int i = 0; i < toProduce / batch; ++i) {
            s.almacenar(new Producto[batch], batch);
        }*/
    }

}
