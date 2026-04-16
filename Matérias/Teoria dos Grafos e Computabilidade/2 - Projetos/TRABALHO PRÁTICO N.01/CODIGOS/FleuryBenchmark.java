import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Benchmark de tempo médio do método de Fleury com duas estratégias de pontes.
 *
 * Fontes de referência:
 * - Fleury (descrição): https://en.wikipedia.org/wiki/Fleury%27s_algorithm
 * - Tarjan (1974): https://doi.org/10.1016/0020-0190(74)90003-9
 * - Rosen (7th ed.), Section 10.5 (Euler Circuits and Euler Paths).
 *
 * OBJETIVO DESTE BENCHMARK:
 * - calcular o tempo médio para os grafos dos 3 tipos do trabalho
 *   (euleriano, semi-euleriano, nao-euleriano)
 * - considerando as instancias de cada combinação tipo+tamanho.
 */
public class FleuryBenchmark {

    private static final Pattern FILE_PATTERN = Pattern.compile(
            "^grafo-(euleriano|semi-euleriano|nao-euleriano)-(\\d+)-(\\d{2})\\.txt$"
    );

    public static void main(String[] args) {
        if (args.length >= 2) {
            String folderPath = args[0].trim();
            int opcao;
            Integer maxN = null;
            try {
                opcao = Integer.parseInt(args[1].trim());
                if (args.length >= 3) {
                    maxN = Integer.parseInt(args[2].trim());
                    if (maxN <= 0) {
                        System.out.println("maxN invalido. Use inteiro > 0.");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Argumentos invalidos. Uso: java FleuryBenchmark <pasta> <1|2> [maxN]");
                return;
            }

            FleuryEngine.BridgeStrategy strategy = parseStrategy(opcao);
            if (strategy == null) {
                System.out.println("Opcao invalida. Use 1 (Naive) ou 2 (Tarjan).");
                return;
            }

            try {
                runBenchmark(folderPath, strategy, maxN);
            } catch (IOException e) {
                System.out.println("Erro no benchmark: " + e.getMessage());
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a pasta com os grafos (ex.: amostras-15): ");
        String folderPath = scanner.nextLine().trim();

        System.out.println("Escolha a estrategia de identificacao de pontes para o Fleury:");
        System.out.println("1 - Naive");
        System.out.println("2 - Tarjan");
        System.out.print("Opcao: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Opcao invalida.");
            scanner.close();
            return;
        }
        int opcao = scanner.nextInt();
        scanner.close();

        FleuryEngine.BridgeStrategy strategy = parseStrategy(opcao);
        if (strategy == null) {
            System.out.println("Opcao invalida. Use 1 (Naive) ou 2 (Tarjan).");
            return;
        }

        try {
            runBenchmark(folderPath, strategy, null);
        } catch (IOException e) {
            System.out.println("Erro no benchmark: " + e.getMessage());
        }
    }

    private static FleuryEngine.BridgeStrategy parseStrategy(int option) {
        if (option == 1) {
            return FleuryEngine.BridgeStrategy.NAIVE;
        }
        if (option == 2) {
            return FleuryEngine.BridgeStrategy.TARJAN;
        }
        return null;
    }

    private static void runBenchmark(
            String folderPath,
            FleuryEngine.BridgeStrategy strategy,
            Integer maxN) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("Pasta invalida: " + folderPath);
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            throw new IOException("Pasta sem arquivos: " + folderPath);
        }

        List<FileCase> selectedCases = selectEligibleCases(files, maxN);
        int totalMatched = selectedCases.size();
        Map<String, GroupInfo> groupsByKey = buildExpectedGroups(selectedCases);

        if (totalMatched == 0) {
            throw new IOException("Nenhum arquivo elegivel no padrao (verifique pasta e maxN).");
        }

        Map<String, Stats> statsByGroup = new HashMap<>();
        int totalProcessed = 0;

        System.out.println("Iniciando benchmark...");
        System.out.println("Estrategia: " + (strategy == FleuryEngine.BridgeStrategy.NAIVE ? "Naive" : "Tarjan"));
        if (maxN != null) {
            System.out.println("Filtro maxN: " + maxN);
        }
        System.out.println("Arquivos elegiveis: " + totalMatched);

        for (FileCase fileCase : selectedCases) {
            FleuryEngine.FleuryResult result = FleuryEngine.solveFromFile(fileCase.file.getAbsolutePath(), strategy);

            Stats stats = statsByGroup.computeIfAbsent(fileCase.key, k -> new Stats(fileCase.type, fileCase.n));
            stats.add(result.elapsedMs, result.hasEulerTrail, result.classification);
            totalProcessed++;
            if (totalProcessed % 5 == 0 || totalProcessed == totalMatched) {
                System.out.println("Progresso: " + totalProcessed + "/" + totalMatched + " arquivos");
            }
        }

        if (totalProcessed == 0) {
            throw new IOException("Nenhum arquivo no padrao esperado foi encontrado.");
        }

        List<Stats> ordered = new ArrayList<>(statsByGroup.values());
        Collections.sort(ordered, Comparator
                .comparingInt((Stats s) -> typeRank(s.type))
                .thenComparingInt(s -> s.n));

        List<GroupInfo> orderedGroups = new ArrayList<>(groupsByKey.values());
        Collections.sort(orderedGroups, Comparator
                .comparingInt((GroupInfo g) -> typeRank(g.type))
                .thenComparingInt(g -> g.n));

        printSummary(orderedGroups, statsByGroup, strategy, totalProcessed, maxN);
        writeCsv(folderPath, orderedGroups, statsByGroup, strategy);
    }

    private static Map<String, GroupInfo> buildExpectedGroups(List<FileCase> selectedCases) {
        Map<String, GroupInfo> groupsByKey = new HashMap<>();
        for (FileCase fileCase : selectedCases) {
            GroupInfo info = groupsByKey.get(fileCase.key);
            if (info == null) {
                info = new GroupInfo(fileCase.type, fileCase.n, fileCase.key);
                groupsByKey.put(fileCase.key, info);
            }
            info.expectedCount++;
        }
        return groupsByKey;
    }

    private static List<FileCase> selectEligibleCases(File[] files, Integer maxN) {
        List<FileCase> all = new ArrayList<>();

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            Matcher matcher = FILE_PATTERN.matcher(file.getName());
            if (!matcher.matches()) {
                continue;
            }

            String type = matcher.group(1);
            int n = Integer.parseInt(matcher.group(2));
            if (maxN != null && n > maxN) {
                continue;
            }
            all.add(new FileCase(file, type, n));
        }

        Collections.sort(all, Comparator
                .comparingInt((FileCase c) -> typeRank(c.type))
                .thenComparingInt(c -> c.n)
                .thenComparing(c -> c.file.getName()));

        return all;
    }

