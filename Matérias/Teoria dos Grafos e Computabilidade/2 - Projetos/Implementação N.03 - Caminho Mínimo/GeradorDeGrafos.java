import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/*
 * Gerador de instancias para o trabalho de caminho minimo.
 *
 * Fontes de referencia para a estrategia geral de geracao aleatoria:
 * 1) Modelo de grafos aleatorios de Erdos-Renyi:
 *    Erdos, P.; Renyi, A. (1959/1960).
 *    On Random Graphs.
 *    Base conceitual para a criacao de grafos com arestas geradas aleatoriamente.
 *
 * 2) Conceito de grafo com backbone garantido:
 *    Nao e um pseudocodigo classico de livro, mas uma adaptacao pratica de engenharia
 *    para assegurar que exista pelo menos um caminho de 1 ate n em todas as instancias.
 *    Essa tecnica adiciona uma cadeia base 1 -> 2 -> ... -> n e depois insere arestas
 *    aleatorias extras para aumentar a complexidade e a variabilidade do grafo.
 *
 * DEFINICAO DAS CLASSES GERADAS:
 * - Tipo 1: grafo esparso aleatorio direcionado
 * - Tipo 2: grafo denso aleatorio direcionado
 *
 * FORMATO DE SAIDA:
 * - Uma pasta por tipo: grafo-esparso ou grafo-denso
 * - Quatro arquivos por pasta, com os tamanhos:
 *   100, 1000, 10000 e 50000 vertices
 * - Formato de cada arquivo:
 *   primeira linha: n m
 *   linhas seguintes: u v peso
 *
 * OBSERVACAO:
 * - Todos os pesos sao inteiros positivos.
 * - Vertices sao numerados de 1 a n.
 * - Todos os grafos gerados possuem pelo menos um caminho de 1 ate n.
 */
public class GeradorDeGrafos {
    private static final int[] TAMANHOS = {100, 1000, 10000, 50000};
    private static final int PESO_MIN = 1;
    private static final int PESO_MAX = 100;
    private static final long BASE_SEED = 20260508L;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de grafo a ser gerado:");
        System.out.println("1 - Esparso aleatorio direcionado");
        System.out.println("2 - Denso aleatorio direcionado");
        System.out.print("Opcao: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida.");
            scanner.close();
            return;
        }

        int opcao = scanner.nextInt();
        scanner.close();

        try {
            generateAll(opcao);
        } catch (IOException e) {
            System.out.println("Erro ao gerar instancias: " + e.getMessage());
        }
    }

    private static void generateAll(int opcao) throws IOException {
        GraphKind kind;
        String folderName;

        if (opcao == 1) {
            kind = GraphKind.SPARSE;
            folderName = "grafo-esparso";
        } else if (opcao == 2) {
            kind = GraphKind.DENSE;
            folderName = "grafo-denso";
        } else {
            System.out.println("Opcao invalida. Use 1 para esparso ou 2 para denso.");
            return;
        }

        File folder = new File(folderName);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("nao foi possivel criar a pasta de saida: " + folder.getAbsolutePath());
        }

        System.out.println("Gerando instancias na pasta: " + folder.getAbsolutePath());
        for (int n : TAMANHOS) {
            long seed = BASE_SEED + (kind == GraphKind.SPARSE ? 10_000L : 20_000L) + n;
            GraphData graph = buildGraph(n, kind, seed);
            File out = new File(folder, String.format(Locale.ROOT, "%s-%d.txt", folderName, n));
            writeGraph(out, graph);
            System.out.println("Gerado: " + out.getAbsolutePath() + " | vertices=" + n + " | arestas=" + graph.edgeCount);
        }
    }

    private static GraphData buildGraph(int n, GraphKind kind, long seed) {
        Random random = new Random(seed);
        int targetEdges = targetEdges(n, kind);

        GraphData graph = new GraphData(n, targetEdges);
        Set<Long> used = new HashSet<>(Math.max(16, targetEdges * 2));

        // Backbone garantido: 1 -> 2 -> 3 -> ... -> n
        for (int u = 1; u < n; u++) {
            addEdge(graph, used, u, u + 1, randomWeight(random));
        }

        // Arestas extras para criar variacao estrutural.
        while (graph.edgeCount < targetEdges) {
            int u = 1 + random.nextInt(n);
            int v = 1 + random.nextInt(n);
            if (u == v) {
                continue;
            }
            addEdge(graph, used, u, v, randomWeight(random));
        }

        return graph;
    }

    private static int targetEdges(int n, GraphKind kind) {
        long base = n - 1L;
        long extra;
        if (kind == GraphKind.SPARSE) {
            // Aproximadamente 2 arestas por vertice, preservando a classificacao de grafo esparso.
            extra = n;
        } else {
            // Mais pesado que o caso esparso, mas ainda viavel para execucao e armazenamento.
            // Aproximadamente 8 arestas por vertice.
            extra = 7L * n;
        }
        long total = base + extra;
        long maxPossible = (long) n * (n - 1L);
        if (total > maxPossible) {
            total = maxPossible;
        }
        return (int) total;
    }

    private static void addEdge(GraphData graph, Set<Long> used, int u, int v, int weight) {
        long key = encode(u, v);
        if (used.add(key)) {
            graph.addEdge(u, v, weight);
        }
    }

    private static int randomWeight(Random random) {
        return PESO_MIN + random.nextInt(PESO_MAX - PESO_MIN + 1);
    }

    private static long encode(int u, int v) {
        return (((long) u) << 32) ^ (v & 0xffffffffL);
    }

    private static void writeGraph(File out, GraphData graph) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            bw.write(Integer.toString(graph.n));
            bw.write(' ');
            bw.write(Integer.toString(graph.edgeCount));
            bw.newLine();

            for (int i = 0; i < graph.edgeCount; i++) {
                bw.write(Integer.toString(graph.from[i]));
                bw.write(' ');
                bw.write(Integer.toString(graph.to[i]));
                bw.write(' ');
                bw.write(Integer.toString(graph.weight[i]));
                bw.newLine();
            }
        }
    }

    private enum GraphKind {
        SPARSE,
        DENSE
    }

    private static class GraphData {
        final int n;
        final int[] from;
        final int[] to;
        final int[] weight;
        int edgeCount;

        GraphData(int n, int capacity) {
            this.n = n;
            this.from = new int[capacity];
            this.to = new int[capacity];
            this.weight = new int[capacity];
            this.edgeCount = 0;
        }

        void addEdge(int u, int v, int w) {
            from[edgeCount] = u;
            to[edgeCount] = v;
            weight[edgeCount] = w;
            edgeCount++;
        }
    }
}
