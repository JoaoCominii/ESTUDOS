
import java.util.Scanner;

public class ArrayHash {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int L;
        String palavra;
        for(int i = 0; i < N; i++)
        {
            int soma = 0;
            L = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline
            for(int j = 0; j < L; j++)
            {
                palavra = scanner.nextLine();
                    for(int k = 0; k < palavra.length(); k++) {
                        soma += (palavra.charAt(k) - 'A' + j + k);
                    }
                }
                System.out.println(soma);
            }
            scanner.close();
        }
    }