    private static void printSummary(
            List<GroupInfo> orderedGroups,
            Map<String, Stats> statsByGroup,
            FleuryEngine.BridgeStrategy strategy,
            int totalProcessed,
            Integer maxN) {
        System.out.println();
        System.out.println("==== RESULTADO BENCHMARK FLEURY ====");
        System.out.println("Estrategia: " + (strategy == FleuryEngine.BridgeStrategy.NAIVE ? "Naive" : "Tarjan"));
        if (maxN != null) {
            System.out.println("Filtro maxN: " + maxN);
        }
        System.out.println("Arquivos processados: " + totalProcessed);
        System.out.println();

        System.out.println("Tipo | N | Qtd(proc/prev) | Status | Media (ms) | Mediana (ms) | P95 (ms) | Eulerianos encontrados");
        for (GroupInfo g : orderedGroups) {
            Stats s = statsByGroup.get(g.key);
            int processed = s == null ? 0 : s.count;
            boolean complete = processed == g.expectedCount;

            String mean = complete && s != null ? formatMs(s.meanMs()) : "-";
            String median = complete && s != null ? formatMs(s.medianMs()) : "-";
            String p95 = complete && s != null ? formatMs(s.p95Ms()) : "-";
            int eulerFound = s == null ? 0 : s.eulerFoundCount;

            System.out.printf(Locale.US, "%s | %d | %d/%d | %s | %s | %s | %s | %d%n",
                    g.type, g.n, processed, g.expectedCount, complete ? "ok" : "erro", mean, median, p95, eulerFound);
        }
    }

