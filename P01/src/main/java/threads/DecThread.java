package threads;

public class DecThread extends Thread {

    public static final int NUM_ITERS = 50;

    private Entero _e;

    public DecThread(Entero e) {
        _e = e;
    }

    public void run() {
        for (int i = 0; i < NUM_ITERS; i++) {
            _e.decrementar();
        }
    }
}
