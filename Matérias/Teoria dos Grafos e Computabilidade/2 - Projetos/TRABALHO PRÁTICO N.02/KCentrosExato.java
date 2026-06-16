import java.io.*;
import java.util.*;

/*
 * Algoritmo Exato para o Problema dos k-Centros, via busca binária sobre os
 * valores de distância combinada com um teste de viabilidade EXATO
 * ("decision problem") para um raio candidato r, resolvido por
 * branch-and-bound (busca exaustiva com poda) sobre o problema de
 * Cobertura de Conjuntos.
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
 *    (Base conceitual: para um raio fixo r, o problema dos k-centros é
 *     equivalente ao problema de Cobertura de Conjuntos (Set Cover) —
 *     "existe uma escolha de <= k vértices cujas vizinhanças de raio r
 *     cobrem V?" — classicamente NP-difícil. Esta equivalência justifica a
 *     resolução do teste de viabilidade por busca exaustiva exata sobre os
 *     subconjuntos de centros, conforme implementado abaixo.)
 *
 * 3) Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009).
 *    Introduction to Algorithms (3rd ed.). MIT Press.
 *    (Capítulos usados como base: "Backtracking / Branch-and-Bound" para a
 *     enumeração exaustiva de subconjuntos de centros com poda (ramos que
 *     não podem mais cobrir todos os vértices restantes dentro do
 *     orçamento de centros são descartados); e "All-Pairs Shortest Paths" -
 *     algoritmo de Floyd-Warshall, usado para obter a matriz de distâncias
 *     de menor custo entre todos os pares de vértices a partir do grafo de
 *     entrada.)
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - O raio ótimo r* pertence necessariamente ao conjunto de distâncias
 *   {d(u,v) | u,v em V}, que possui O(n^2) valores distintos. Assim, é
 *   feita uma busca binária sobre esses valores ordenados (Hochbaum &
 *   Shmoys, 1985).
 * - Para cada raio candidato r, o teste de viabilidade ("existe um conjunto
 *   C, |C| <= k, tal que toda vértice v está a distância <= r de algum
 *   centro em C?") é resolvido de forma EXATA por branch-and-bound: a cada
 *   passo, escolhe-se o vértice ainda não coberto com MENOS candidatos
 *   capazes de cobri-lo ("most-constrained-first") e ramifica-se sobre
 *   cada um desses candidatos, com poda quando o número de centros já
 *   usados atinge k sem que todos os vértices estejam cobertos.
 * - NENHUMA heurística é usada na decisão de viabilidade: o resultado
 *   retornado (viável / inviável) é sempre correto, isto é, o algoritmo é
 *   100% exato. O custo é complexidade exponencial no pior caso, por
 *   isso, na prática, só é tratável para instâncias pequenas (tipicamente
 *   pmed1 e pmed2 da OR-Library, o que é o resultado experimental esperado e discutido no
 *   relatório.
 *
 * CONFORMIDADE COM AS REFERÊNCIAS:
 * - Segue o esquema de Hochbaum & Shmoys (1985) de reduzir o problema de
 *   otimização a uma sequência de problemas de decisão sobre raios
 *   candidatos, resolvidos via busca binária.
 * - Resolve cada problema de decisão como uma instância de Cobertura de
 *   Conjuntos (Garey & Johnson, 1979), por enumeração exaustiva com poda
 *   (branch-and-bound, Cormen et al., 2009), garantindo exatidão.
 * - Utiliza Floyd-Warshall (Cormen et al., 2009) para pré-processar a
 *   matriz de distâncias de todos os pares a partir do grafo esparso
 *   fornecido nas instâncias da OR-Library.
 *
 * Complexidade: o pré-processamento (Floyd-Warshall) é O(n^3). A busca
 * binária realiza O(log(n^2)) = O(log n) testes de viabilidade. Cada teste
 * de viabilidade é, no pior caso, exponencial em n (branch-and-bound sobre
 * Set Cover, NP-difícil) — daí a inviabilidade prática para instâncias
 * maiores que pmed1/pmed2.
 */
public class KCentrosExato {

    private final int n;
    private final int k;
    private final double[][] dist;

