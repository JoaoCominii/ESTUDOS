import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Núcleo de execução do método de Fleury para trilha/caminho euleriano.
 *
 * Fontes de referência:
 * 1) Condição de existência de trilha/circuito euleriano em grafo não-direcionado:
 *    Rosen, K. H. Discrete Mathematics and Its Applications (7th ed.),
 *    Section 10.5 (Euler Circuits and Euler Paths).
 *
 * 2) Algoritmo de Fleury (escolha de arestas evitando pontes quando possível):
 *    Descrição didática (com exemplos):
 *    https://en.wikipedia.org/wiki/Fleury%27s_algorithm
 *
 * 3) Referências de Tarjan utilizadas para estratégia de pontes:
 *    3.1) Tarjan, R. E. (1974). A note on finding the bridges of a graph.
 *         Information Processing Letters, 2(6), 160-161.
 *         DOI: https://doi.org/10.1016/0020-0190(74)90003-9
 *    3.2) Tarjan, R. E. (1972). Depth-first search and linear graph algorithms.
 *         SIAM Journal on Computing, 1(2), 146-160.
 *         Link: https://doi.org/10.1137/0201010
 *         (Seções base: Section 2 - Depth-first search e formulações de low-link)
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - Tarjan foi implementado de forma iterativa para evitar StackOverflowError.
 * - O Fleury possui duas estratégias para ponte: NAIVE e TARJAN.
 *
 * CONFORMIDADE COM AS REFERÊNCIAS:
 * - Para a estratégia TARJAN, a identificação de pontes segue o critério
 *   low[filho] > disc[pai], conforme Tarjan (1974), sobre DFS.
 * - A base de DFS e a natureza linear do processamento seguem o arcabouço
 *   de Tarjan (1972) para algoritmos lineares em grafos.
 * - Adaptação de engenharia: integração da rotina de pontes ao método de Fleury
 *   e implementação iterativa para suportar instâncias grandes.
 */
public class FleuryEngine {

    public enum BridgeStrategy {
        NAIVE,
        TARJAN
    }

    public static class FleuryResult {
        public final boolean hasEulerTrail;
        public final String classification;
        public final List<Integer> trail;
        public final double elapsedMs;
        public final String message;

        FleuryResult(boolean hasEulerTrail, String classification, List<Integer> trail, double elapsedMs, String message) {
            this.hasEulerTrail = hasEulerTrail;
            this.classification = classification;
            this.trail = trail;
            this.elapsedMs = elapsedMs;
            this.message = message;
        }
    }

    public static FleuryResult solveFromFile(String filePath, BridgeStrategy strategy) throws IOException {
        Graph graph = Graph.readFromFile(filePath);
        return solve(graph, strategy);
    }

    public static FleuryResult solve(Graph graph, BridgeStrategy strategy) {
        long startTime = System.nanoTime();

        if (!isConnectedIgnoringIsolated(graph)) {
            double elapsed = elapsedMs(startTime);
            return new FleuryResult(false, "nao-euleriano", new ArrayList<>(), elapsed,
                    "Grafo desconexo (considerando vertices com grau > 0).");
        }

        List<Integer> odd = oddDegreeVertices(graph);
        if (!(odd.size() == 0 || odd.size() == 2)) {
            double elapsed = elapsedMs(startTime);
            return new FleuryResult(false, "nao-euleriano", new ArrayList<>(), elapsed,
                    "Quantidade de vertices de grau impar diferente de 0 e 2.");
        }

        String classification = odd.isEmpty() ? "euleriano" : "semi-euleriano";
        int start = chooseStartVertex(graph, odd);

        List<Integer> trail = runFleury(graph, start, strategy);
        boolean ok = (trail.size() - 1) == graph.originalEdgeCount;
        double elapsed = elapsedMs(startTime);

        if (!ok) {
            return new FleuryResult(false, "nao-euleriano", trail, elapsed,
                    "Falha ao percorrer todas as arestas.");
        }
        return new FleuryResult(true, classification, trail, elapsed, "OK");
    }

    private static List<Integer> runFleury(Graph graph, int start, BridgeStrategy strategy) {
        List<Integer> trail = new ArrayList<>();
        int current = start;
        trail.add(current);

        while (graph.activeEdges > 0) {
            int edgeId = chooseEdge(graph, current, strategy);
            if (edgeId == -1) {
                break;
            }

            int next = graph.other(edgeId, current);
            graph.removeEdge(edgeId);
            current = next;
            trail.add(current);
        }
        return trail;
    }

