
import java.util.Scanner;

public class TimeDuendes {
    public static void main(String[] args) {
        int N;
        Scanner scanner  =  new Scanner(System.in);
        N = scanner.nextInt();
        scanner.nextLine(); // Pra tirar o enter do buffer pra proxima leitura (exigido pelo int)
        String[] nomes = new String[N];
        int[] idades = new int[N];

        for(int i=0; i < N; i++) // Guardar N nomes e idades dos duendes
        {
            nomes[i] = scanner.next();
            idades[i] = scanner.nextInt();
            scanner.nextLine();
        }
        scanner.close();
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N - i - 1; j++) // Ordenar mais velho pra mais novo 
            {
                if(idades[j] < idades[j + 1])
                {
                    int temp = idades[j];
                    idades[j] = idades[j + 1];
                    idades[j+1] = temp;
                }
                else if(idades[j] == idades[j+1]) // Ordenar por nome ascendente
                {
                    /*
                     * O método compareTo é parte da classe String em Java e segue as seguintes regras:
                     * Retorno Positivo (> 0): Se a string no índice j (ou seja, nomes[j]) for 
                     * lexicograficamente maior (vem depois) que a string no índice j + 1 (ou seja, nomes[j + 1]).
                     * Retorno Zero (== 0): Se as strings forem iguais.
                     * Retorno Negativo (< 0): Se a string no índice j for lexicograficamente menor (vem antes) que a string no índice j + 1.
                     * A ordem lexicográfica é basicamente a ordem alfabética, mas com base nos valores numéricos dos caracteres Unicode.
                     */
                    if(nomes[j].compareTo(nomes[j + 1]) > 0) 
                    {
                        String temp = nomes[j];
                        nomes[j] = nomes[j + 1];
                        nomes[j + 1] = temp;
                    }
                }
            }
        }
        // Dividindo os times
        int numTimes = N / 3;
        for(int i = 0; i < numTimes; i++)
        {
            System.out.println("Time " + (i + 1));
            System.out.println(nomes[i] + " " + idades[i]); // Lider
            System.out.println(nomes[i + numTimes] + " " + idades[i + numTimes]); // Entregador
            System.out.println(nomes[i + 2 * numTimes] + " " + idades[i + 2 * numTimes]); // Piloto
            System.out.println(); // Linha em branco entre os times
        }
    }
}
