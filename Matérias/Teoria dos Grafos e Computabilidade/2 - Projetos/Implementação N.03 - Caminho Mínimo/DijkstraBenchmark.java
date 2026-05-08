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
 * Benchmark do algoritmo de Dijkstra com desempate pelo menor numero de arestas.
 *
 * Fontes de referencia para a metodologia de avaliacao:
 * 1) Cormen, T. H.; Leiserson, C. E.; Rivest, R. L.; Stein, C.
 *    Introduction to Algorithms (CLRS), 3rd/4th ed.
 *    - Capitulo 24: Single-Source Shortest Paths
 *    - Base conceitual para a analise de execucao do algoritmo de Dijkstra.
 *
 * 2) Dijkstra, E. W. (1959).
 *    A note on two problems in connexion with graphs.
 *    Numerische Mathematik, 1, 269-271.
 *    DOI: https://doi.org/10.1007/BF01386390
 *
 * OBJETIVO DO BENCHMARK:
 * - Ler as instancias geradas pelo GeradorDeGrafos.
 * - Medir o tempo de execucao do caminho minimo entre 1 e n.
 * - Registrar o comprimento do caminho e a quantidade de arestas.
 * - Gerar tabelas em CSV e Markdown, uma para cada tipo de grafo.
 *
 * FORMATO ASSUMIDO DAS INSTANCIAS:
 * - primeira linha: n m
 * - linhas seguintes: u v peso
 * - vertices numerados de 1 a n
 */
public class DijkstraBenchmark {
    private static final int REPETITIONS = 5;
    private static final Pattern SIZE_PATTERN = Pattern.compile(".*-(\\d+)\\.txt$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de benchmark a ser executado:");
        System.out.println("1 - Grafo esparso aleatorio direcionado");
        System.out.println("2 - Grafo denso aleatorio direcionado");
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
                runBenchmark("grafo-denso", "grafo-denso");
            } else if (opcao == 3) {
                runBenchmark("grafo-esparso", "grafo-esparso");
                runBenchmark("grafo-denso", "grafo-denso");
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
                .comparingInt(DijkstraBenchmark::extractSize)
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
        Dijkstra.Graph graph = Dijkstra.readGraph(file.getAbsolutePath());
        int origem = 1;
        int destino = graph.n;

        // aquecimento simples para reduzir ruido de inicializacao da JVM
        Dijkstra.Result warmup = Dijkstra.shortestPath(graph, origem, destino);
        if (!warmup.found) {
            throw new IOException("nao foi encontrado caminho no arquivo: " + file.getName());
        }

        double total = 0.0;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        Dijkstra.Result last = warmup;

        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            Dijkstra.Result result = Dijkstra.shortestPath(graph, origem, destino);
            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;
            total += elapsedMs;
            min = Math.min(min, elapsedMs);
            max = Math.max(max, elapsedMs);
            last = result;
        }

        int size = extractSize(file);
        int edgeCount = 0;
        for (int i = 1; i <= graph.n; i++) {
            edgeCount += graph.adj.get(i).size();
        }

        return new BenchmarkRow(
                file.getName(),
                size,
                graph.n,
                edgeCount,
                last.found,
                last.distance,
                last.edges,
                total / REPETITIONS,
                min,
                max,
                last.path.size());
    }

    private static void printRow(BenchmarkRow row) {
        System.out.println("=== " + row.fileName + " ===");
        System.out.println("Vertices: " + row.vertices);
        System.out.println("Arestas: " + row.edges);
        System.out.println("Caminho encontrado: " + (row.found ? "sim" : "nao"));
        System.out.println("Distancia total: " + formatNumber(row.distance));
        System.out.println("Arestas no caminho: " + row.pathEdges);
        System.out.println("Tempo medio (ms): " + formatMs(row.avgMs));
        System.out.println("Tempo minimo (ms): " + formatMs(row.minMs));
        System.out.println("Tempo maximo (ms): " + formatMs(row.maxMs));
        System.out.println();
    }

    private static void writeCsv(File out, List<BenchmarkRow> rows) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            bw.write("arquivo,vertices,arestas,caminho_encontrado,distancia_total,arestas_no_caminho,tempo_medio_ms,tempo_min_ms,tempo_max_ms");
            bw.newLine();
            for (BenchmarkRow row : rows) {
                bw.write(row.fileName);
                bw.write(',');
                bw.write(Integer.toString(row.vertices));
                bw.write(',');
                bw.write(Integer.toString(row.edges));
                bw.write(',');
                bw.write(Boolean.toString(row.found));
                bw.write(',');
                bw.write(formatNumber(row.distance));
                bw.write(',');
                bw.write(Integer.toString(row.pathEdges));
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
            bw.write("# Benchmark Dijkstra - " + label);
            bw.newLine();
            bw.newLine();
            bw.write("Medições do caminho minimo entre os vertices 1 e n, com " + REPETITIONS + " repeticoes por arquivo.");
            bw.newLine();
            bw.newLine();
            bw.write("| Arquivo | Vertices | Arestas | Caminho encontrado | Distancia total | Arestas no caminho | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |");
            bw.newLine();
            bw.write("|---|---:|---:|---|---:|---:|---:|---:|---:|");
            bw.newLine();
            for (BenchmarkRow row : rows) {
                bw.write("| " + row.fileName + " | " + row.vertices + " | " + row.edges + " | "
                        + (row.found ? "sim" : "nao") + " | " + formatNumber(row.distance) + " | "
                        + row.pathEdges + " | " + formatMs(row.avgMs) + " | " + formatMs(row.minMs) + " | "
                        + formatMs(row.maxMs) + " |");
                bw.newLine();
            }
        }
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

    private static String formatNumber(double value) {
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

    private static class BenchmarkRow {
        final String fileName;
        final int size;
        final int vertices;
        final int edges;
        final boolean found;
        final double distance;
        final int pathEdges;
        final double avgMs;
        final double minMs;
        final double maxMs;
        final int pathVertices;

        BenchmarkRow(String fileName, int size, int vertices, int edges, boolean found, double distance,
                     int pathEdges, double avgMs, double minMs, double maxMs, int pathVertices) {
            this.fileName = fileName;
            this.size = size;
            this.vertices = vertices;
            this.edges = edges;
            this.found = found;
            this.distance = distance;
            this.pathEdges = pathEdges;
            this.avgMs = avgMs;
            this.minMs = minMs;
            this.maxMs = maxMs;
            this.pathVertices = pathVertices;
        }
    }
}
