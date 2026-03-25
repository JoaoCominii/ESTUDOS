import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/*
 * Algoritmo de Busca em Profundidade (DFS) adaptado.
 * 
 * Fonte de referência para a lógica do DFS:
 * Introduction to Algorithms, Cormen et al. (CLRS) - Section 22.3 Depth-first search
 * 
 * NOTA SOBRE A IMPLEMENTAÇÃO:
 * A lógica original do CLRS é recursiva. No entanto, para suportar grafos muito grandes 
 * (como o teste de 50.000 vértices) sem causar estouro de pilha (StackOverflowError), 
 * o algoritmo foi adaptado para uma versão ITERATIVA utilizando uma pilha (Stack) explícita.
 * 
 * A lógica de cores (Branco, Cinza, Preto), tempos de descoberta/término e 
 * classificação de arestas permanece fidelíssima ao livro, apenas a estrutura 
 * de controle de fluxo foi alterada de recursão de sistema para pilha de dados.
 */

public class BuscaGrafo {
    // Variáveis globais para o DFS
    private static int tempo;
    private static int[] tempoDescoberta;
    private static int[] tempoTermino;
    private static int[] cor; 
    private static List<String> arestasArvore;
    private static List<String> classificacaoArestasEscolhidas;
    private static int verticeEscolhido;

    // Constantes para as cores
    private static final int BRANCO = 0;
    private static final int CINZA = 1;
    private static final int PRETO = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite o nome do arquivo de entrada: ");
        String nomeArquivo = scanner.nextLine();
        
        System.out.print("Digite o numero de um dos vertices do grafo: ");
        if (!scanner.hasNextInt()) {
             System.out.println("Entrada invalida.");
             scanner.close();
             return;
        }
        verticeEscolhido = scanner.nextInt();
        scanner.close();

        try {
            BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
            String primeiraLinha = br.readLine();
            if (primeiraLinha == null) {
                br.close();
                return;
            }
            
            String[] valores = primeiraLinha.trim().split("\\s+");
            int n = Integer.parseInt(valores[0]);
            int m = Integer.parseInt(valores[1]);

            if (verticeEscolhido < 1 || verticeEscolhido > n) {
                System.out.println("Vertice invalido. O grafo possui vertices de 1 a " + n + ".");
                br.close();
                return;
            }

            // Construção do Grafo (Lista de Adjacência)
            // Indice 0 é ignorado para facilitar uso de 1 a n
            List<List<Integer>> grafo = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                grafo.add(new ArrayList<>()); 
            }

            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] partes = linha.split("\\s+");
                int u = Integer.parseInt(partes[0]);
                int v = Integer.parseInt(partes[1]);
                grafo.get(u).add(v);
            }
            br.close();

            // Ordenar listas de adjacência para garantir ordem lexicográfica na visita
            for (int i = 1; i <= n; i++) {
                Collections.sort(grafo.get(i));
            }

            // Inicialização das estruturas do DFS
            tempo = 0;
            tempoDescoberta = new int[n + 1];
            tempoTermino = new int[n + 1];
            cor = new int[n + 1]; // Inicializa com 0 (BRANCO)
            arestasArvore = new ArrayList<>();
            classificacaoArestasEscolhidas = new ArrayList<>();

            // Executar DFS (percorrendo todos os vertices para garantir floresta completa)
            for (int u = 1; u <= n; u++) {
                if (cor[u] == BRANCO) {
                    dfs(u, grafo);
                }
            }

            // Exibir resultados
            System.out.println("Arestas de arvore encontradas:");
            for (String aresta : arestasArvore) {
                System.out.println(aresta);
            }

            System.out.println();
            System.out.println("Classificacao das arestas saindo do vertice " + verticeEscolhido + ":");
            
            if (classificacaoArestasEscolhidas.isEmpty()) {
                 if (grafo.get(verticeEscolhido).isEmpty()) {
                     // Caso não tenha sucessores
                 }
            } else {
                for (String classificacao : classificacaoArestasEscolhidas) {
                    System.out.println(classificacao);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato no arquivo de entrada.");
        }
    }

    private static void dfs(int raiz, List<List<Integer>> grafo) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> stackIndex = new Stack<>(); // mantém o controle do índice do vizinho atual
        
        // Empilha o nó inicial
        stack.push(raiz);
        stackIndex.push(0);
        
        // Lógica de descoberta para a raiz
        cor[raiz] = CINZA;
        tempo++;
        tempoDescoberta[raiz] = tempo;
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            int index = stackIndex.pop();
            
            List<Integer> vizinhos = grafo.get(u);
            
            if (index < vizinhos.size()) {
                // Se ainda houver vizinhos para verificar, salva o estado para retornar ao próximo vizinho
                stackIndex.push(index + 1);
                
                int v = vizinhos.get(index);
                
                // Classificação de Arestas
                if (u == verticeEscolhido) {
                    String tipo = "";
                    if (cor[v] == BRANCO) {
                        tipo = "Arvore";
                    } else if (cor[v] == CINZA) {
                        tipo = "Retorno";
                    } else { // PRETO
                        if (tempoDescoberta[u] < tempoDescoberta[v]) {
                            tipo = "Avanco";
                        } else {
                            tipo = "Cruzamento";
                        }
                    }
                    classificacaoArestasEscolhidas.add(u + " -> " + v + " : " + tipo);
                }

                if (cor[v] == BRANCO) {
                    arestasArvore.add(u + " -> " + v);
                    
                    // Simula a chamada recursiva
                    cor[v] = CINZA;
                    tempo++;
                    tempoDescoberta[v] = tempo;
                    
                    stack.push(v);
                    stackIndex.push(0);
                }
                // Se não for branco, o loop continua para o próximo vizinho imediatamente
            } else {
                // Todos os vizinhos processados, finaliza u
                stack.pop();
                cor[u] = PRETO;
                tempo++;
                tempoTermino[u] = tempo;
            }
        }
    }
}
