package threads;

public class IncThread extends Thread {

    public static final int NUM_ITERS = 50;

    private Entero _e;

    public IncThread(Entero e) {
        _e = e;
    }

    public void run() {
        for (int i = 0; i < NUM_ITERS; i++) {
            _e.incrementar();
        }
    }
}
