import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Estrutura escolhida: Lista de Adjacência.
 *
 * Foram criadas duas listas de adjacência:
 *  - Uma para armazenar os sucessores (arestas de saída).
 *  - Outra para armazenar os predecessores (arestas de entrada).
 *
 * Essa decisão foi tomada para permitir que o grau de entrada e o conjunto de predecessores 
 * de um vértice sejam obtidos diretamente, sem a necessidade de percorrer toda a lista de sucessores.
 *
 */

public class RepresentacaoGrafo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo de entrada:");
        String nomeArquivo = scanner.nextLine();
        System.out.print("Digite o número do vértice desejado:");
        int verticeDesejado = scanner.nextInt();

        try{
            BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
            String primeiraLinha = br.readLine();
            /*
            * divide a string onde houver um ou mais espaços consecutivos.
            * \s  → representa qualquer espaço em branco e +   → significa "uma ou mais ocorrências"
            */ 
            String[] valores = primeiraLinha.trim().split("\\s+"); 
            int n = Integer.parseInt(valores[0]);
            int m = Integer.parseInt(valores[1]);

            // Listas de adjacência
            // Lista de sucessores
            List<List<Integer>> adjSaida = new ArrayList<>();
            // Lista de predecessores
             List<List<Integer>> adjEntrada = new ArrayList<>();

            // Criar uma lista para cada vértice
            for(int i = 0; i <= n; i++)
            {
                adjSaida.add(new ArrayList<>());
                adjEntrada.add(new ArrayList<>());
            }

            // Preencher as listas de cada vértice
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split("\\s+");
                int origem = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);

                adjSaida.get(origem).add(destino);
                adjEntrada.get(destino).add(origem);
            }
            br.close();

            if (verticeDesejado < 1 || verticeDesejado > n) {
                System.out.println("Vértice inválido.");
                return;
            }
            
            System.err.println("Informações do vertice:" + verticeDesejado);
            // Grau de saída
            int grauSaida = adjSaida.get(verticeDesejado).size();
            // Grau de entrada
            int grauEntrada = adjEntrada.get(verticeDesejado).size();
            System.out.println("Grau de saída: " + grauSaida);
            System.out.println("Grau de entrada: " + grauEntrada);

            System.out.println("Sucessores: " + adjSaida.get(verticeDesejado));
            System.out.println("Predecessores: " + adjEntrada.get(verticeDesejado));
        } catch (IOException e) {
        }
    }
}
