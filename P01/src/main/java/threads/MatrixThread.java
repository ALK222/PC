package threads;

public class MatrixThread extends Thread {
    private int _tam;

    private int _i;

    private int _A[][];
    private int _B[][];
    private int _C[][];

    public MatrixThread(int tam, int i, int A[][], int B[][], int C[][]) {
        _tam = tam;
        _i = i;
        _A = A;
        _B = B;
        _C = C;
    }

    public void run() {
        for (int j = 0; j < _tam; j++) {
            _C[_i][j] = 0;
            for (int k = 0; k < _tam; k++) {
                _C[_i][j] += _A[_i][k] * _B[k][j];
            }
        }
    }
}
