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
 * Gerador de instancias para o problema de caminhos disjuntos em arestas.
 *
 * Fontes de referencia para a estrategia geral de geracao:
 *
 * 1) Modelo de grafos aleatorios de Erdos-Renyi:
 *    Erdos, P.; Renyi, A. (1959/1960).
 *    On Random Graphs.
 *    Base conceitual para a criacao de grafos com arestas aleatorias.
 *
 * 2) Conceito de redes em camadas para fluxo maximo:
 *    Estrutura pratica muito usada em problemas de caminhos disjuntos,
 *    pois facilita a criacao de multiplas rotas entre origem e destino.
 *
 * DEFINICAO DOS TIPOS GERADOS:
 * - Tipo 1: grafo esparso aleatorio direcionado
 * - Tipo 2: grafo em camadas direcionado
 *
 * FORMATO DE SAIDA:
 * - Uma pasta por tipo: grafo-esparso ou grafo-camadas
 * - Quatro arquivos por pasta, com os tamanhos:
 *   100, 1000, 10000 e 50000 vertices
 * - Formato de cada arquivo:
 *   primeira linha: n m
 *   proximas m linhas: u v
 *
 * OBSERVACAO:
 * - O programa garante pelo menos um caminho de 1 ate n.
 * - A origem e o destino serao informados apenas na execucao do FluxoMaximo.
 */
public class GeradorDeGrafosFluxo {
    private static final int[] TAMANHOS = {100, 1000, 10000, 50000};
    private static final long BASE_SEED = 20260601L;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de grafo a ser gerado:");
        System.out.println("1 - Esparso aleatorio direcionado");
        System.out.println("2 - Em camadas direcionado");
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
            kind = GraphKind.LAYERED;
            folderName = "grafo-camadas";
        } else {
            System.out.println("Opcao invalida. Use 1 para esparso ou 2 para camadas.");
            return;
        }

        File folder = new File(folderName);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta de saida: " + folder.getAbsolutePath());
        }

        System.out.println("Gerando instancias em: " + folder.getAbsolutePath());

        for (int n : TAMANHOS) {
            long seed = BASE_SEED + n + (kind == GraphKind.SPARSE ? 1000L : 2000L);
            GraphData graph = buildGraph(n, kind, seed);
            File out = new File(folder, String.format(Locale.ROOT, "%s-%d.txt", folderName, n));
            writeGraph(out, graph);
            System.out.println("Gerado: " + out.getAbsolutePath() + " | vertices=" + n + " | arestas=" + graph.edgeCount);
        }
    }

    private static GraphData buildGraph(int n, GraphKind kind, long seed) {
        Random random = new Random(seed);

        if (kind == GraphKind.SPARSE) {
            return buildSparseGraph(n, random);
        }
        return buildLayeredGraph(n, random);
    }

    private static GraphData buildSparseGraph(int n, Random random) {
        int targetEdges = Math.max(n - 1, (2 * n) - 2);
        GraphData graph = new GraphData(n, targetEdges);
        Set<Long> used = new HashSet<>(Math.max(16, targetEdges * 2));

        for (int u = 1; u < n; u++) {
            addEdge(graph, used, u, u + 1);
        }

        while (graph.edgeCount < targetEdges) {
            int u = 1 + random.nextInt(n);
            int v = 1 + random.nextInt(n);
            if (u == v) {
                continue;
            }
            addEdge(graph, used, u, v);
        }

        return graph;
    }

    private static GraphData buildLayeredGraph(int n, Random random) {
        int targetEdges = estimateLayeredEdges(n);
        GraphData graph = new GraphData(n, targetEdges);
        Set<Long> used = new HashSet<>(Math.max(16, targetEdges * 2));

        int source = 1;
        int sink = n;

        if (n == 1) {
            return graph;
        }

        int internal = Math.max(0, n - 2);
        int layers = Math.max(2, (int) Math.sqrt(Math.max(2, internal)));
        int baseLayerSize = layers == 0 ? 0 : internal / layers;
        int remainder = layers == 0 ? 0 : internal % layers;

        int[] layerStart = new int[layers];
        int[] layerSize = new int[layers];

        int current = 2;
        for (int i = 0; i < layers; i++) {
            layerStart[i] = current;
            layerSize[i] = baseLayerSize + (i < remainder ? 1 : 0);
            current += layerSize[i];
        }

        if (internal == 0) {
            addEdge(graph, used, source, sink);
            return graph;
        }

        int previousRepresentative = source;
        for (int i = 0; i < layers; i++) {
            if (layerSize[i] == 0) {
                continue;
            }
            int representative = layerStart[i];
            addEdge(graph, used, previousRepresentative, representative);
            previousRepresentative = representative;
        }
        addEdge(graph, used, previousRepresentative, sink);

        for (int i = 0; i < layers - 1; i++) {
            for (int u = layerStart[i]; u < layerStart[i] + layerSize[i]; u++) {
                int maxTargets = Math.min(layerSize[i + 1], 3);
                for (int j = 0; j < maxTargets; j++) {
                    int offset = (j + random.nextInt(layerSize[i + 1])) % layerSize[i + 1];
                    int v = layerStart[i + 1] + offset;
                    addEdge(graph, used, u, v);
                }
            }
        }

        if (layers > 0 && layerSize[0] > 0) {
            for (int i = 0; i < Math.min(layerSize[0], 4); i++) {
                addEdge(graph, used, source, layerStart[0] + i);
            }
        }

        if (layers > 0) {
            int last = layers - 1;
            for (int i = 0; i < Math.min(layerSize[last], 4); i++) {
                addEdge(graph, used, layerStart[last] + i, sink);
            }
        }

        while (graph.edgeCount < targetEdges) {
            int fromLayer = random.nextInt(layers);
            int toLayer = Math.min(layers - 1, fromLayer + 1 + random.nextInt(Math.max(1, layers - fromLayer)));
            if (fromLayer == toLayer) {
                continue;
            }

            int u = (fromLayer == 0) ? source : layerStart[fromLayer] + random.nextInt(layerSize[fromLayer]);
            int v;
            if (toLayer == layers - 1 && random.nextInt(4) == 0) {
                v = sink;
            } else {
                v = layerStart[toLayer] + random.nextInt(layerSize[toLayer]);
            }

            if (u != v) {
                addEdge(graph, used, u, v);
            }
        }

        return graph;
    }

    private static int estimateLayeredEdges(int n) {
        if (n <= 2) {
            return Math.max(1, n - 1);
        }
        int internal = n - 2;
        int approx = (int) (internal * 3L);
        return Math.max(n - 1, approx);
    }

    private static void addEdge(GraphData graph, Set<Long> used, int u, int v) {
        long key = encode(u, v);
        if (used.add(key)) {
            graph.addEdge(u, v);
        }
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
                bw.newLine();
            }
        }
    }

    private enum GraphKind {
        SPARSE,
        LAYERED
    }

    private static class GraphData {
        final int n;
        final int[] from;
        final int[] to;
        int edgeCount;

        GraphData(int n, int capacity) {
            this.n = n;
            this.from = new int[capacity];
            this.to = new int[capacity];
            this.edgeCount = 0;
        }

        void addEdge(int u, int v) {
            from[edgeCount] = u;
            to[edgeCount] = v;
            edgeCount++;
        }
    }
}
