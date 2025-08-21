import java.util.Scanner;

public class JogandoCartasFora {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) { 
            int N = scanner.nextInt();            
            if (N == 0) break; // Interrompe o loop quando N for 0

            // Criar a pilha
            int[] pilha = new int[N];
            for (int i = 0; i < N; i++) {
                pilha[i] = i + 1; // Preencher a pilha com os números de 1 a N
            }

            StringBuilder descartadas = new StringBuilder();
            int inicio = 0; // Topo da pilha
            int fim = N; // Base da pilha (não deve ultrapassar N)

            // Operações na pilha
            while (fim - inicio > 1) { // Enquanto houver pelo menos 2 cartas
                // Adicionar carta descartada
                if (descartadas.length() > 0) {
                    descartadas.append(", ");
                }
                descartadas.append(pilha[inicio]);
                inicio++; // Remove a carta do topo

                // Mover a próxima carta para a base (ajuste circular no índice)
                pilha[inicio - 1] = pilha[inicio];
                fim--; // Corrigir contagem de fim para simular rotação circular
            }

            // Última carta restante
            int ultimaCarta = pilha[inicio];

            // Escrever o resultado para este valor de N
            System.out.println("Discarded cards: " + descartadas.toString());
            System.out.println("Remaining card: " + ultimaCarta);
        }

        scanner.close();
    }
}
