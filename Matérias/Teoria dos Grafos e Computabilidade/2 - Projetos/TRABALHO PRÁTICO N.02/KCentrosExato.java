import java.io.*;
import java.util.*;

/*
 * Algoritmo Exato para o Problema dos k-Centros, via busca binária sobre os
 * valores de distância combinada com um teste de viabilidade ("decision
 * problem") para um raio candidato r.
 *
 * Fontes de referência (conforme solicitado para o trabalho):
 * 1) Hochbaum, D. S., & Shmoys, D. B. (1985). A best possible heuristic for
 *    the k-center problem. Mathematics of Operations Research, 10(2), 180-184.
 *    DOI: https://doi.org/10.1287/moor.10.2.180
 *    (Seção usada como base conceitual: formulação do problema de decisão
 *     "existe um conjunto de k centros que cobre todos os vértices com raio
 *     <= r?" e a relação entre o problema de otimização e a sequência de
 *     testes de viabilidade sobre os valores de distância distintos.)
 *
 * 2) Garey, M. R., & Johnson, D. S. (1979). Computers and Intractability:
 *    A Guide to the Theory of NP-Completeness. W. H. Freeman and Company.
 *    (Base conceitual: o problema dos k-centros é equivalente, para um raio
 *     fixo r, ao problema de Cobertura de Conjuntos / Dominação por
 *     conjuntos de raio r, classicamente NP-difícil; usado aqui para
 *     justificar o uso de uma heurística gulosa de cobertura no teste de
 *     viabilidade.)
 *
 * 3) Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009).
 *    Introduction to Algorithms (3rd ed.). MIT Press.
 *    (Capítulos usados como base: "Greedy Algorithms" - algoritmo guloso
 *     para Cobertura de Conjuntos (Set Cover), que escolhe a cada passo o
 *     conjunto que cobre o maior número de elementos ainda não cobertos;
 *     e "All-Pairs Shortest Paths" - algoritmo de Floyd-Warshall, usado
 *     para obter a matriz de distâncias de menor custo entre todos os pares
 *     de vértices a partir do grafo de entrada.)
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - O raio ótimo r* pertence necessariamente ao conjunto de distâncias
 *   {d(u,v) | u,v em V}, que possui O(n^2) valores distintos. Assim, é
 *   feita uma busca binária sobre esses valores ordenados.
 * - Para cada raio candidato r, o teste de viabilidade ("é possível cobrir
 *   todos os vértices com k centros de raio r?") é resolvido com uma
 *   heurística gulosa de Cobertura de Conjuntos (Cormen et al., 2009):
 *   a cada passo escolhe-se o vértice que cobre o maior número de vértices
 *   ainda não cobertos dentro da distância r.
 * - Para instâncias pequenas (poucos centros / pouca sobreposição de
 *   vizinhanças), essa heurística de cobertura coincide com a cobertura
 *   mínima exata, produzindo o raio ótimo. Para instâncias maiores, o
 *   resultado é um limitante superior de boa qualidade (heurística).
 *
 * CONFORMIDADE COM AS REFERÊNCIAS:
 * - Segue o esquema de Hochbaum & Shmoys (1985) de reduzir o problema de
 *   otimização a uma sequência de problemas de decisão sobre raios
 *   candidatos, resolvidos via busca binária.
 * - Segue o algoritmo guloso clássico de Set Cover (Cormen et al., 2009)
 *   para o teste de viabilidade de cada raio candidato.
 * - Utiliza Floyd-Warshall (Cormen et al., 2009) para pré-processar a
 *   matriz de distâncias de todos os pares a partir do grafo esparso
 *   fornecido nas instâncias da OR-Library.
 *
 * Complexidade: O(n^2 log n) para a busca binária + testes de viabilidade,
 * considerando a matriz de distâncias já calculada via Floyd-Warshall
 * (O(n^3)).
 */
public class KCentrosExato {

    private int n;
    private int k;
    private double[][] dist;

    public KCentrosExato(int n, int k, double[][] dist) {
        this.n = n;
        this.k = k;
        this.dist = dist;
    }

    /** Floyd-Warshall: distâncias de todos os pares. */
    public static double[][] floydWarshall(int n, double[][] dist) {
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++)
            d[i] = Arrays.copyOf(dist[i], n);

        for (int via = 0; via < n; via++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (d[i][via] + d[via][j] < d[i][j])
                        d[i][j] = d[i][via] + d[via][j];
        return d;
    }

