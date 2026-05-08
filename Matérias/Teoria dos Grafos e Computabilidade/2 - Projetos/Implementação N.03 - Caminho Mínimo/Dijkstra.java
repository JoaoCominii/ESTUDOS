import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
 * Algoritmo de Dijkstra para grafo direcionado com pesos positivos.
 *
 * Fontes de referência para a lógica do pseudocódigo utilizada:
 * 1) Cormen, T. H.; Leiserson, C. E.; Rivest, R. L.; Stein, C.
 *    Introduction to Algorithms (CLRS), 3rd/4th ed.
 *    - Seção 24.3: Dijkstra's algorithm
 *    - Base conceitual para relaxamento, fila de prioridade e predecessores.
 *
 * 2) Dijkstra, E. W. (1959).
 *    A note on two problems in connexion with graphs.
 *    Numerische Mathematik, 1, 269-271.
 *    DOI: https://doi.org/10.1007/BF01386390
 *
 * ADAPTAÇÃO FEITA NESTA IMPLEMENTAÇÃO:
 * - O algoritmo original encontra o menor caminho por peso total.
 * - Nesta versão, quando houver mais de um caminho com o mesmo peso mínimo,
 *   o critério de desempate é o menor número de arestas.
 * - Para isso, cada estado na fila de prioridade carrega o par:
 *   (distancia_total, quantidade_de_arestas).
 *
 * FORMATO DE ENTRADA ESPERADO:
 * - Primeira linha: n m
 * - Próximas m linhas: u v peso
 * - Grafo direcionado, vertices de 1 a n.
 *
 * USO:
 * - O programa pergunta o arquivo de entrada e os vertices de origem e destino.
 */
public class Dijkstra {
    private static final double INF = Double.POSITIVE_INFINITY;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo de entrada: ");
        String nomeArquivo = scanner.nextLine();

