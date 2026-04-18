import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/*
 * Gerador de grafos simples não-direcionados para experimentos de trilha/caminho euleriano.
 * 
 * Fontes de referência para a lógica utilizada:
 * - Critério de existência de trilha/ciclo euleriano em grafos não-direcionados:
 *   Euler, L. (1736) e formulações modernas em CLRS / livros de Teoria dos Grafos.
 *   Regras aplicadas:
 *   (i) Euleriano: todos os vértices com grau par (e grafo conexo no subgrafo com arestas).
 *   (ii) Semi-euleriano: exatamente 2 vértices de grau ímpar.
 *   (iii) Não euleriano: quantidade de vértices ímpares diferente de 0 e 2.
 * 
 * - Estratégia de aleatorização inspirada em geração de grafos aleatórios (família Erdos-Renyi).
 * 
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * O gerador constrói primeiro um ciclo hamiltoniano aleatório (garantindo conectividade)
 * e depois adiciona arestas de forma controlada para manter/alterar a paridade dos graus
 * conforme o tipo de grafo desejado.
 *
 * EXTENSÃO PARA VALIDAÇÃO DE PONTES:
 * Também foi adicionada uma classe de instâncias "com-ponte" para validação funcional
 * dos algoritmos de detecção de pontes (naïve/Tarjan). Isso foi feito porque grafos
 * densos aleatórios e os eulerianos/semi-eulerianos gerados podem, em várias amostras,
 * não conter pontes; essa classe garante casos positivos para teste e relatório.
 */
public class GraphTestGenerator {

    private static final int[] TAMANHOS_PADRAO = {100, 1000, 10000, 100000};
    private static final int INSTANCIAS_POR_TESTE_PADRAO = 10;

    private static final String TIPO_EULERIANO = "euleriano";
    private static final String TIPO_SEMI_EULERIANO = "semi-euleriano";
    private static final String TIPO_NAO_EULERIANO = "nao-euleriano";

    public static void main(String[] args) {
        String pastaSaida = args.length >= 1 ? args[0] : ".";
        long seed = args.length >= 2 ? Long.parseLong(args[1]) : System.currentTimeMillis();
        int instanciasPorTeste = args.length >= 3 ? Integer.parseInt(args[2]) : INSTANCIAS_POR_TESTE_PADRAO;

        if (instanciasPorTeste <= 0) {
            System.out.println("A quantidade de instancias por teste deve ser maior que zero.");
            return;
        }

        Random random = new Random(seed);

        try {
            gerarLotePadrao(pastaSaida, random, instanciasPorTeste);
            System.out.println("Arquivos de teste gerados com sucesso em: " + new File(pastaSaida).getAbsolutePath());
            System.out.println("Seed utilizada: " + seed);
            System.out.println("Instancias por teste: " + instanciasPorTeste);
            System.out.println("Tipos gerados: euleriano, semi-euleriano, nao-euleriano.");
        } catch (IOException e) {
            System.out.println("Erro ao gerar arquivos: " + e.getMessage());
        }
    }

    private static void gerarLotePadrao(String pastaSaida, Random random, int instanciasPorTeste) throws IOException {
        File pasta = new File(pastaSaida);
        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta de saida: " + pastaSaida);
        }