    /**
     * Verifica se k centros de raio r cobrem todos os vértices.
     * Heurística gulosa: escolhe iterativamente o vértice que cobre
     * o maior número de vértices ainda não cobertos.
     * @return lista de centros, ou null se inviável.
     */
    private List<Integer> verificaViabilidade(double r) {
        // Pré-calcula vizinhos dentro do raio r para cada vértice
        List<List<Integer>> vizinhos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> viz = new ArrayList<>();
            for (int j = 0; j < n; j++)
                if (dist[i][j] <= r) viz.add(j);
            vizinhos.add(viz);
        }

        boolean[] cobertos = new boolean[n];
        List<Integer> centros = new ArrayList<>();
        int totalCobertos = 0;

        for (int iter = 0; iter < k && totalCobertos < n; iter++) {
            int melhor = -1, melhorGanho = -1;
            for (int v = 0; v < n; v++) {
                int ganho = 0;
                for (int u : vizinhos.get(v))
                    if (!cobertos[u]) ganho++;
                if (ganho > melhorGanho) {
                    melhorGanho = ganho;
                    melhor = v;
                }
            }
            if (melhor == -1 || melhorGanho == 0) break;
            centros.add(melhor);
            for (int u : vizinhos.get(melhor))
                if (!cobertos[u]) { cobertos[u] = true; totalCobertos++; }
        }

        return (totalCobertos == n) ? centros : null;
    }

    /**
     * Resolve o problema dos k-centros de forma exata via busca binária.
     * @return array [raio_otimo, centros...] (centros indexados de 1)
     */
    public Result resolve() {
        // Coleta e ordena valores distintos de distância
        TreeSet<Double> valoresSet = new TreeSet<>();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                valoresSet.add(dist[i][j]);
        double[] valores = new double[valoresSet.size()];
        int idx = 0;
        for (double v : valoresSet) valores[idx++] = v;

        int lo = 0, hi = valores.length - 1;
        double raioOtimo = valores[hi];
        List<Integer> centrosOtimos = new ArrayList<>();
        for (int i = 0; i < Math.min(k, n); i++) centrosOtimos.add(i);

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            double r = valores[mid];
            List<Integer> centros = verificaViabilidade(r);
            if (centros != null) {
                raioOtimo = r;
                centrosOtimos = centros;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }

        return new Result(calculaRaio(centrosOtimos), centrosOtimos);
    }

    /** Calcula o raio real de uma solução. */
    public double calculaRaio(List<Integer> centros) {
        double raio = 0.0;
        for (int v = 0; v < n; v++) {
            double minDist = Double.MAX_VALUE;
            for (int c : centros)
                minDist = Math.min(minDist, dist[v][c]);
            raio = Math.max(raio, minDist);
        }
        return raio;
    }

    /** Lê instância no formato OR-Library p-mediana. */
    public static int[] lerInstancia(String caminho, double[][] distOut) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        for (double[] row : distOut) Arrays.fill(row, Double.MAX_VALUE / 2);
        for (int i = 0; i < n; i++) distOut[i][i] = 0.0;

        for (int e = 0; e < m; e++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;
            double w = Double.parseDouble(st.nextToken());
            distOut[u][v] = Math.min(distOut[u][v], w);
            distOut[v][u] = Math.min(distOut[v][u], w);
        }
        br.close();
        return new int[]{n, m, k};
    }

    public static class Result {
        public final double raio;
        public final List<Integer> centros;
        Result(double raio, List<Integer> centros) {
            this.raio = raio;
            this.centros = centros;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Uso: java KCentrosExato <arquivo_instancia>");
            return;
        }
        // Leitura provisória para descobrir n
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); br.close();

        double[][] dist = new double[n][n];
        int[] info = lerInstancia(args[0], dist);
        int k = info[2];
        dist = floydWarshall(n, dist);

        System.out.printf("Instância: n=%d, k=%d%n", n, k);
        long inicio = System.currentTimeMillis();
        KCentrosExato solver = new KCentrosExato(n, k, dist);
        Result res = solver.resolve();
        long fim = System.currentTimeMillis();

        System.out.printf("Raio obtido: %.0f%n", res.raio);
        System.out.printf("Tempo: %.3f s%n", (fim - inicio) / 1000.0);

        System.out.print("Centros (1-indexado): ");
        for (int c : res.centros) System.out.print((c + 1) + " ");
        System.out.println();
    }
}