        System.out.print("Digite o numero do vertice de origem: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida para o vertice de origem.");
            scanner.close();
            return;
        }
        int origem = scanner.nextInt();

        System.out.print("Digite o numero do vertice de destino: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida para o vertice de destino.");
            scanner.close();
            return;
        }
        int destino = scanner.nextInt();
        scanner.close();

        try {
            Graph graph = readGraph(nomeArquivo);

            if (origem < 1 || origem > graph.n || destino < 1 || destino > graph.n) {
                System.out.println("Vertice invalido. O grafo possui vertices de 1 a " + graph.n + ".");
                return;
            }

            Result result = shortestPath(graph, origem, destino);
            printResult(result, origem, destino);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato no arquivo de entrada.");
        }
    }

    static Result shortestPath(Graph graph, int origem, int destino) {
        double[] dist = new double[graph.n + 1];
        int[] edgeCount = new int[graph.n + 1];
        int[] parent = new int[graph.n + 1];

        for (int i = 0; i <= graph.n; i++) {
            dist[i] = INF;
            edgeCount[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        PriorityQueue<State> pq = new PriorityQueue<>();
        dist[origem] = 0.0;
        edgeCount[origem] = 0;
        pq.add(new State(origem, 0.0, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            int u = current.vertex;

            if (isStale(current, dist, edgeCount)) {
                continue;
            }

            if (u == destino) {
                break;
            }

            List<Edge> neighbors = graph.adj.get(u);
            for (int i = 0; i < neighbors.size(); i++) {
                Edge edge = neighbors.get(i);
                int v = edge.to;
                double newDist = dist[u] + edge.weight;
                int newEdges = edgeCount[u] + 1;

                if (isBetter(newDist, newEdges, dist[v], edgeCount[v])) {
                    dist[v] = newDist;
                    edgeCount[v] = newEdges;
                    parent[v] = u;
                    pq.add(new State(v, newDist, newEdges));
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        if (Double.isInfinite(dist[destino])) {
            return new Result(false, INF, Integer.MAX_VALUE, path);
        }

        int cursor = destino;
        while (cursor != -1) {
            path.add(cursor);
            cursor = parent[cursor];
        }
        Collections.reverse(path);

        return new Result(true, dist[destino], edgeCount[destino], path);
    }

    private static boolean isStale(State state, double[] dist, int[] edgeCount) {
        return Math.abs(state.distance - dist[state.vertex]) > 1e-12 || state.edges != edgeCount[state.vertex];
    }

    private static boolean isBetter(double newDist, int newEdges, double oldDist, int oldEdges) {
        if (newDist < oldDist - 1e-12) {
            return true;
        }
        if (Math.abs(newDist - oldDist) <= 1e-12 && newEdges < oldEdges) {
            return true;
        }
        return false;
    }

    static Graph readGraph(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String firstLine = nextNonEmptyLine(br);
            if (firstLine == null) {
                throw new IOException("arquivo vazio.");
            }

            String[] header = firstLine.trim().split("\\s+");
            if (header.length < 2) {
                throw new IOException("a primeira linha deve conter n e m.");
            }

            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);
            if (n <= 0) {
                throw new IOException("numero de vertices invalido.");
            }
            if (m < 0) {
                throw new IOException("numero de arestas invalido.");
            }

            Graph graph = new Graph(n);
            int edgesRead = 0;

            while (edgesRead < m) {
                String line = nextNonEmptyLine(br);
                if (line == null) {
                    throw new IOException("esperadas " + m + " arestas, mas o arquivo terminou antes.");
                }

                String[] parts = line.trim().split("\\s+");
                if (parts.length < 3) {
                    throw new IOException("cada aresta deve conter u v peso.");
                }

                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                double weight = Double.parseDouble(parts[2]);

                if (u < 1 || u > n || v < 1 || v > n) {
                    throw new IOException("vertice fora do intervalo na aresta: " + u + " " + v);
                }
                if (weight <= 0) {
                    throw new IOException("os pesos devem ser positivos.");
                }

                graph.adj.get(u).add(new Edge(v, weight));
                edgesRead++;
            }

            for (int i = 1; i <= n; i++) {
                graph.adj.get(i).sort(Comparator
                        .comparingInt((Edge e) -> e.to)
                        .thenComparingDouble(e -> e.weight));
            }

            return graph;
        }
    }

    private static String nextNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line;
            }
        }
        return null;
    }

    private static void printResult(Result result, int origem, int destino) {
        if (!result.found) {
            System.out.println("Nao existe caminho de " + origem + " ate " + destino + ".");
            return;
        }

        System.out.println("Caminho minimo encontrado de " + origem + " ate " + destino + ":");
        System.out.println("Comprimento total: " + formatNumber(result.distance));
        System.out.println("Quantidade de arestas: " + result.edges);
        System.out.println("Vertices do caminho:");
        for (int i = 0; i < result.path.size(); i++) {
            if (i > 0) {
                System.out.print(" -> ");
            }
            System.out.print(result.path.get(i));
        }
        System.out.println();
    }

    private static String formatNumber(double value) {
        if (Double.isInfinite(value)) {
            return "INF";
        }
        double rounded = Math.rint(value);
        if (Math.abs(value - rounded) < 1e-12) {
            return Long.toString((long) rounded);
        }
        String text = String.format(Locale.US, "%.10f", value);
        while (text.contains(".") && (text.endsWith("0") || text.endsWith("."))) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    static class Graph {
        final int n;
        final List<List<Edge>> adj;

        Graph(int n) {
            this.n = n;
            this.adj = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) {
                this.adj.add(new ArrayList<>());
            }
        }
    }

    private static class Edge {
        final int to;
        final double weight;

        Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    private static class State implements Comparable<State> {
        final int vertex;
        final double distance;
        final int edges;

        State(int vertex, double distance, int edges) {
            this.vertex = vertex;
            this.distance = distance;
            this.edges = edges;
        }

        @Override
        public int compareTo(State other) {
            int byDistance = Double.compare(this.distance, other.distance);
            if (byDistance != 0) {
                return byDistance;
            }
            int byEdges = Integer.compare(this.edges, other.edges);
            if (byEdges != 0) {
                return byEdges;
            }
            return Integer.compare(this.vertex, other.vertex);
        }
    }

    static class Result {
        final boolean found;
        final double distance;
        final int edges;
        final List<Integer> path;

        Result(boolean found, double distance, int edges, List<Integer> path) {
            this.found = found;
            this.distance = distance;
            this.edges = edges;
            this.path = path;
        }
    }
}
