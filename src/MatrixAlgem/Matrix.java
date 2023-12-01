package MatrixAlgem;

import javax.xml.transform.SourceLocator;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Objects;

public class Matrix {
    private int rowsCount;
    private int colsCount;
    private double[][] matrix;

    Matrix(double[][] matrix) {
        matrix = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new); // копируем матрицу
        rowsCount = matrix.length;
        colsCount = matrix[0].length;
    }
    public double[][] getMatrix() {
        return matrix;
    }

    public void solve(double[][] BB) {
        Scanner sc = new Scanner(System.in);
        double[][] B = Arrays.stream(BB).map(double[]::clone).toArray(double[][]::new); // копирование матрицы B
        double[][] A = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new); // копирование матрицы A
        System.out.println("Введите уровень точности (до какого знака после запятой следует округлить)");
        double ScaleDegree = sc.nextDouble();
        while (ScaleDegree < 0) {
            System.out.println("Минимальный уровень точности - 0");
            ScaleDegree = sc.nextDouble();
        }

        if (A[0].length != B.length) {
            System.out.println("ошибка");
            return;
        }

        double[][] new_matrix = new double[B.length][A.length + 1]; // расширенная матрица
        for (int i = 0; i < A.length; i++) {  // склеивание A|B
            for (int j = 0; j < A[0].length; j++) {
                new_matrix[i][j] = A[i][j];
            }
        }
        for (int i = 0; i < B.length; i++) {
            new_matrix[i][new_matrix.length] = B[i][0];
        }

        for (int i = 0; i < new_matrix.length; i++) { // печать расширенной матрицы
            for (int j = 0; j < new_matrix[0].length; j++) {
                if (j == matrix.length - 1) System.out.print(new_matrix[i][j] + " | ");
                else System.out.print(new_matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();


        for (int i = 0; i < new_matrix[0].length; i++) { // метод Гаусса прямой ход
            for (int j = i + 1; j < new_matrix.length; j++) {
                if (new_matrix[i][i] == 0) {
                    for (int k = i + 1; k < new_matrix.length; k++) {
                        if (new_matrix[k][i] != 0) {  // проверка на нуль на гд
                            double[] tmp = new_matrix[i];
                            new_matrix[i] = new_matrix[k];
                            new_matrix[k] = tmp;
                            break;
                        }
                    }
                }
                if (new_matrix[i][i] == 0) {
                    System.out.println("computer: система имеет больше одного решения или не имеет вообще");
                    return;
                }
                double k = new_matrix[j][i] / new_matrix[i][i];


                for (int l = 0; l < new_matrix[0].length; l++) {
                    new_matrix[j][l] -= new_matrix[i][l] * k;
                }
            }
        }

        for (int i = new_matrix.length - 1; i > 0; i--) { // обратный ход МГ
            for (int j = i - 1; j >= 0; j--) {
                if (new_matrix[i][i] == 0) {
                    System.out.println("система имеет больше одного решения или не имеет вообще");
                    return;
                }
                double k = new_matrix[j][i] / new_matrix[i][i];
                for (int l = 0; l < new_matrix[0].length; l++) {
                    new_matrix[j][l] = (-k) * new_matrix[i][l] + new_matrix[j][l];
                }
            }
        }

        double[] result = new double[new_matrix.length]; // печать ответа
        for (int i = 0; i < new_matrix.length; i++) {

            double scale = Math.pow(10, ScaleDegree);
            result[i] = Math.round(new_matrix[i][new_matrix.length] / new_matrix[i][i] * scale) / scale;
            System.out.println("X" + (i + 1) + " = " + result[i]);
        }

    }

    public Matrix() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Введить количество строк: ");
            rowsCount = sc.nextInt();
            System.out.print("Введить количество столбцов: ");
            colsCount = sc.nextInt();
            matrix = new double[rowsCount][colsCount];
        } catch (Exception e) {
            System.out.println("Вы ввели что-то неверно!");
        }
    }

    public int rank() {
        double[][] new_matrix = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new); //копируется матрица
        int N = 0; // прибавляется чтобы пропустить столб
        for (int i = 0; i < new_matrix[0].length - N; i++) {  // метод Гаусса
            for (int j = i + 1; j < new_matrix.length; j++) {
                if (new_matrix[i][i + N] == 0) { // swap столбца с нулем
                    while (new_matrix[i][i + N] == 0) {
                        for (int k = i + 1; k < new_matrix.length; k++) {
                            if (new_matrix[k][i + N] != 0) {
                                double[] tmp = new_matrix[i];
                                new_matrix[i] = new_matrix[k];
                                new_matrix[k] = tmp;
                                break;
                            }
                            N++;
                        }
                    }
                }
                double k = new_matrix[j][i + N] / new_matrix[i][i + N];
                for (int l = 0; l < new_matrix[0].length - N; l++) {
                    new_matrix[j][l + N] -= new_matrix[i][l + N] * k;
                }
            }
        }

        int cnt = 0; // количество нулевых столбцов
        for (double[] newMatrix : new_matrix) {
            int f = 0;
            for (int j = 0; j < new_matrix[0].length; j++) {
                if (Math.abs(newMatrix[j]) < 0.000000001) f++;
            }
            if (f == new_matrix[0].length) {
                cnt += 1;
            }
        }
        return new_matrix.length - cnt;
    }
    public void fillMatrix() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Как вы хотите ввести элементы матрицы? поэлементно или построчно");
        String type = sc.nextLine();
        if (type.equals("поэлементно") || type.equals("поэлем") || type.equals("ПОЭЛЕМЕНТНО") || type.equals("by elements") || type.equals("byelements") || type.equals("by elems") || type.equals("byelems")) {
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < colsCount; j++) {
                    System.out.println("Введите элемент: [" + (i + 1) + "][" + (j + 1) + "] ");
                    double temp = sc.nextDouble();
                    matrix[i][j] = temp;
                }
            }
        } else if (type.equals("построчно") || type.equals("постр") || type.equals("ПОСТРОЧНО") || type.equals("by strings") || type.equals("bystrings") || type.equals("by str") || type.equals("bystr")) {
            for (int i = 0; i < rowsCount; i++) {
                System.out.printf("Введите числа в строку (i = %d): ", i + 1);
                String line = sc.nextLine();
                String[] numbers = line.split(" ");
                for (int j = 0; j < colsCount; j++) {
                    matrix[i][j] = Double.parseDouble(numbers[j]);
                }
            }
        }
    }
    public void transposition() {
        double[][] new_matrix = new double[colsCount][rowsCount];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                new_matrix[j][i] = this.matrix[i][j];
            }
        }
        printMatrix(new_matrix);
    }
    public double[][] multiplyMatrices(double[][] B) {
        double[][] other = B;
        Scanner sc = new Scanner(System.in);
        if (matrix[0].length != other.length) {
            if (other[0].length == matrix.length) {
                System.out.println("computer: Прямое умножение невозможно");
                System.out.println("Хотите умножить матрицы в обратном порядке? (y/n)");
                String y_n = sc.nextLine();
                if (Objects.equals(y_n, "y")) {
                    double[][] trash = matrix;
                    matrix = other;
                    other = trash;
                } else {
                    return null;
                }
            } else {
                System.out.println("Невозможно умножить матрицы");
                return null;
            }
        }
        double[][] new_matrix = new double[matrix.length][matrix[0].length];
        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix[0].length; i++) {
                new_matrix[j][i] = 0;
                for (int k = 0; k < matrix[0].length; k++) {
                    new_matrix[j][i] += matrix[j][k] * other[k][i];
                }
            }
        }
        printMatrix(new_matrix);
        return new_matrix;
    }
    public double[][] mulitplyMatrix(Matrix m) {
        Scanner sc = new Scanner(System.in);
        double[][] result;
        if (colsCount == m.rowsCount) {
            result = new double[rowsCount][m.colsCount];
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < m.colsCount; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < colsCount; k++) {
                        result[i][j] += matrix[i][k] * m.matrix[k][j];
                    }
                }
            }
            printMatrix(result);
            return result;
        } if (m.colsCount == rowsCount) {
            System.out.println("Матрицы можно умножить B * A, нужно?");
            String q = sc.nextLine();
            if (q.equals("yes") || q.equals("да") || q.equals("y") || q.equals("д")) {
                result = new double[m.rowsCount][colsCount];
                for (int i = 0; i < m.rowsCount; i++) {
                    for (int j = 0; j < colsCount; j++) {
                        result[i][j] = 0;
                        for (int k = 0; k < m.colsCount; k++) {
                            result[i][j] += m.matrix[i][k] * matrix[k][j];
                        }
                   }
                }
                printMatrix(result);
                return result;
            } else {
                return null;
            }
        }
        return null;
    }
    public void gauss_view(Matrix ma, Matrix mb) {
        ma.solve(mb.getMatrix());
        System.out.println("ранг матрицы: " + ma.rank());
    }

    public void printMatrix() {
        System.out.println("Матрица (" + rowsCount + "*" + colsCount + ")");

        for (double[] trash : matrix) {
            int cnt = 0;
            System.out.print("(");
            for (double i : trash) {
                cnt += 1;
                System.out.print(cnt == trash.length ? i : i + " ");
            }
            System.out.println(")");
        }
    }
    private void printMatrix(double[][] matrix) {
        System.out.println("Matrix (" + rowsCount + "*" + colsCount + ")");

        for (double[] trash : matrix) {
            int cnt = 0;
            System.out.print("(");
            for (double i : trash) {
                cnt += 1;
                System.out.print(cnt == trash.length ? i : i + " ");
            }
            System.out.println(")");
        }
    }
}
