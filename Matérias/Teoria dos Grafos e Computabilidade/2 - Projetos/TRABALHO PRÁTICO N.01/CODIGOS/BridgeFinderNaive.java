import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Identificação de pontes em grafo simples não-direcionado pelo método naïve.
 *
 * Fontes de referência:
 * 1) Definição formal de ponte e algoritmo eficiente de referência (Tarjan):
 *    Tarjan, R. E. (1974). A note on finding the bridges of a graph.
 *    Information Processing Letters, 2(6), 160-161.
 *    DOI: https://doi.org/10.1016/0020-0190(74)90003-9
 *
 * 2) Busca em profundidade para teste de conectividade/componente conexa:
 *    Cormen, T. H.; Leiserson, C. E.; Rivest, R. L.; Stein, C.
 *    Introduction to Algorithms (3rd ed.), Section 22.3 (Depth-first search).
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - Este código usa a estratégia naïve solicitada no trabalho:
 *   para cada aresta e=(u,v), recalcula o número de componentes conexas ignorando e.
 * - Se o número de componentes aumenta após remover e, então e é ponte.
 * - O DFS foi implementado de forma iterativa para evitar StackOverflowError em instâncias grandes.
 */
public class BridgeFinderNaive {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java BridgeFinderNaive <arquivo_entrada> [arquivo_saida]");
            return;
        }

        String arquivoEntrada = args[0];
        String arquivoSaida = args.length >= 2 ? args[1] : null;

        try {
            Graph graph = Graph.readFromFile(arquivoEntrada);

            long inicio = System.nanoTime();
            List<Edge> bridges = findBridgesNaive(graph);
            long fim = System.nanoTime();

            double tempoMs = (fim - inicio) / 1_000_000.0;
            printResult(graph, bridges, tempoMs, arquivoSaida);
        } catch (IOException e) {
            System.out.println("Erro ao processar arquivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro de formato: " + e.getMessage());
        }
    }

    private static List<Edge> findBridgesNaive(Graph graph) {
        int baseComponents = countComponents(graph, -1);
        List<Edge> bridges = new ArrayList<>();

        for (int edgeIndex = 0; edgeIndex < graph.edges.size(); edgeIndex++) {
            int componentsAfterRemoval = countComponents(graph, edgeIndex);
            if (componentsAfterRemoval > baseComponents) {
                bridges.add(graph.edges.get(edgeIndex));
            }
        }

        return bridges;
    }

    private static int countComponents(Graph graph, int ignoredEdgeIndex) {
        boolean[] visited = new boolean[graph.n + 1];
        int components = 0;

        IntStack stack = new IntStack(Math.max(4, graph.n / 8));

        for (int start = 1; start <= graph.n; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            visited[start] = true;
            stack.push(start);

            while (!stack.isEmpty()) {
                int u = stack.pop();
                List<AdjEdge> neighbors = graph.adj[u];

                for (int i = 0; i < neighbors.size(); i++) {
                    AdjEdge adjEdge = neighbors.get(i);
                    if (adjEdge.edgeIndex == ignoredEdgeIndex) {
                        continue;
                    }

                    int v = adjEdge.to;
                    if (!visited[v]) {
                        visited[v] = true;
                        stack.push(v);
                    }
                }
            }
        }

        return components;
    }

    private static void printResult(Graph graph, List<Edge> bridges, double tempoMs, String arquivoSaida) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(graph.n).append('\n');
        sb.append("Arestas: ").append(graph.edges.size()).append('\n');
        sb.append("Pontes encontradas (naive): ").append(bridges.size()).append('\n');
        sb.append("Tempo de execucao (ms): ").append(String.format("%.3f", tempoMs)).append('\n');
        sb.append('\n');

        for (Edge edge : bridges) {
            sb.append(edge.u).append(' ').append(edge.v).append('\n');
        }

        String result = sb.toString();
        System.out.print(result);

        if (arquivoSaida != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSaida))) {
                bw.write(result);
            }
        }
    }

    private static class Graph {
        final int n;
        final List<Edge> edges;
        final List<AdjEdge>[] adj;

        @SuppressWarnings("unchecked")
        Graph(int n) {
            this.n = n;
            this.edges = new ArrayList<>();
            this.adj = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                this.adj[i] = new ArrayList<>();
            }
        }

        void addEdge(int u, int v) {
            int edgeIndex = edges.size();
            edges.add(new Edge(u, v));
            adj[u].add(new AdjEdge(v, edgeIndex));
            adj[v].add(new AdjEdge(u, edgeIndex));
        }

        static Graph readFromFile(String path) throws IOException {
            FastScanner fs = new FastScanner(path);

            int n = fs.nextInt();
            int m = fs.nextInt();
            if (n <= 0) {
                throw new IllegalArgumentException("Numero de vertices invalido: " + n);
            }
            if (m < 0) {
                throw new IllegalArgumentException("Numero de arestas invalido: " + m);
            }

            Graph g = new Graph(n);
            for (int i = 0; i < m; i++) {
                int u = fs.nextInt();
                int v = fs.nextInt();

                if (u < 1 || u > n || v < 1 || v > n) {
                    throw new IllegalArgumentException("Aresta fora do intervalo de vertices: " + u + " " + v);
                }
                if (u == v) {
                    throw new IllegalArgumentException("Laco nao permitido em grafo simples: " + u + " " + v);
                }

                g.addEdge(u, v);
            }

            fs.close();
            return g;
        }
    }

    private static class Edge {
        final int u;
        final int v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    private static class AdjEdge {
        final int to;
        final int edgeIndex;

        AdjEdge(int to, int edgeIndex) {
            this.to = to;
            this.edgeIndex = edgeIndex;
        }
    }

    private static class IntStack {
        private int[] data;
        private int size;

        IntStack(int initialCapacity) {
            int cap = Math.max(2, initialCapacity);
            this.data = new int[cap];
            this.size = 0;
        }

        void push(int value) {
            if (size == data.length) {
                int[] next = new int[data.length * 2];
                System.arraycopy(data, 0, next, 0, data.length);
                data = next;
            }
            data[size++] = value;
        }

        int pop() {
            return data[--size];
        }

        boolean isEmpty() {
            return size == 0;
        }
    }

    private static class FastScanner {
        private final BufferedInputStream in;
        private final byte[] buffer;
        private int ptr;
        private int len;

        FastScanner(String filePath) throws IOException {
            this.in = new BufferedInputStream(new java.io.FileInputStream(filePath));
            this.buffer = new byte[1 << 16];
            this.ptr = 0;
            this.len = 0;
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
                if (c == -1) {
                    throw new IllegalArgumentException("Fim de arquivo inesperado durante leitura de inteiro.");
                }
            } while (c <= ' ');

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            int value = 0;
            while (c > ' ') {
                if (c < '0' || c > '9') {
                    throw new IllegalArgumentException("Caracter invalido no inteiro: " + (char) c);
                }
                value = value * 10 + (c - '0');
                c = read();
            }

            return value * sign;
        }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        void close() throws IOException {
            in.close();
        }
    }
}