package MatrixAlgem;

public class Main {
    public static void main(String[] args) {
        Matrix a = new Matrix();
        a.fillMatrix();
        a.printMatrix();
//        a.transposition();
        
        Matrix b = new Matrix();
        b.fillMatrix();
        b.printMatrix();
//        a.multiplyMatrices(b.getMatrix());

        a.gauss_view(a, b);
    }
}