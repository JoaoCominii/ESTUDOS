import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * Determinacao de caminhos disjuntos em arestas via fluxo maximo.
 *
 * Fontes de referencia para os pseudocodigos e a estrutura geral:
 *
 * 1) CLRS - Introduction to Algorithms (Cormen, Leiserson, Rivest, Stein)
 *    Chapter 26: Maximum Flow
 *    - Ford-Fulkerson Method
 *    - Edmonds-Karp (base conceitual de fluxo por caminhos aumentantes)
 *    - Max-Flow Min-Cut Theorem
 *
 * 2) Dinic, E. A. (1970).
 *    Algorithm for Solution of a Problem of Maximum Flow in Networks with Power Estimation.
 *    - Base para a implementacao do algoritmo Dinic com grafo residual e BFS/DFS.
 *
 * 3) Conceito de decomposicao de fluxo inteiro em caminhos s-t
 *    - Consequencia do teorema de integralidade do fluxo maximo em redes com capacidades inteiras.
 *    - Utilizado aqui para extrair os caminhos disjuntos em arestas a partir do fluxo final.
 *
 * OBSERVACOES:
 * - Cada aresta do grafo eh tratada com capacidade 1.
 * - Logo, o valor do fluxo maximo corresponde ao numero de caminhos disjuntos em arestas.
 * - O arquivo de entrada deve ter o formato:
 *     primeira linha: n m
 *     proximas m linhas: u v [peso opcional]
 *     linha final opcional: origem destino
 * - Se origem e destino nao estiverem no arquivo, o programa solicita pelo terminal.
 * - O peso da aresta, se existir, eh ignorado nesta tarefa, pois o objetivo eh apenas
 *   contar caminhos disjuntos em arestas.
 */
public class FluxoMaximo {

    public static class Result {
        public final int maxFlow;
        public final List<List<Integer>> paths;

        Result(int maxFlow, List<List<Integer>> paths) {
            this.maxFlow = maxFlow;
            this.paths = paths;
        }
    }

    private static class Edge {
        int to;
        int rev;
        int cap;
        int originalCap;

