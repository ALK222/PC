package threads;

public class Entero {
    private int _valor;

    public Entero() {
        _valor = 0;
    }

    public void incrementar() {
        _valor++;
    }

    public void decrementar() {
        _valor--;
    }

    public int getValor() {
        return _valor;
    }
}
