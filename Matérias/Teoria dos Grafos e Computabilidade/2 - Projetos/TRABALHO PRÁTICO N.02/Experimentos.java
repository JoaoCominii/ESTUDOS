import java.io.*;
import java.util.*;

/*
 * Script de experimentos comparativos entre o método exato (KCentrosExato)
 * e a heurística de Gonzalez (KCentrosGonzalez) para o problema dos
 * k-centros, aplicados às 40 instâncias de p-medianas da OR-Library.
 *
 * Fontes de referência (conforme solicitado para o trabalho):
 * 1) Beasley, J. E. (1990). OR-Library: Distributing test problems by
 *    electronic mail. Journal of the Operational Research Society, 41(11),
 *    1069-1072. DOI: https://doi.org/10.1057/jors.1990.166
 *    (Origem das instâncias pmed1.txt..pmed40.txt utilizadas nos testes,
 *     incluindo o formato de leitura: primeira linha com n, m, k, seguida
 *     de m linhas com arestas (u, v, peso).)
 *
 * 2) Hochbaum, D. S., & Shmoys, D. B. (1985). A best possible heuristic for
 *    the k-center problem. Mathematics of Operations Research, 10(2), 180-184.
 *    DOI: https://doi.org/10.1287/moor.10.2.180
 *
 * 3) Gonzalez, T. F. (1985). Clustering to minimize the maximum intercluster
 *    distance. Theoretical Computer Science, 38, 293-306.
 *    DOI: https://doi.org/10.1016/0304-3975(85)90224-5
 *    (Itens 2 e 3 fundamentam, respectivamente, o algoritmo exato e a
 *     heurística aproximada cujos resultados são comparados aqui; ver os
 *     comentários de cabeçalho de KCentrosExato.java e
 *     KCentrosGonzalez.java para detalhes de cada implementação.)
 *
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * - Os valores em RAIOS_OTIMOS correspondem aos raios ótimos esperados para
 *   o problema dos k-centros em cada instância, conforme fornecidos no
 *   enunciado do trabalho (Tabela 1), e são usados apenas para calcular o
 *   fator de aproximação (FA = raio_obtido / raio_ótimo) de cada método.
 * - Para cada instância, a heurística de Gonzalez é sempre executada. O
 *   método exato é executado apenas para instâncias com n <= LIMITE_N e
 *   abortado (não reportado) caso ultrapasse o limite de tempo definido em
 *   LIMITE_MS, pois o teste de viabilidade do método exato (branch-and-bound
 *   sobre Set Cover, ver KCentrosExato.java) tem complexidade exponencial
 *   no pior caso. Na prática, espera-se que o exato termine em tempo
 *   razoável apenas para as primeiras instâncias (pmed1, pmed2), conforme
 *   discutido no relatório.
 * - Os resultados de cada instância (n, k, raio ótimo, raio e tempo de
 *   cada método, fator de aproximação) são impressos em formato tabular e
 *   gravados em resultados.csv para posterior análise/plotagem.
 */
public class Experimentos {

    // Raios ótimos esperados para as 40 instâncias (do enunciado do trabalho)
    private static final int[] RAIOS_OTIMOS = {
        127, 98, 93, 74, 48,   // 1-5
         84, 64, 55, 37, 20,   // 6-10
         59, 51, 35, 26, 18,   // 11-15
         47, 39, 28, 18, 13,   // 16-20
         40, 38, 22, 15, 11,   // 21-25
         38, 32, 18, 13,  9,   // 26-30
         30, 29, 15, 11,       // 31-34
         30, 27, 15,           // 35-37
         29, 23, 13            // 38-40
    };

    private static final long LIMITE_MS = 300_000; // 5 minutos
    private static final int LIMITE_N = 300; // tamanho máximo de instância para tentar o exato

    public static void main(String[] args) throws IOException {
        String pasta = (args.length > 0) ? args[0] : ".";
        PrintWriter csv = new PrintWriter(new FileWriter("resultados.csv"));
        csv.println("instancia,n,k,otimo,exato_raio,exato_fa,exato_tempo_s,gonz_raio,gonz_fa,gonz_tempo_s");

        System.out.printf("%-4s %-4s %-4s %-4s | %-6s %-5s %-9s | %-6s %-5s %-9s%n",
                "Inst", "n", "k", "Ót", "Exato", "FA_E", "T_E(s)", "Gonz", "FA_G", "T_G(s)");
        System.out.println("-".repeat(65));

        for (int i = 1; i <= 40; i++) {
            String arq = pasta + File.separator + "pmed" + i + ".txt";
            File f = new File(arq);
            if (!f.exists()) {
                System.out.printf("[pmed%d.txt não encontrado, pulando]%n", i);
                continue;
            }

            // Descobre n para alocar a matriz
            BufferedReader br = new BufferedReader(new FileReader(arq));
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken()); br.close();

            double[][] dist = new double[n][n];
            int[] info = KCentrosExato.lerInstancia(arq, dist);
            int k = info[2];
            dist = KCentrosExato.floydWarshall(n, dist);
            int otimo = RAIOS_OTIMOS[i - 1];

            // --- Gonzalez (sempre roda) ---
            long t0 = System.currentTimeMillis();
            KCentrosGonzalez solverG = new KCentrosGonzalez(n, k, dist);
            KCentrosGonzalez.Result resG = solverG.resolve();
            double tG = (System.currentTimeMillis() - t0) / 1000.0;
            double faG = resG.raio / otimo;

            // --- Exato (apenas para n <= LIMITE_N, com timeout de LIMITE_MS) ---
            // O teste de viabilidade do exato é exponencial no pior caso, então
            // executamos em uma thread separada e a interrompemos caso exceda
            // o tempo limite, evitando que o experimento fique bloqueado
            // indefinidamente em instâncias maiores.
            double raioE = Double.NaN, faE = Double.NaN, tE = Double.NaN;
            String exatoStr = "   ---- -----   -------";
            if (n <= LIMITE_N) {
                final double[][] distFinal = dist;
                final int nFinal = n, kFinal = k;
                final KCentrosExato.Result[] resultadoExato = new KCentrosExato.Result[1];

                Thread worker = new Thread(() -> {
                    KCentrosExato solverE = new KCentrosExato(nFinal, kFinal, distFinal);
                    resultadoExato[0] = solverE.resolve();
                });

                long t0e = System.currentTimeMillis();
                worker.start();
                try {
                    worker.join(LIMITE_MS);
                } catch (InterruptedException ignored) {}
                tE = (System.currentTimeMillis() - t0e) / 1000.0;

                if (worker.isAlive()) {
                    // Excedeu o tempo limite: interrompe e descarta o resultado parcial
                    worker.interrupt();
                    tE = Double.NaN;
                } else if (resultadoExato[0] != null) {
                    raioE = resultadoExato[0].raio;
                    faE = raioE / otimo;
                    exatoStr = String.format("%6.0f %5.2f %9.3f", raioE, faE, tE);
                }
            }

            System.out.printf("%4d %4d %4d %4d | %s | %6.0f %5.2f %9.4f%n",
                    i, n, k, otimo, exatoStr, resG.raio, faG, tG);

            csv.printf(Locale.US, "%d,%d,%d,%d,%.0f,%.4f,%.4f,%.0f,%.4f,%.4f%n",
                    i, n, k, otimo,
                    Double.isNaN(raioE) ? -1 : raioE,
                    Double.isNaN(faE) ? -1 : faE,
                    Double.isNaN(tE) ? -1 : tE,
                    resG.raio, faG, tG);
        }

        csv.close();
        System.out.println("\nResultados salvos em resultados.csv");
    }
}
