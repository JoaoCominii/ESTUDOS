import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SBCGerenciador {
    public static void main(String[] args) {
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linha;
        int N;
        while((linha = br.readLine()) != null)
        N = Integer.parseInt(linha); // Converter string para int
        int[][] processos = new int[N][2]; // Array para armazenar os processos
        for(int i = 0; i < N; i++)
        {
            String[] entrada = br.readLine().split(" ");
            processos[i][0] = Integer.parseInt(entrada[0]); // Tempo de requisicao
            processos[i][1] = Integer.parseInt(entrada[1]); // Duracao
        }

        // Ordenar os processos usando bubble sort (pelo tempo de requisicao)
        for(int i = 0; i < N - 1; i++)
        {
            for(int j = 0; j < N - i - 1; j++)
            {
                if(processos[j][0] > processos[j + 1][0])
                {
                    int[] temp = processos[j];
                    processos[j] = processos[j + 1];
                    processos[j + 1] = temp;
                }
            }
        }

        long tempoAtual = 1;
        long tempoDeEsperaTotal = 0;

        for(int i = 0; i < N; i++)
        {
            int tempoRequisicao = processos[i][0];
            int duracao = processos[i][1];
            
            if(tempoAtual < tempoRequisicao);
            {
                tempoAtual = tempoRequisicao;
            }
            
            tempoDeEsperaTotal += (tempoAtual - tempoRequisicao);
            tempoAtual += duracao;
        }
    
        System.out.println(tempoDeEsperaTotal);
    }
}