    /** Para cada vértice v, lista de candidatos u tais que d(u,v) <= r (raio em análise). */
    private List<List<Integer>> cobrePor;

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
     * Verifica, de forma EXATA, se existe um conjunto C de até k centros tal
     * que todo vértice v está a distância <= r de algum centro em C.
     *
     * @param r raio candidato
     * @return lista de centros que cobrem todos os vértices com raio r,
     *         ou null se NÃO existir tal conjunto (instância inviável
     *         para este r).
     */
    private List<Integer> verificaViabilidadeExata(double r) {
        // cobrePor.get(v) = candidatos u tais que d(u,v) <= r
        cobrePor = new ArrayList<>(n);
        for (int v = 0; v < n; v++) {
            List<Integer> candidatos = new ArrayList<>();
            for (int u = 0; u < n; u++)
                if (dist[u][v] <= r) candidatos.add(u);
            cobrePor.add(candidatos);
            // d(v,v) = 0 <= r sempre, então candidatos nunca é vazio.
        }

        boolean[] coberto = new boolean[n];
        List<Integer> centrosEscolhidos = new ArrayList<>();
        return buscaExaustiva(coberto, centrosEscolhidos, 0);
    }

    /**
     * Busca exaustiva (branch-and-bound) sobre o problema de Cobertura de
     * Conjuntos: encontra um conjunto de centros de tamanho <= k que cubra
     * todos os vértices a partir do estado atual, ou retorna null se for
     * impossível.
     *
     * @param coberto            vetor de marcação de vértices já cobertos
     * @param centrosEscolhidos  centros escolhidos até o momento (mutável,
     *                           usado como pilha para backtracking)
     * @param totalCobertos      número de vértices já cobertos
     * @return cópia da lista de centros que resolve a instância a partir
     *         deste estado, ou null se inviável.
     */
    private List<Integer> buscaExaustiva(boolean[] coberto, List<Integer> centrosEscolhidos,
                                          int totalCobertos) {
        if (totalCobertos == n) {
            return new ArrayList<>(centrosEscolhidos);
        }
        if (centrosEscolhidos.size() == k) {
            // Orçamento de centros esgotado e ainda há vértices descobertos: poda.
            return null;
        }

        // Escolhe o vértice não-coberto com o menor número de candidatos
        // que o cobrem ("most-constrained-first"). Isso reduz o fator de
        // ramificação médio sem afetar a correção do resultado: continua
        // sendo necessário cobrir esse vértice com algum centro, então
        // ramificar sobre ele primeiro não descarta nenhuma solução.
        int vEscolhido = -1;
        int menorOpcoes = Integer.MAX_VALUE;
        for (int v = 0; v < n; v++) {
            if (coberto[v]) continue;
            int opcoes = cobrePor.get(v).size();
            if (opcoes < menorOpcoes) {
                menorOpcoes = opcoes;
                vEscolhido = v;
            }
        }

        // Ramifica sobre cada candidato capaz de cobrir vEscolhido
        for (int candidato : cobrePor.get(vEscolhido)) {
            if (centrosEscolhidos.contains(candidato)) continue;

            // Aplica o candidato: marca todos os vértices não-cobertos
            // que ele cobre (backtracking-friendly: guarda quais foram
            // marcados para poder desfazer depois).
            List<Integer> recemCobertos = new ArrayList<>();
            int novoTotal = totalCobertos;
            for (int v = 0; v < n; v++) {
                if (!coberto[v] && contem(cobrePor.get(v), candidato)) {
                    coberto[v] = true;
                    recemCobertos.add(v);
                    novoTotal++;
                }
            }

            centrosEscolhidos.add(candidato);
            List<Integer> resultado = buscaExaustiva(coberto, centrosEscolhidos, novoTotal);
            centrosEscolhidos.remove(centrosEscolhidos.size() - 1);

            // Desfaz a marcação de cobertura (backtracking)
            for (int v : recemCobertos) coberto[v] = false;

            if (resultado != null) return resultado;
        }

        return null;
    }

    /** Verifica se um inteiro pertence a uma lista (busca linear simples). */
    private static boolean contem(List<Integer> lista, int valor) {
        for (int x : lista) if (x == valor) return true;
        return false;
    }

    /**
     * Resolve o problema dos k-centros de forma exata via busca binária
     * sobre os valores distintos de distância, com teste de viabilidade
     * exato (branch-and-bound) para cada raio candidato.
     *
     * @return Result contendo o raio ótimo e o conjunto de centros que o
     *         alcança.
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
            List<Integer> centros = verificaViabilidadeExata(r);
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
            distOut[u][v] = w;
            distOut[v][u] = w;
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

        System.out.printf("Raio obtido (ÓTIMO): %.0f%n", res.raio);
        System.out.printf("Tempo: %.3f s%n", (fim - inicio) / 1000.0);

        System.out.print("Centros (1-indexado): ");
        for (int c : res.centros) System.out.print((c + 1) + " ");
        System.out.println();
    }
}
