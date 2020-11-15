import java.util.Scanner;

class Matrix
{
    int Dimension;

    public long calcDeterminant(int Dimension, long[][] Elements)
    {
        if (Dimension == 2)
            return Elements[0][0] * Elements[1][1] - Elements[0][1] * Elements[1][0];

        long[][] newElement = new long[Dimension - 1][Dimension - 1];
        int i = 0, j = 0, k =0, l = 0, q = -1, z = -1;
        long Determinant = 0;
        boolean detectO = false;

        for (j = 0; j < Dimension; j++)
        {
            for (k = 1; k < Dimension; k++)
            {
                for (l = 0; l < Dimension; l++)
                {
                    if (l != j)
                    {
                        z++;
                        newElement[k - 1][z] = Elements[k][l];
                    }
                }
                z = -1;
            }
            if ( j%2 == 0)
                detectO = false;
            else
                detectO = true;
            Determinant += Elements[0][j] * ( detectO ? (-1):(1)) * calcDeterminant(Dimension - 1, newElement);
        }
        return Determinant;
    }
}
public class Main
{

    public static void main(String[] args)
    {
        Scanner Scan = new Scanner(System.in);
        int dimension = Scan.nextInt();
        long Determinant = 0;
        long[][] element = new long[dimension][dimension];
        Matrix matrix = new Matrix();

        int i = 0;
        int j = 0;

        for (i = 0; i < dimension; i += 1) {
            for (j = 0; j < dimension; j += 1) {
                element[i][j] = Scan.nextInt();
            }
        }

        Determinant = matrix.calcDeterminant(dimension, element);
        System.out.print(Determinant);
    }
}