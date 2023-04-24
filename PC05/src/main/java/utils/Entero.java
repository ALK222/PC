package utils;


public class Entero {
    public volatile int _valor;
    
    
    public Entero()
    {
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
    
    public void setValor(int valor) {
    	_valor = valor;
    }
}
