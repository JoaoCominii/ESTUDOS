
import java.util.Scanner;

public class ProtocoloTCP {
    public static void main(String[] args) {
        int N;
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        scanner.next();
        String pacote;
        String[] numero = new  String[N];
        for(int i = 0; i < N; i++)
        {
            pacote = scanner.nextLine();
            String[] partes = pacote.split(" ");
            numero[i] = partes[1];
        }
        // Ordenar
        for(int i = 0; i < N -1; i++)
        {
            int menor = i;
            for(int j = i +1; j < N; j++)
            {
                if(numero[menor].compareTo(numero[j]) > 0)
                    menor = j;
            }
            String tmp = numero[menor];
            numero[menor] = numero[i];
            numero[i] = tmp;
        }

        // Print na tela
        for(int i = 0; i < N; i++)
        {
            System.out.println("Package " + numero[i]);
        }

    }
}
