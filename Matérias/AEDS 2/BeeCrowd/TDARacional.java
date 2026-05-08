
import java.util.Scanner;

public class TDARacional {
    // Calcular maximo divisor comum
    public static int mdc(int a, int b)
    {
        if(b == 0)
            return Math.abs(a);
        return mdc(b, a % b);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        for(int i = 0; i < N; i++)
        {
            int n1 = scanner.nextInt();
            scanner.next(); // Ignorar a "/"
            int d1 = scanner.nextInt();
            String op = scanner.next();
            int n2 = scanner.nextInt();
            scanner.next(); // Ignorar a "/"
            int d2 = scanner.nextInt();
            int num = 0, den = 0;
            if(op.equals("+"))
            {
                num = n1 * d2 + n2 * d1;
                den = d1 * d2;
            }
            else if(op.equals("-"))
            {
                num = n1 * d2 - n2 * d1;
                den = d1 * d2;
            }
            else if(op.equals("*"))
            {
                num = n1 * n2;
                den = d1 * d2;
            }
            else if(op.equals("/"))
            {
                num = n1 * d2;
                den = n2 * d1;
            }
            // Simplificar
            int mdc = mdc(num, den);
            int simpNum = num / mdc;
            int simpDen = den / mdc;

            // Ajusta sinais pro denominador ser sempre positivo
            if(simpDen < 0)
            {
                simpNum *= -1;
                simpDen *= -1;
            }

            System.out.println(num + "/" + den + "=" + simpNum + "/" + simpDen);
        }
        scanner.close();
    }
}
