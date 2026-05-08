
import java.util.Scanner;

public class EstacionamentoLinear {
    public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      int N, K;
      do{
        N = scanner.nextInt();
        K = scanner.nextInt();
        int[] pilha = new int[K];
        int topo = -1;
        boolean possivel = true;
        for(int i = 0; i < N; i++)
        {
            int chegada = scanner.nextInt();
            int saida = scanner.nextInt();

            // remover carros que ja sairam
            while(topo >= 0 && pilha[topo] < chegada)
            {
                topo--;
            }

            // verificar espaco para outro carro
            if(topo + 1 < K)
            {
                pilha[++topo] = saida; // novo carro
            }
            else{
                possivel = false;
            }
        }

        System.out.println(possivel ? "SIM" : "NAO");

      }while(N != 0 && K != 0);
      




      scanner.close();
    }
}
