import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*
 * Programa principal para execução do método de Fleury com escolha de estratégia de pontes.
 *
 * Fontes de referência:
 * - Rosen, K. H. Discrete Mathematics and Its Applications (7th ed.), Section 10.5.
 * - Fleury's algorithm (descrição): https://en.wikipedia.org/wiki/Fleury%27s_algorithm
 * - Tarjan (1974): https://doi.org/10.1016/0020-0190(74)90003-9
 *
 * OBSERVAÇÃO SOBRE A INTERFACE:
 * Este programa implementa explicitamente o requisito solicitado no trabalho:
 * - opção 1: identificação de pontes por estratégia Naive
 * - opção 2: identificação de pontes por estratégia Tarjan
 */
public class FleuryMain {

    public static void main(String[] args) {
        if (args.length >= 2) {
            String arquivo = args[0].trim();
            int opcao;
            try {
                opcao = Integer.parseInt(args[1].trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida. Use 1 (Naive) ou 2 (Tarjan).");
                return;
            }

            FleuryEngine.BridgeStrategy strategy = parseStrategy(opcao);
            if (strategy == null) {
                System.out.println("Opcao invalida. Use 1 (Naive) ou 2 (Tarjan).");
                return;
            }

            try {
                FleuryEngine.FleuryResult result = FleuryEngine.solveFromFile(arquivo, strategy);
                printResult(result, strategy);
            } catch (IOException e) {
                System.out.println("Erro ao ler arquivo: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro de formato: " + e.getMessage());
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o caminho do arquivo de grafo: ");
        String arquivo = scanner.nextLine().trim();

        System.out.println("Escolha a estrategia de identificacao de pontes:");
        System.out.println("1 - Naive");
        System.out.println("2 - Tarjan");
        System.out.print("Opcao: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Opcao invalida.");
            scanner.close();
            return;
        }
        int opcao = scanner.nextInt();
        scanner.close();

        FleuryEngine.BridgeStrategy strategy = parseStrategy(opcao);
        if (strategy == null) {
            System.out.println("Opcao invalida. Use 1 (Naive) ou 2 (Tarjan).");
            return;
        }

        try {
            FleuryEngine.FleuryResult result = FleuryEngine.solveFromFile(arquivo, strategy);
            printResult(result, strategy);
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro de formato: " + e.getMessage());
        }
    }

    private static FleuryEngine.BridgeStrategy parseStrategy(int option) {
        if (option == 1) {
            return FleuryEngine.BridgeStrategy.NAIVE;
        }
        if (option == 2) {
            return FleuryEngine.BridgeStrategy.TARJAN;
        }
        return null;
    }

    private static void printResult(FleuryEngine.FleuryResult result, FleuryEngine.BridgeStrategy strategy) {
        System.out.println();
        System.out.println("Estrategia de ponte: " + (strategy == FleuryEngine.BridgeStrategy.NAIVE ? "Naive" : "Tarjan"));
        System.out.println("Classificacao do grafo: " + result.classification);
        System.out.println("Tempo de execucao (ms): " + String.format("%.3f", result.elapsedMs));

        if (!result.hasEulerTrail) {
            System.out.println("Nao foi encontrado caminho/trilha euleriana.");
            System.out.println("Motivo: " + result.message);
            return;
        }

        System.out.println("Trilha/Caminho euleriano encontrado.");
        System.out.println("Numero de arestas percorridas: " + (result.trail.size() - 1));

        if (result.trail.size() <= 200) {
            System.out.println("Sequencia de vertices:");
            printFullTrail(result.trail);
        } else {
            System.out.println("Sequencia muito grande; exibindo prefixo e sufixo:");
            printTrailPreview(result.trail, 20);
        }
    }

    private static void printFullTrail(List<Integer> trail) {
        for (int i = 0; i < trail.size(); i++) {
            if (i > 0) {
                System.out.print(" -> ");
            }
            System.out.print(trail.get(i));
        }
        System.out.println();
    }

    private static void printTrailPreview(List<Integer> trail, int k) {
        for (int i = 0; i < Math.min(k, trail.size()); i++) {
            if (i > 0) {
                System.out.print(" -> ");
            }
            System.out.print(trail.get(i));
        }
        System.out.println(" -> ... -> ");

        int start = Math.max(0, trail.size() - k);
        for (int i = start; i < trail.size(); i++) {
            if (i > start) {
                System.out.print(" -> ");
            }
            System.out.print(trail.get(i));
        }
        System.out.println();
    }
}