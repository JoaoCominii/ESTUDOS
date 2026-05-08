import java.util.*;

public class Godofor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        String vencedor = "";
        int maxPoder = -1, maxMatou = -1, minMorreu = 101;
        for (int i = 0; i < N; i++) {
            String nome = sc.next();
            int poder = sc.nextInt();
            int matou = sc.nextInt();
            int morreu = sc.nextInt();

            boolean melhor = false;
            if (poder > maxPoder) {
                melhor = true;
            } else if (poder == maxPoder) {
                if (matou > maxMatou) {
                    melhor = true;
                } else if (matou == maxMatou) {
                    if (morreu < minMorreu) {
                        melhor = true;
                    } else if (morreu == minMorreu) {
                        if (nome.compareTo(vencedor) < 0) {
                            melhor = true;
                        }
                    }
                }
            }
            if (melhor) {
                vencedor = nome;
                maxPoder = poder;
                maxMatou = matou;
                minMorreu = morreu;
            }
        }
        System.out.println(vencedor);
    }
}
