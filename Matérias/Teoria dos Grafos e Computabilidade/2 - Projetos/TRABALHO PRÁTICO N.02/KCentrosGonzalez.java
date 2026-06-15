import java.io.*;
import java.util.*;

/*
 * Heurística de Gonzalez (2-Aproximação) para o Problema dos k-Centros.
 *
 * Fontes de referência (conforme solicitado para o trabalho):
 * 1) Gonzalez, T. F. (1985). Clustering to minimize the maximum intercluster
 *    distance. Theoretical Computer Science, 38, 293-306.
 *    DOI: https://doi.org/10.1016/0304-3975(85)90224-5
 *    (Algoritmo principal usado como base: "farthest-point clustering" -
 *     a cada passo, escolhe-se como novo centro o vértice com a maior
 *     distância mínima ao conjunto de centros já escolhidos. Também é a
 *     fonte da prova de que o raio obtido é, no máximo, o dobro do raio
 *     ótimo.)
 *
 * 2) Hochbaum, D. S., & Shmoys, D. B. (1985). A best possible heuristic for
 *    the k-center problem. Mathematics of Operations Research, 10(2), 180-184.
 *    DOI: https://doi.org/10.1287/moor.10.2.180
 *    (Resultado complementar usado: prova de que não existe algoritmo de
 *     aproximação com fator menor que 2 para o problema dos k-centros, a
 *     menos que P = NP, o que mostra que a garantia de Gonzalez (1985) é
 *     assintoticamente ótima.)
 *
 * 3) Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009).
 *    Introduction to Algorithms (3rd ed.). MIT Press.
 *    (Capítulo "All-Pairs Shortest Paths" usado como base para o algoritmo
 *     de Floyd-Warshall, empregado no pré-processamento da matriz de
 *     distâncias de todos os pares.)
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - O algoritmo mantém, para cada vértice v, a distância mínima distMin[v]
 *   até o conjunto de centros escolhido até o momento.
 * - O primeiro centro é escolhido arbitrariamente (vértice de índice 0),
 *   conforme indicado em Gonzalez (1985), já que o fator de aproximação 2
 *   vale para qualquer escolha inicial.
 * - A cada iteração, o próximo centro é o vértice v que maximiza
 *   distMin[v] (ou seja, o ponto mais distante do conjunto de centros
 *   atual). Em seguida, distMin[] é atualizado em O(n).
 * - O raio final reportado é max_v distMin[v] após a escolha dos k centros.
 *
 * CONFORMIDADE COM AS REFERÊNCIAS:
 * - Segue exatamente o algoritmo guloso de "farthest-first traversal"
 *   descrito em Gonzalez (1985), garantindo fator de aproximação 2 em
 *   relação ao raio ótimo (relação justificada também por
 *   Hochbaum & Shmoys, 1985).
 * - Utiliza Floyd-Warshall (Cormen et al., 2009) para obter a matriz de
 *   distâncias de todos os pares a partir do grafo esparso de entrada.
 *
 * Complexidade: O(k * n) para a escolha dos centros (após a obtenção da
 * matriz de distâncias via Floyd-Warshall, O(n^3)).
 */
public class KCentrosGonzalez {

    private int n;
    private int k;
    private double[][] dist;

    public KCentrosGonzalez(int n, int k, double[][] dist) {
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
     * Executa a heurística de Gonzalez.
     * @return Result com raio e lista de centros.
     */
    public Result resolve() {
        // distMin[v] = distância mínima de v ao conjunto de centros atual
        double[] distMin = new double[n];
        Arrays.fill(distMin, Double.MAX_VALUE);

        List<Integer> centros = new ArrayList<>();

        // Primeiro centro: vértice 0 (arbitrário)
        int primeiro = 0;
        centros.add(primeiro);
        for (int v = 0; v < n; v++)
            distMin[v] = dist[v][primeiro];

        for (int iter = 1; iter < k; iter++) {
            // Próximo centro: vértice mais distante do conjunto atual
            int proximo = 0;
            double maxDist = -1.0;
            for (int v = 0; v < n; v++) {
                if (distMin[v] > maxDist) {
                    maxDist = distMin[v];
                    proximo = v;
                }
            }
            centros.add(proximo);
            // Atualiza distâncias mínimas
            for (int v = 0; v < n; v++)
                distMin[v] = Math.min(distMin[v], dist[v][proximo]);
        }

        double raio = 0.0;
        for (double d : distMin) raio = Math.max(raio, d);

        return new Result(raio, centros);
    }

    /** Calcula o raio real de uma solução (verificação). */
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
            System.out.println("Uso: java KCentrosGonzalez <arquivo_instancia>");
            return;
        }
        // Descobrir n para alocar matriz
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); br.close();

        double[][] dist = new double[n][n];
        int[] info = lerInstancia(args[0], dist);
        int k = info[2];
        dist = floydWarshall(n, dist);

        System.out.printf("Instância: n=%d, k=%d%n", n, k);
        long inicio = System.currentTimeMillis();
        KCentrosGonzalez solver = new KCentrosGonzalez(n, k, dist);
        Result res = solver.resolve();
        long fim = System.currentTimeMillis();

        System.out.printf("Raio obtido: %.0f%n", res.raio);
        System.out.printf("Tempo: %.3f s%n", (fim - inicio) / 1000.0);

        System.out.print("Centros (1-indexado): ");
        for (int c : res.centros) System.out.print((c + 1) + " ");
        System.out.println();
    }
}
