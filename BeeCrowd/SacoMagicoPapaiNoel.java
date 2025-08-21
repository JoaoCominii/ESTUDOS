/*
 * Entrada
 * A primeira linha da entrada contém um inteiro N correspondedendo ao número de operações feitas sobre a pilha de presentes. 
 * As operações podem ser de três tipos: "PUSH V" onde V é um inteiro que representa o grau de diversão do presente 
 * sendo colocado na pilha; 
 * "POP" que representa que o papai Noel está tirando um presente da pilha para entregar 
 * "MIN" que representa uma consulta do Noel para saber o menor valor de presente na pilha.
 * Saida
 * A saída consiste em uma linha contendo um inteiro com o menor valor de presente na pilha para as consultas do tipo "MIN" 
 * ou a mensagem "EMPTY" para as operações "MIN" e "POP" quando a pilha estiver vazia.
 */
import java.util.Scanner;

public class SacoMagicoPapaiNoel {
    public static void main(String[] args) {
        int N;
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        int[] pilha = new int[N];
        int topo = -1;
        int valor, menor;
        String operacao;
        for(int i = 0; i < N; i++)
        {
            operacao = scanner.next();
            if(operacao.equals("PUSH"))
            {
                valor = scanner.nextInt();
                topo++;
                pilha[topo] = valor;
            }
            else if(operacao.equals("POP"))
            {
                if(topo == -1) // pilha vazia
                {
                    System.out.println("EMPTY");
                }
                else{
                    topo--; // Remover o elemento do topo
                }
            }
            else if(operacao.equals("MIN"))
            {
                if(topo == -1)
                {
                    System.out.println("EMPTY");
                }
                else
                {
                    menor = pilha[0];
                    for(int j = 1; j <= topo; j++)
                    {
                        if(pilha[j] < menor)
                        {
                            menor = pilha[j];
                        }
                    }
                    System.out.println(menor);
                }     
            }
        }
        scanner.close();
    }
}
