import java.util.Scanner;

public class Nota {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int N = sc.nextInt();
            int K = sc.nextInt();
            int[] maiores = new int[K];
            int count = 0;

            for (int i = 0; i < N; i++) {
                int nota = sc.nextInt();
                // Insere ordenado nas K maiores
                if (count < K) {
                    int j = count - 1;
                    while (j >= 0 && maiores[j] > nota) {
                        maiores[j + 1] = maiores[j];
                        j--;
                    }
                    maiores[j + 1] = nota;
                    count++;
                } else if (nota > maiores[0]) {
                    int j = 0;
                    while (j < K - 1 && maiores[j + 1] < nota) {
                        maiores[j] = maiores[j + 1];
                        j++;
                    }
                    maiores[j] = nota;
                }
            }
            long soma = 0;
            for (int i = 0; i < K; i++) {
                soma += maiores[i];
            }
            System.out.println(soma);
        }
        sc.close();
    }
}