    private static int chooseEdge(Graph graph, int u, BridgeStrategy strategy) {
        List<Integer> incident = graph.activeIncidentEdges(u);
        if (incident.isEmpty()) {
            return -1;
        }
        if (incident.size() == 1) {
            return incident.get(0);
        }

        if (strategy == BridgeStrategy.NAIVE) {
            for (int edgeId : incident) {
                if (!isBridgeNaive(graph, u, edgeId)) {
                    return edgeId;
                }
            }
            return incident.get(0);
        }

        boolean[] isBridge = computeBridgesTarjan(graph);
        for (int edgeId : incident) {
            if (!isBridge[edgeId]) {
                return edgeId;
            }
        }
        return incident.get(0);
    }

    private static boolean isBridgeNaive(Graph graph, int u, int edgeId) {
        int before = countReachableFrom(graph, u);
        graph.removeEdge(edgeId);
        int after = countReachableFrom(graph, u);
        graph.restoreEdge(edgeId);
        return after < before;
    }

    private static int countReachableFrom(Graph graph, int start) {
        boolean[] visited = new boolean[graph.n + 1];
        IntStack stack = new IntStack(Math.max(4, graph.n / 16));

        visited[start] = true;
        stack.push(start);
        int count = 0;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            count++;

            List<Integer> adjIds = graph.adjEdgeIds[u];
            for (int i = 0; i < adjIds.size(); i++) {
                int edgeId = adjIds.get(i);
                if (!graph.active[edgeId]) {
                    continue;
                }
                int v = graph.other(edgeId, u);
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                }
            }
        }

        return count;
    }

    private static boolean[] computeBridgesTarjan(Graph graph) {
        int n = graph.n;
        int m = graph.edges.size();
        boolean[] bridge = new boolean[m];

        int[] disc = new int[n + 1];
        int[] low = new int[n + 1];
        int[] parent = new int[n + 1];
        int[] parentEdge = new int[n + 1];
        int[] nextNeighborIndex = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = -1;
            parentEdge[i] = -1;
        }

        int time = 0;
        IntStack stack = new IntStack(Math.max(4, n / 16));

        for (int start = 1; start <= n; start++) {
            if (graph.degree[start] == 0 || disc[start] != 0) {
                continue;
            }

            stack.push(start);
            while (!stack.isEmpty()) {
                int u = stack.peek();

                if (disc[u] == 0) {
                    disc[u] = ++time;
                    low[u] = disc[u];
                }

                List<Integer> adjIds = graph.adjEdgeIds[u];
                int i = nextNeighborIndex[u];
                while (i < adjIds.size() && !graph.active[adjIds.get(i)]) {
                    i++;
                }
                nextNeighborIndex[u] = i;

                if (i < adjIds.size()) {
                    int edgeId = adjIds.get(i);
                    nextNeighborIndex[u]++;

                    int v = graph.other(edgeId, u);
                    if (edgeId == parentEdge[u]) {
                        continue;
                    }

                    if (disc[v] == 0) {
                        parent[v] = u;
                        parentEdge[v] = edgeId;
                        stack.push(v);
                    } else {
                        low[u] = Math.min(low[u], disc[v]);
                    }
                } else {
                    stack.pop();
                    int p = parent[u];
                    if (p != -1) {
                        low[p] = Math.min(low[p], low[u]);
                        if (low[u] > disc[p]) {
                            bridge[parentEdge[u]] = true;
                        }
                    }
                }
            }
        }

        return bridge;
    }

    private static boolean isConnectedIgnoringIsolated(Graph graph) {
        int start = -1;
        for (int v = 1; v <= graph.n; v++) {
            if (graph.degree[v] > 0) {
                start = v;
                break;
            }
        }
        if (start == -1) {
            return true;
        }

        boolean[] visited = new boolean[graph.n + 1];
        IntStack stack = new IntStack(Math.max(4, graph.n / 16));
        visited[start] = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            int u = stack.pop();
            List<Integer> adjIds = graph.adjEdgeIds[u];
            for (int i = 0; i < adjIds.size(); i++) {
                int edgeId = adjIds.get(i);
                if (!graph.active[edgeId]) {
                    continue;
                }
                int v = graph.other(edgeId, u);
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                }
            }
        }

        for (int v = 1; v <= graph.n; v++) {
            if (graph.degree[v] > 0 && !visited[v]) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> oddDegreeVertices(Graph graph) {
        List<Integer> odd = new ArrayList<>();
        for (int v = 1; v <= graph.n; v++) {
            if ((graph.degree[v] & 1) == 1) {
                odd.add(v);
            }
        }
        return odd;
    }

    private static int chooseStartVertex(Graph graph, List<Integer> odd) {
        if (odd.size() == 2) {
            return odd.get(0);
        }
        for (int v = 1; v <= graph.n; v++) {
            if (graph.degree[v] > 0) {
                return v;
            }
        }
        return 1;
    }

    private static double elapsedMs(long startNano) {
        return (System.nanoTime() - startNano) / 1_000_000.0;
    }

    public static class Graph {
        final int n;
        final List<Edge> edges;
        final List<Integer>[] adjEdgeIds;
        final int[] degree;
        final boolean[] active;
        int activeEdges;
        final int originalEdgeCount;

        @SuppressWarnings("unchecked")
        Graph(int n, int m) {
            this.n = n;
            this.edges = new ArrayList<>(m);
            this.adjEdgeIds = new ArrayList[n + 1];
            this.degree = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                this.adjEdgeIds[i] = new ArrayList<>();
            }
            this.active = new boolean[m];
            this.activeEdges = 0;
            this.originalEdgeCount = m;
        }

        void addEdge(int u, int v) {
            int id = edges.size();
            edges.add(new Edge(u, v));
            active[id] = true;
            activeEdges++;

            adjEdgeIds[u].add(id);
            adjEdgeIds[v].add(id);
            degree[u]++;
            degree[v]++;
        }

        int other(int edgeId, int u) {
            Edge e = edges.get(edgeId);
            return (e.u == u) ? e.v : e.u;
        }

        void removeEdge(int edgeId) {
            if (!active[edgeId]) {
                return;
            }
            active[edgeId] = false;
            activeEdges--;
            Edge e = edges.get(edgeId);
            degree[e.u]--;
            degree[e.v]--;
        }

        void restoreEdge(int edgeId) {
            if (active[edgeId]) {
                return;
            }
            active[edgeId] = true;
            activeEdges++;
            Edge e = edges.get(edgeId);
            degree[e.u]++;
            degree[e.v]++;
        }

        List<Integer> activeIncidentEdges(int u) {
            List<Integer> ids = new ArrayList<>();
            List<Integer> raw = adjEdgeIds[u];
            for (int i = 0; i < raw.size(); i++) {
                int edgeId = raw.get(i);
                if (active[edgeId]) {
                    ids.add(edgeId);
                }
            }
            return ids;
        }

        static Graph readFromFile(String filePath) throws IOException {
            FastScanner fs = new FastScanner(filePath);
            int n = fs.nextInt();
            int m = fs.nextInt();

            if (n <= 0) {
                throw new IllegalArgumentException("Numero de vertices invalido: " + n);
            }
            if (m < 0) {
                throw new IllegalArgumentException("Numero de arestas invalido: " + m);
            }

            Graph g = new Graph(n, m);
            for (int i = 0; i < m; i++) {
                int u = fs.nextInt();
                int v = fs.nextInt();

                if (u < 1 || u > n || v < 1 || v > n) {
                    throw new IllegalArgumentException("Aresta fora do intervalo: " + u + " " + v);
                }
                if (u == v) {
                    throw new IllegalArgumentException("Laco nao permitido: " + u + " " + v);
                }

                g.addEdge(u, v);
            }
            fs.close();
            return g;
        }
    }

    public static class Edge {
        final int u;
        final int v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
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

        void push(int x) {
            if (size == data.length) {
                int[] next = new int[data.length * 2];
                System.arraycopy(data, 0, next, 0, data.length);
                data = next;
            }
            data[size++] = x;
        }

        int pop() {
            return data[--size];
        }

        int peek() {
            return data[size - 1];
        }

        boolean isEmpty() {
            return size == 0;
        }
    }

    private static class FastScanner {
        private final BufferedInputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        FastScanner(String filePath) throws IOException {
            this.in = new BufferedInputStream(new java.io.FileInputStream(filePath));
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
                if (c == -1) {
                    throw new IllegalArgumentException("Fim de arquivo inesperado.");
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
                    throw new IllegalArgumentException("Caractere invalido: " + (char) c);
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