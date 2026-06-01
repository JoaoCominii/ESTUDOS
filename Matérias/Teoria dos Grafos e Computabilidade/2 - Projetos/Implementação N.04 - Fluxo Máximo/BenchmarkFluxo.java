import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Benchmark para o problema de caminhos disjuntos em arestas via fluxo maximo.
 *
 * Fontes de referencia para a metodologia de avaliacao:
 * 1) CLRS - Introduction to Algorithms, Chapter 26: Maximum Flow.
 * 2) Dinic, E. A. (1970). Algorithm for Solution of a Problem of Maximum Flow in Networks with Power Estimation.
 *
 * OBJETIVO:
 * - Medir o tempo de execucao do FluxoMaximo em dois tipos de grafos:
 *   grafo-esparso e grafo-camadas.
 * - Registrar a quantidade de caminhos disjuntos em arestas encontrados entre
 *   1 e n para cada instancia.
 * - Gerar CSV e Markdown com as tabelas de resultados.
 */
public class BenchmarkFluxo {
    private static final int REPETITIONS = 5;
    private static final Pattern SIZE_PATTERN = Pattern.compile(".*-(\\d+)\\.txt$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de benchmark a ser executado:");
        System.out.println("1 - Grafo esparso aleatorio direcionado");
        System.out.println("2 - Grafo em camadas direcionado");
        System.out.println("3 - Ambos");
        System.out.print("Opcao: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida.");
            scanner.close();
            return;
        }

        int opcao = scanner.nextInt();
        scanner.close();

        try {
            if (opcao == 1) {
                runBenchmark("grafo-esparso", "grafo-esparso");
            } else if (opcao == 2) {
                runBenchmark("grafo-camadas", "grafo-camadas");
            } else if (opcao == 3) {
                runBenchmark("grafo-esparso", "grafo-esparso");
                runBenchmark("grafo-camadas", "grafo-camadas");
            } else {
                System.out.println("Opcao invalida. Use 1, 2 ou 3.");
            }
        } catch (IOException e) {
            System.out.println("Erro no benchmark: " + e.getMessage());
        }
    }

    private static void runBenchmark(String folderName, String label) throws IOException {
        File folder = new File(folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("pasta invalida: " + folder.getAbsolutePath());
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            throw new IOException("nenhum arquivo .txt encontrado em: " + folder.getAbsolutePath());
        }

        List<File> sorted = new ArrayList<>();
        Collections.addAll(sorted, files);
        sorted.sort(Comparator
                .comparingInt(BenchmarkFluxo::extractSize)
                .thenComparing(File::getName));

        List<BenchmarkRow> rows = new ArrayList<>();
        for (File file : sorted) {
            BenchmarkRow row = benchmarkFile(file);
            rows.add(row);
            printRow(row);
        }

        File csv = new File(folder, "benchmark-" + label + ".csv");
        File md = new File(folder, "benchmark-" + label + ".md");
        writeCsv(csv, rows);
        writeMarkdown(md, label, rows);

        System.out.println();
        System.out.println("CSV salvo em: " + csv.getAbsolutePath());
        System.out.println("Tabela Markdown salva em: " + md.getAbsolutePath());
    }

    private static BenchmarkRow benchmarkFile(File file) throws IOException {
        int size = extractSize(file);
        int source = 1;
        int sink = size;

        FluxoMaximo.Result warmup = FluxoMaximo.solveFromFile(file.getAbsolutePath(), source, sink);
        double total = 0.0;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        FluxoMaximo.Result last = warmup;

        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            FluxoMaximo.Result result = FluxoMaximo.solveFromFile(file.getAbsolutePath(), source, sink);
            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;
            total += elapsedMs;
            min = Math.min(min, elapsedMs);
            max = Math.max(max, elapsedMs);
            last = result;
        }

        int edges = countEdges(file);
        return new BenchmarkRow(
                file.getName(),
                size,
                edges,
                last.maxFlow,
                last.paths.size(),
                total / REPETITIONS,
                min,
                max);
    }

    private static int countEdges(File file) throws IOException {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String firstLine = nextNonEmptyLine(br);
            if (firstLine == null) {
                return 0;
            }
            String[] header = firstLine.trim().split("\\s+");
            return Integer.parseInt(header[1]);
        }
    }

    private static void printRow(BenchmarkRow row) {
        System.out.println("=== " + row.fileName + " ===");
        System.out.println("Vertices: " + row.vertices);
        System.out.println("Arestas: " + row.edges);
        System.out.println("Caminhos disjuntos: " + row.maxFlow);
        System.out.println("Quantidade de caminhos listados: " + row.pathsFound);
        System.out.println("Tempo medio (ms): " + formatMs(row.avgMs));
        System.out.println("Tempo minimo (ms): " + formatMs(row.minMs));
        System.out.println("Tempo maximo (ms): " + formatMs(row.maxMs));
        System.out.println();
    }

    private static void writeCsv(File out, List<BenchmarkRow> rows) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            bw.write("arquivo,vertices,arestas,caminhos_disjuntos,caminhos_listados,tempo_medio_ms,tempo_min_ms,tempo_max_ms");
            bw.newLine();
            for (BenchmarkRow row : rows) {
                bw.write(row.fileName);
                bw.write(',');
                bw.write(Integer.toString(row.vertices));
                bw.write(',');
                bw.write(Integer.toString(row.edges));
                bw.write(',');
                bw.write(Integer.toString(row.maxFlow));
                bw.write(',');
                bw.write(Integer.toString(row.pathsFound));
                bw.write(',');
                bw.write(formatMs(row.avgMs));
                bw.write(',');
                bw.write(formatMs(row.minMs));
                bw.write(',');
                bw.write(formatMs(row.maxMs));
                bw.newLine();
            }
        }
    }

    private static void writeMarkdown(File out, String label, List<BenchmarkRow> rows) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            bw.write("# Benchmark Fluxo Maximo - " + label);
            bw.newLine();
            bw.newLine();
            bw.write("Medições do fluxo máximo entre os vértices 1 e n, com " + REPETITIONS + " repetições por arquivo.");
            bw.newLine();
            bw.newLine();
            bw.write("| Arquivo | Vertices | Arestas | Caminhos disjuntos | Caminhos listados | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |");
            bw.newLine();
            bw.write("|---|---:|---:|---:|---:|---:|---:|---:|");
            bw.newLine();
            for (BenchmarkRow row : rows) {
                bw.write("| " + row.fileName + " | " + row.vertices + " | " + row.edges + " | "
                        + row.maxFlow + " | " + row.pathsFound + " | " + formatMs(row.avgMs) + " | "
                        + formatMs(row.minMs) + " | " + formatMs(row.maxMs) + " |");
                bw.newLine();
            }
        }
    }

    private static String nextNonEmptyLine(java.io.BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return null;
    }

    private static int extractSize(File file) {
        Matcher matcher = SIZE_PATTERN.matcher(file.getName());
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return Integer.MAX_VALUE;
    }

    private static String formatMs(double value) {
        return String.format(Locale.US, "%.3f", value);
    }

    private static class BenchmarkRow {
        final String fileName;
        final int vertices;
        final int edges;
        final int maxFlow;
        final int pathsFound;
        final double avgMs;
        final double minMs;
        final double maxMs;

        BenchmarkRow(String fileName, int vertices, int edges, int maxFlow, int pathsFound,
                     double avgMs, double minMs, double maxMs) {
            this.fileName = fileName;
            this.vertices = vertices;
            this.edges = edges;
            this.maxFlow = maxFlow;
            this.pathsFound = pathsFound;
            this.avgMs = avgMs;
            this.minMs = minMs;
            this.maxMs = maxMs;
        }
    }
}