        Edge(int to, int rev, int cap) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
            this.originalCap = cap;
        }
    }

    private static class Dinic {
        private final int n;
        private final List<List<Edge>> graph;
        private int[] level;
        private int[] it;

        Dinic(int n) {
            this.n = n;
            this.graph = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
        }

        void addEdge(int u, int v, int cap) {
            Edge a = new Edge(v, graph.get(v).size(), cap);
            Edge b = new Edge(u, graph.get(u).size(), 0);
            graph.get(u).add(a);
            graph.get(v).add(b);
        }

        boolean bfs(int s, int t) {
            level = new int[n + 1];
            Arrays.fill(level, -1);
            Deque<Integer> q = new ArrayDeque<>();
            level[s] = 0;
            q.add(s);

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : graph.get(u)) {
                    if (e.cap > 0 && level[e.to] < 0) {
                        level[e.to] = level[u] + 1;
                        q.add(e.to);
                    }
                }
            }
            return level[t] >= 0;
        }

        int dfs(int u, int t, int f) {
            if (u == t) {
                return f;
            }
            for (; it[u] < graph.get(u).size(); it[u]++) {
                Edge e = graph.get(u).get(it[u]);
                if (e.cap > 0 && level[u] + 1 == level[e.to]) {
                    int ret = dfs(e.to, t, Math.min(f, e.cap));
                    if (ret > 0) {
                        e.cap -= ret;
                        graph.get(e.to).get(e.rev).cap += ret;
                        return ret;
                    }
                }
            }
            return 0;
        }

        int maxFlow(int s, int t) {
            int flow = 0;
            while (bfs(s, t)) {
                it = new int[n + 1];
                int f;
                while ((f = dfs(s, t, Integer.MAX_VALUE)) > 0) {
                    flow += f;
                }
            }
            return flow;
        }

        List<List<Edge>> getGraph() {
            return graph;
        }
    }

    private static class InputData {
        int n;
        int m;
        int source = -1;
        int sink = -1;
        List<int[]> edges = new ArrayList<>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo de entrada: ");
        String fileName = scanner.nextLine().trim();

        try {
            InputData input = readInput(fileName);

            if (input.source == -1 || input.sink == -1) {
                System.out.print("Digite o vertice de origem: ");
                input.source = scanner.nextInt();
                System.out.print("Digite o vertice de destino: ");
                input.sink = scanner.nextInt();
            }

            if (input.source < 1 || input.source > input.n || input.sink < 1 || input.sink > input.n) {
                System.out.println("Erro: origem ou destino fora do intervalo valido 1..n.");
                scanner.close();
                return;
            }

            Dinic dinic = new Dinic(input.n);

            for (int[] edge : input.edges) {
                int u = edge[0];
                int v = edge[1];
                dinic.addEdge(u, v, 1);
            }

            int maxFlow = dinic.maxFlow(input.source, input.sink);
            List<List<Integer>> paths = decomposePaths(dinic.getGraph(), input.source, input.sink, maxFlow);

            System.out.println();
            System.out.println("Quantidade de caminhos disjuntos em arestas: " + maxFlow);

            for (int i = 0; i < paths.size(); i++) {
                List<Integer> path = paths.get(i);
                System.out.print("Caminho " + (i + 1) + ": ");
                for (int j = 0; j < path.size(); j++) {
                    if (j > 0) {
                        System.out.print(" -> ");
                    }
                    System.out.print(path.get(j));
                }
                System.out.println();
            }

            if (paths.isEmpty()) {
                System.out.println("Nenhum caminho entre a origem e o destino foi encontrado.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato no arquivo de entrada: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static Result solveFromFile(String fileName, int source, int sink) throws IOException {
        InputData input = readInput(fileName);

        if (source < 1 || source > input.n || sink < 1 || sink > input.n) {
            throw new IllegalArgumentException("origem ou destino fora do intervalo valido 1..n");
        }

        Dinic dinic = new Dinic(input.n);

        for (int[] edge : input.edges) {
            dinic.addEdge(edge[0], edge[1], 1);
        }

        int maxFlow = dinic.maxFlow(source, sink);
        List<List<Integer>> paths = decomposePaths(dinic.getGraph(), source, sink, maxFlow);
        return new Result(maxFlow, paths);
    }

    private static InputData readInput(String fileName) throws IOException {
        InputData input = new InputData();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = nextNonEmptyLine(br);
            if (line == null) {
                throw new IOException("Arquivo vazio.");
            }

            StringTokenizer st = new StringTokenizer(line);
            input.n = Integer.parseInt(st.nextToken());
            input.m = Integer.parseInt(st.nextToken());

            for (int i = 0; i < input.m; i++) {
                line = nextNonEmptyLine(br);
                if (line == null) {
                    throw new IOException("Quantidade de arestas menor que o especificado em m.");
                }

                st = new StringTokenizer(line);
                if (st.countTokens() < 2) {
                    throw new IOException("Linha de aresta invalida: " + line);
                }

                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());

                // Peso opcional: ignorado nesta implementacao.
                input.edges.add(new int[] {u, v});
            }

            line = nextNonEmptyLine(br);
            if (line != null) {
                st = new StringTokenizer(line);
                if (st.countTokens() >= 2) {
                    input.source = Integer.parseInt(st.nextToken());
                    input.sink = Integer.parseInt(st.nextToken());
                }
            }
        }

        return input;
    }

    private static String nextNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return null;
    }

    private static List<List<Integer>> decomposePaths(List<List<Edge>> graph, int source, int sink, int expectedPaths) {
        List<List<Integer>> paths = new ArrayList<>();

        for (int k = 0; k < expectedPaths; k++) {
            int[] parentV = new int[graph.size()];
            int[] parentE = new int[graph.size()];
            Arrays.fill(parentV, -1);
            Arrays.fill(parentE, -1);

            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(source);
            parentV[source] = source;

            while (!queue.isEmpty() && parentV[sink] == -1) {
                int u = queue.poll();

                for (int i = 0; i < graph.get(u).size(); i++) {
                    Edge e = graph.get(u).get(i);

                    // Aqui, consideramos apenas arestas com fluxo positivo
                    // no grafo original, isto e, arestas cuja capacidade original
                    // foi consumida pelo maxflow.
                    if (e.originalCap == 1 && e.cap == 0 && parentV[e.to] == -1) {
                        parentV[e.to] = u;
                        parentE[e.to] = i;
                        queue.add(e.to);
                        if (e.to == sink) {
                            break;
                        }
                    }
                }
            }

            if (parentV[sink] == -1) {
                break;
            }

            List<Integer> path = new ArrayList<>();
            int cur = sink;
            while (cur != source) {
                path.add(cur);
                int prev = parentV[cur];
                int edgeIndex = parentE[cur];
                graph.get(prev).get(edgeIndex).cap = 1; // devolve 1 unidade de fluxo para nao reutilizar
                graph.get(cur).get(graph.get(prev).get(edgeIndex).rev).cap = 0;
                cur = prev;
            }
            path.add(source);
            java.util.Collections.reverse(path);
            paths.add(path);
        }

        return paths;
    }
}