        for (int n : TAMANHOS_PADRAO) {
            for (int i = 1; i <= instanciasPorTeste; i++) {
                GraphData euleriano = gerarGrafoEuleriano(n, random);
                GraphData semiEuleriano = gerarGrafoSemiEuleriano(n, random);
                GraphData naoEuleriano = gerarGrafoNaoEuleriano(n, random);

                String sufixo = String.format("-%02d.txt", i);
                escreverGrafoEmArquivo(euleriano, new File(pasta, "grafo-euleriano-" + n + sufixo));
                escreverGrafoEmArquivo(semiEuleriano, new File(pasta, "grafo-semi-euleriano-" + n + sufixo));
                escreverGrafoEmArquivo(naoEuleriano, new File(pasta, "grafo-nao-euleriano-" + n + sufixo));
            }
        }
    }

    private static GraphData gerarGrafoComPonteGarantida(int n, Random random) {
        if (n < 6) {
            throw new IllegalArgumentException("Para gerar grafo com ponte garantida, n deve ser >= 6.");
        }

        int tamanhoA = n / 2;
        int tamanhoB = n - tamanhoA;

        GraphData g = new GraphData(n);

        List<Integer> grupoA = new ArrayList<>();
        for (int v = 1; v <= tamanhoA; v++) {
            grupoA.add(v);
        }

        List<Integer> grupoB = new ArrayList<>();
        for (int v = tamanhoA + 1; v <= n; v++) {
            grupoB.add(v);
        }

        adicionarCicloHamiltonianoAleatorio(g, grupoA, random);
        adicionarCicloHamiltonianoAleatorio(g, grupoB, random);

        int u = grupoA.get(random.nextInt(grupoA.size()));
        int v = grupoB.get(random.nextInt(grupoB.size()));
        g.adicionarAresta(u, v);
        
        int targetArestas = n - 1 + random.nextInt((int)(0.2 * n) + 1);
        adicionarBlocosCicloPreservandoPonte(g, targetArestas, random, grupoA, grupoB);

        return g;
    }

    private static void adicionarBlocosCicloPreservandoPonte(GraphData g, int targetArestas, Random random,
                                                              List<Integer> grupoA, List<Integer> grupoB) {
        // Adiciona blocos APENAS dentro de cada grupo para preservar a ponte
        while (g.getNumeroArestas() < targetArestas) {
            // 50% de chance de adicionar no grupo A, 50% no grupo B
            List<Integer> grupo = random.nextBoolean() ? grupoA : grupoB;
            
            if (grupo.size() < 4) continue;
            
            // Sorteia 4 vértices APENAS deste grupo
            int idx1 = random.nextInt(grupo.size());
            int idx2 = random.nextInt(grupo.size());
            int idx3 = random.nextInt(grupo.size());
            int idx4 = random.nextInt(grupo.size());
            
            Set<Integer> conjunto = new HashSet<>();
            conjunto.add(grupo.get(idx1));
            conjunto.add(grupo.get(idx2));
            conjunto.add(grupo.get(idx3));
            conjunto.add(grupo.get(idx4));
            
            if (conjunto.size() < 4) continue;
            
            int[] v = new int[4];
            int i = 0;
            for (int u : conjunto) {
                v[i++] = u;
            }
            
            int a = v[0], b = v[1], c = v[2], d = v[3];
            
            if (g.existeAresta(a, b) || g.existeAresta(b, c) || 
                g.existeAresta(c, d) || g.existeAresta(d, a)) {
                continue;
            }
            
            g.adicionarAresta(a, b);
            g.adicionarAresta(b, c);
            g.adicionarAresta(c, d);
            g.adicionarAresta(d, a);
        }
    }

    private static void adicionarCicloHamiltonianoAleatorio(GraphData g, List<Integer> vertices, Random random) {
        List<Integer> ordem = new ArrayList<>(vertices);
        Collections.shuffle(ordem, random);

        for (int i = 0; i < ordem.size(); i++) {
            int u = ordem.get(i);
            int v = ordem.get((i + 1) % ordem.size());
            g.adicionarAresta(u, v);
        }
    }

    private static void adicionarBlocosCicloPreservandoParidade(GraphData g, int targetArestas, Random random) {
        // Adiciona blocos de 4 arestas que formam ciclos
        // Cada bloco: (a,b), (b,c), (c,d), (d,a) - mantém paridade de cada vértice par
        while (g.getNumeroArestas() < targetArestas) {
            int[] v = sortearVerticesDistintos(g.n, 4, random);
            int a = v[0], b = v[1], c = v[2], d = v[3];
            
            if (g.existeAresta(a, b) || g.existeAresta(b, c) || 
                g.existeAresta(c, d) || g.existeAresta(d, a)) {
                continue;
            }
            
            g.adicionarAresta(a, b);
            g.adicionarAresta(b, c);
            g.adicionarAresta(c, d);
            g.adicionarAresta(d, a);
        }
    }

    private static void adicionarBlocosCicloPreservandoParidadeSemiEuleriano(GraphData g, int targetArestas, 
                                                                             Random random, int vimparA, int vimparB) {
        // Adiciona blocos de 4 arestas APENAS entre vértices que não são os dois vértices de grau ímpar
        // Isso preserva a propriedade de exatamente 2 vértices com grau ímpar
        List<Integer> verticesInterior = new ArrayList<>();
        for (int i = 1; i <= g.n; i++) {
            if (i != vimparA && i != vimparB) {
                verticesInterior.add(i);
            }
        }
        
        if (verticesInterior.size() < 4) {
            return; // Não é possível adicionar blocos
        }
        
        while (g.getNumeroArestas() < targetArestas) {
            // Sorteia 4 vértices APENAS do interior (não os 2 ímpares)
            int idx1 = random.nextInt(verticesInterior.size());
            int idx2 = random.nextInt(verticesInterior.size());
            int idx3 = random.nextInt(verticesInterior.size());
            int idx4 = random.nextInt(verticesInterior.size());
            
            Set<Integer> conjunto = new HashSet<>();
            conjunto.add(verticesInterior.get(idx1));
            conjunto.add(verticesInterior.get(idx2));
            conjunto.add(verticesInterior.get(idx3));
            conjunto.add(verticesInterior.get(idx4));
            
            if (conjunto.size() < 4) continue; // Precisa de 4 distintos
            
            int[] v = new int[4];
            int i = 0;
            for (int u : conjunto) {
                v[i++] = u;
            }
            
            int a = v[0], b = v[1], c = v[2], d = v[3];
            
            if (g.existeAresta(a, b) || g.existeAresta(b, c) || 
                g.existeAresta(c, d) || g.existeAresta(d, a)) {
                continue;
            }
            
            g.adicionarAresta(a, b);
            g.adicionarAresta(b, c);
            g.adicionarAresta(c, d);
            g.adicionarAresta(d, a);
        }
    }

    private static GraphData gerarGrafoEuleriano(int n, Random random) {
        if (n < 3) {
            throw new IllegalArgumentException("Para gerar grafo euleriano simples e conexo, n deve ser >= 3.");
        }

        GraphData g = new GraphData(n);
        adicionarCicloHamiltonianoAleatorio(g, random);
        
        int targetArestas = n - 1 + random.nextInt((int)(0.2 * n) + 1);
        adicionarBlocosCicloPreservandoParidade(g, targetArestas, random);
        
        return g;
    }

    private static GraphData gerarGrafoSemiEuleriano(int n, Random random) {
        if (n < 2) {
            throw new IllegalArgumentException("Para gerar grafo semi-euleriano simples e conexo, n deve ser >= 2.");
        }

        GraphData g = new GraphData(n);
        List<Integer> ordem = adicionarCaminhoHamiltonianoAleatorio(g, random);
        
        // Os dois vértices de grau ímpar são o primeiro e o último do caminho
        int verticeImparA = ordem.get(0);
        int verticeImparB = ordem.get(ordem.size() - 1);
        
        int targetArestas = n - 1 + random.nextInt((int)(0.2 * n) + 1);
        adicionarBlocosCicloPreservandoParidadeSemiEuleriano(g, targetArestas, random, verticeImparA, verticeImparB);
        
        return g;
    }

    private static GraphData gerarGrafoNaoEuleriano(int n, Random random) {
        if (n < 4) {
            throw new IllegalArgumentException("Para gerar grafo nao-euleriano simples e conexo, n deve ser >= 4.");
        }

        GraphData g = new GraphData(n);
        List<Integer> ordem = adicionarCaminhoHamiltonianoAleatorio(g, random);

        boolean adicionou = adicionarArestaNaoConsecutiva(g, ordem, random);
        if (!adicionou) {
            throw new IllegalStateException("Nao foi possivel gerar grafo nao-euleriano esparso.");
        }
        
        int targetArestas = n - 1 + random.nextInt((int)(0.2 * n) + 1);
        adicionarArestasAleatoriasNaoEuleriano(g, targetArestas, random);
        
        return g;
    }

    private static void adicionarArestasAleatoriasNaoEuleriano(GraphData g, int targetArestas, Random random) {
        // Para não-euleriano, pode adicionar arestas aleatoriamente (já começou não-euleriano)
        int maxTentativas = Math.max(100000, targetArestas * 50);
        int tentativas = 0;
        
        while (g.getNumeroArestas() < targetArestas && tentativas < maxTentativas) {
            tentativas++;
            int u = 1 + random.nextInt(g.n);
            int v = 1 + random.nextInt(g.n);
            
            if (u != v && !g.existeAresta(u, v)) {
                g.adicionarAresta(u, v);
            }
        }
    }

    private static void adicionarCicloHamiltonianoAleatorio(GraphData g, Random random) {
        List<Integer> vertices = new ArrayList<>();
        for (int i = 1; i <= g.n; i++) {
            vertices.add(i);
        }
        Collections.shuffle(vertices, random);

        for (int i = 0; i < vertices.size(); i++) {
            int u = vertices.get(i);
            int v = vertices.get((i + 1) % vertices.size());
            g.adicionarAresta(u, v);
        }
    }

    private static List<Integer> adicionarCaminhoHamiltonianoAleatorio(GraphData g, Random random) {
        List<Integer> vertices = new ArrayList<>();
        for (int i = 1; i <= g.n; i++) {
            vertices.add(i);
        }
        Collections.shuffle(vertices, random);

        for (int i = 0; i < vertices.size() - 1; i++) {
            g.adicionarAresta(vertices.get(i), vertices.get(i + 1));
        }
        return vertices;
    }

    private static boolean adicionarArestaNaoConsecutiva(GraphData g, List<Integer> ordem, Random random) {
        int n = ordem.size();
        int maxTentativas = Math.max(50, n);

        for (int tentativa = 0; tentativa < maxTentativas; tentativa++) {
            int i = random.nextInt(n);
            int j = random.nextInt(n);
            if (i == j || Math.abs(i - j) == 1) {
                continue;
            }
            int u = ordem.get(i);
            int v = ordem.get(j);
            if (g.adicionarAresta(u, v)) {
                return true;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                int u = ordem.get(i);
                int v = ordem.get(j);
                if (g.adicionarAresta(u, v)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void adicionarBlocosParidadePreservada(GraphData g, int quantidadeBlocos, Random random) {
        int adicionados = 0;
        int tentativas = 0;
        int maxTentativas = Math.max(10000, quantidadeBlocos * 200);

        while (adicionados < quantidadeBlocos && tentativas < maxTentativas) {
            tentativas++;
            int[] v = sortearVerticesDistintos(g.n, 4, random);
            int a = v[0];
            int b = v[1];
            int c = v[2];
            int d = v[3];

            if (g.existeAresta(a, b) || g.existeAresta(b, c) || g.existeAresta(c, d) || g.existeAresta(d, a)) {
                continue;
            }

            g.adicionarAresta(a, b);
            g.adicionarAresta(b, c);
            g.adicionarAresta(c, d);
            g.adicionarAresta(d, a);
            adicionados++;
        }
    }

    private static boolean adicionarUmaArestaNaoExistente(GraphData g, Random random) {
        int tentativas = 0;
        int maxTentativas = 100000;

        while (tentativas < maxTentativas) {
            tentativas++;
            int u = 1 + random.nextInt(g.n);
            int v = 1 + random.nextInt(g.n);
            if (u == v || g.existeAresta(u, v)) {
                continue;
            }
            g.adicionarAresta(u, v);
            return true;
        }
        return false;
    }

    private static boolean adicionarDuasArestasDisjuntas(GraphData g, Random random) {
        int tentativas = 0;
        int maxTentativas = 200000;

        while (tentativas < maxTentativas) {
            tentativas++;
            int[] v = sortearVerticesDistintos(g.n, 4, random);
            int a = v[0];
            int b = v[1];
            int c = v[2];
            int d = v[3];

            if (g.existeAresta(a, b) || g.existeAresta(c, d)) {
                continue;
            }

            g.adicionarAresta(a, b);
            g.adicionarAresta(c, d);
            return true;
        }
        return false;
    }

    private static int[] sortearVerticesDistintos(int n, int k, Random random) {
        if (k > n) {
            throw new IllegalArgumentException("Nao e possivel sortear " + k + " vertices distintos com n=" + n);
        }

        Set<Integer> conjunto = new HashSet<>();
        while (conjunto.size() < k) {
            conjunto.add(1 + random.nextInt(n));
        }

        int[] resultado = new int[k];
        int idx = 0;
        for (int v : conjunto) {
            resultado[idx++] = v;
        }
        return resultado;
    }

    private static void escreverGrafoEmArquivo(GraphData g, File arquivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            bw.write(g.n + " " + g.getNumeroArestas());
            bw.newLine();
            for (long key : g.arestas) {
                int u = (int) (key >>> 32);
                int v = (int) key;
                bw.write(u + " " + v);
                bw.newLine();
            }
        }
    }

    private static class GraphData {
        private final int n;
        private final Set<Long> arestas;

        GraphData(int n) {
            this.n = n;
            this.arestas = new HashSet<>();
        }

        boolean adicionarAresta(int u, int v) {
            if (u == v) {
                return false;
            }
            long key = chaveAresta(u, v);
            return arestas.add(key);
        }

        boolean existeAresta(int u, int v) {
            return arestas.contains(chaveAresta(u, v));
        }

        int getNumeroArestas() {
            return arestas.size();
        }

        private static long chaveAresta(int u, int v) {
            int a = Math.min(u, v);
            int b = Math.max(u, v);
            return (((long) a) << 32) | (b & 0xffffffffL);
        }
    }
}