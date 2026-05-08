/*
 * Alguém deixou o quadro de medalhas das olimpíadas fora de ordem. 
 * Seu programa deve colocá-lo na ordem correta. A ordem dos países no quadro de medalhas é dada pelo número de medalhas de ouro. 
 * Se há empate em medalhas de ouro, a nação que tiver mais medalhas de prata fica a frente. 
 * Havendo empate em medalhas de ouro e prata, fica mais bem colocado o país com mais medalhas de bronze. 
 * Se dois ou mais países empatarem nos três tipos de medalhas, seu programa deve mostrá-los em ordem alfabética.
 * Entrada
 * A entrada é dada pelo número de países participantes N (0 ≤ N ≤ 500) seguido pela lista dos países, 
 * com suas medalhas de ouro O (0 ≤ O ≤ 10000), prata P (0 ≤ P ≤ 10000) e bronze B (0 ≤ B ≤ 10000).
 * Saida
 * A saída deve ser a lista de países, com suas medalhas de ouro, prata e bronze, 
 * na ordem correta do quadro de medalhas, com as nações mais premiadas aparecendo primeiro.
 */

import java.util.Scanner;


public class QuadroMedalhas
{
    public static void main(String[] args) {
        int N;
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        scanner.nextLine();
        String[] nomes = new String[N];
        int[] Ouro = new int[N];
        int[] Prata = new int[N];
        int[] Bronze = new int[N];
        for(int i = 0; i < N; i++) // Receber n nomes, ouros, pratas e bronzes
        {
            nomes[i] = scanner.next();
            Ouro[i] = scanner.nextInt();
            Prata[i] = scanner.nextInt();
            Bronze[i] = scanner.nextInt();
        }
        for(int i = 0; i < N - 1; i++)
        {
            for(int j = 0; j < N - i - 1; j++)
            {
                if(precisaTrocar(j, Ouro, Prata, Bronze, nomes))
                {
                    trocar(j, nomes, Ouro, Prata, Bronze);
                }
            }
        }
        // Imprimir saida
        for(int i = 0; i < N; i++)
        {
            System.out.println(nomes[i] + " " + Ouro[i] + " " + Prata[i] + " " + Bronze[i]);
        }
        scanner.close();
    }
    private static boolean precisaTrocar(int j, int[] ouro, int[] prata, int[] bronze, String[] nomes)
    {
        if(ouro[j] < ouro[j + 1]) return true;
        if(ouro[j] == ouro[j + 1] && prata[j] < prata[j + 1]) return true;
        if(ouro[j] == ouro[j + 1] && prata[j] == prata[j +1] && bronze[j] < bronze[j + 1]) return true;
        if(ouro[j] == ouro[j + 1] && prata[j] == prata[j + 1] && bronze[j] == bronze[j + 1] && nomes[j].compareTo(nomes[j + 1]) > 0) return true;
        return false; 
    }
    public static void trocar(int j, String[] nomes, int[] Ouro, int[] Prata, int[] Bronze)
    {
        // Trocar nome
        String tempNome = nomes[j];
        nomes[j] = nomes[j + 1];
        nomes[j + 1] = tempNome;
        // Trocar Ouro
        int tempOuro = Ouro[j];
        Ouro[j] = Ouro[j + 1];
        Ouro[j + 1] = tempOuro;
        // Trocar prata
        int tempPrata = Prata[j];
        Prata[j] = Prata[j + 1];
        Prata[j + 1] = tempPrata;
        // Trocar Bronze
        int tempBronze = Bronze[j];
        Bronze[j] = Bronze[j + 1];
        Bronze[j + 1] = tempBronze;
    }
}