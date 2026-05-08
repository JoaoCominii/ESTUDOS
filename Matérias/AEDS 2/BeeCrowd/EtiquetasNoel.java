
import java.util.Scanner;

public class EtiquetasNoel {
    public static void main(String[] args) {
        /*  
        * N = quantidade de traducoes presentes na entrada.
        * M = quantidade de criancas que recebem cartas.
        */ 
        int N, M; 
        Scanner scanner= new Scanner(System.in);
        N = scanner.nextInt();
        scanner.nextLine();
        String[] linguas = new String[N];
        String[] traducoes = new String[N];
        // Ler N linguas e traducoes
        for(int i = 0; i < N; i++)
        {
            linguas[i] = scanner.nextLine();
            traducoes[i] = scanner.nextLine();
        }

        M = scanner.nextInt();
        scanner.nextLine();
        for(int i = 0; i < M; i++) // Ler M nomes e linguas nativas
        {
            String nome = scanner.nextLine();
            String lingua = scanner.nextLine();
            // Procurar lingua correspondente
            String mensagem = "";
            for(int j = 0; j < N; j++)
            {
                if(linguas[j].equals(lingua))
                {
                    mensagem = traducoes[j];
                    j = N;
                }
            }
            // Construir a etiqueta
            System.out.println(nome);
            System.out.println(mensagem);
            System.out.println();
        }
        scanner.close();
    }
}