    private static void writeCsv(
            String folderPath,
            List<GroupInfo> orderedGroups,
            Map<String, Stats> statsByGroup,
            FleuryEngine.BridgeStrategy strategy) throws IOException {
        String strategyName = strategy == FleuryEngine.BridgeStrategy.NAIVE ? "naive" : "tarjan";
        File out = new File(folderPath, "benchmark-fleury-" + strategyName + ".csv");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            bw.write("tipo,n,quantidade_processada,quantidade_prevista,status,tempo_medio_ms,tempo_mediana_ms,tempo_p95_ms,eulerianos_encontrados");
            bw.newLine();

            for (GroupInfo g : orderedGroups) {
                Stats s = statsByGroup.get(g.key);
                int processed = s == null ? 0 : s.count;
                boolean complete = processed == g.expectedCount;

                String mean = complete && s != null ? String.format(Locale.US, "%.6f", s.meanMs()) : "0.000000";
                String median = complete && s != null ? String.format(Locale.US, "%.6f", s.medianMs()) : "0.000000";
                String p95 = complete && s != null ? String.format(Locale.US, "%.6f", s.p95Ms()) : "0.000000";
                int eulerFound = s == null ? 0 : s.eulerFoundCount;

                bw.write(g.type + "," + g.n + "," + processed + "," + g.expectedCount + ","
                    + (complete ? "ok" : "erro") + ","
                        + mean + ","
                        + median + ","
                        + p95 + ","
                        + eulerFound);
                bw.newLine();
            }
        }

        System.out.println();
        System.out.println("CSV salvo em: " + out.getAbsolutePath());
    }

    private static int typeRank(String type) {
        if ("euleriano".equals(type)) {
            return 1;
        }
        if ("semi-euleriano".equals(type)) {
            return 2;
        }
        if ("nao-euleriano".equals(type)) {
            return 3;
        }
        return 99;
    }

    private static String formatMs(double value) {
        return String.format(Locale.US, "%.3f", value);
    }

    private static class FileCase {
        final File file;
        final String type;
        final int n;
        final String key;

        FileCase(File file, String type, int n) {
            this.file = file;
            this.type = type;
            this.n = n;
            this.key = type + ":" + n;
        }
    }

    private static class GroupInfo {
        final String type;
        final int n;
        final String key;
        int expectedCount;

        GroupInfo(String type, int n, String key) {
            this.type = type;
            this.n = n;
            this.key = key;
            this.expectedCount = 0;
        }
    }

    private static class Stats {
        final String type;
        final int n;
        int count;
        double totalMs;
        int eulerFoundCount;
        final List<Double> samplesMs;

        Stats(String type, int n) {
            this.type = type;
            this.n = n;
            this.count = 0;
            this.totalMs = 0.0;
            this.eulerFoundCount = 0;
            this.samplesMs = new ArrayList<>();
        }

        void add(double elapsedMs, boolean hasEulerTrail, String classification) {
            count++;
            totalMs += elapsedMs;
            samplesMs.add(elapsedMs);
            if (hasEulerTrail && ("euleriano".equals(classification) || "semi-euleriano".equals(classification))) {
                eulerFoundCount++;
            }
        }

        double meanMs() {
            return count == 0 ? 0.0 : totalMs / count;
        }

        double medianMs() {
            return percentile(0.50);
        }

        double p95Ms() {
            return percentile(0.95);
        }

        private double percentile(double p) {
            if (samplesMs.isEmpty()) {
                return 0.0;
            }
            List<Double> sorted = new ArrayList<>(samplesMs);
            Collections.sort(sorted);
            int idx = (int) Math.ceil(p * sorted.size()) - 1;
            if (idx < 0) {
                idx = 0;
            }
            if (idx >= sorted.size()) {
                idx = sorted.size() - 1;
            }
            return sorted.get(idx);
        }
    }
}