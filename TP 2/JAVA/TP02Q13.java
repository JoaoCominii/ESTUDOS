
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Show {

    private String Show_ID;
    private String Type;
    private String Title;
    private String Director;
    private String[] Cast;
    private String Country;
    private String Date_Added;
    private int Release_Year;
    private String Rating;
    private String Duration;
    private String[] Listed_In;

    // Construtor padrao
    public Show() {
        this.Show_ID = "NaN";
        this.Type = "NaN";
        this.Title = "NaN";
        this.Director = "NaN";
        this.Cast = new String[0];
        this.Country = "NaN";
        this.Date_Added = "NaN";
        this.Release_Year = 0;
        this.Rating = "NaN";
        this.Duration = "NaN";
        this.Listed_In = new String[0];
    }

    // Construtor com parametros
    public Show(String Show_ID, String Type, String Title, String Director, String[] Cast, String Country, String date_Added2, int Release_Year, String Rating, String Duration, String[] Listed_In) {
        this.Show_ID = Show_ID != null ? Show_ID : "NaN";
        this.Type = Type != null ? Type : "NaN";
        this.Title = Title != null ? Title : "NaN";
        this.Director = Director != null ? Director : "NaN";
        this.Cast = Cast != null ? Cast : new String[0];
        this.Country = Country != null ? Country : "NaN";
        this.Date_Added = date_Added2 != null ? date_Added2 : "NaN";
        this.Release_Year = Release_Year > 0 ? Release_Year : 0;
        this.Rating = Rating != null ? Rating : "NaN";
        this.Duration = Duration != null ? Duration : "NaN";
        this.Listed_In = Listed_In != null ? Listed_In : new String[0];
        // Ordena alfabeticamente os arrays Cast e Listed_In com ordenação BubbleSort
        ordenarBubbleSort(this.Cast);
        ordenarBubbleSort(this.Listed_In);
    }

    // Getters e Setters
    public String getShow_ID() {
        return Show_ID;
    }

    public void setShow_ID(String Show_ID) {
        this.Show_ID = Show_ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public String[] getCast() {
        return Cast;
    }

    public void setCast(String[] Cast) {
        this.Cast = Cast;
        ordenarBubbleSort(this.Cast);
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getDate_Added() {
        return Date_Added;
    }

    public void setDate_Added(String Date_Added) {
        this.Date_Added = Date_Added;
    }

    public int getRelease_Year() {
        return Release_Year;
    }

    public void setRelease_Year(int Release_Year) {
        this.Release_Year = Release_Year;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String Duration) {
        this.Duration = Duration;
    }

    public String[] getListed_In() {
        return Listed_In;
    }

    public void setListed_In(String[] Listed_In) {
        this.Listed_In = Listed_In;
        ordenarBubbleSort(this.Listed_In);
    }

    // Método imprimir: formata os dados do Show para exibição
    public String imprimir() {
        // Remove os espaços de cada elemento do array Cast e Listed_In
        String castFormatado = "[" + String.join(", ", removerEspacos(this.Cast)) + "]";
        String listedInFormatado = "[" + String.join(", ", removerEspacos(this.Listed_In)) + "]";

        // Retorna a string formatada com os arrays, mesmo quando estão vazios com as informações do Show
        return ("=> " + getShow_ID().trim() + " ## " + getTitle().trim() + " ## " + getType().trim() + " ## " + getDirector().trim() + " ## "
                + castFormatado + " ## " + getCountry().trim() + " ## " + Date_Added.trim()
                + " ## " + getRelease_Year() + " ## " + getRating().trim() + " ## " + getDuration().trim() + " ## "
                + listedInFormatado + " ##");
    }

    // Método auxiliar para remover espaços dos elementos de um array de strings
    private String[] removerEspacos(String[] array) {
        String[] resultado = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            resultado[i] = array[i].trim(); // Remove espaços do início e do final de cada string
        }
        return resultado;
    }

    // Método clone: cria uma cópia do objeto Show
    public String clone() {
        Show cloneShow = new Show(this.Show_ID, this.Type, this.Title, this.Director, this.Cast, this.Country, this.Date_Added, this.Release_Year, this.Rating, this.Duration, this.Listed_In);
        return cloneShow.imprimir();
    }

    // Método para ler os dados do arquivo CSV
    public static Show[] Ler() {
        String caminhoArquivo = "/tmp/disneyplus.csv";
        Show[] shows = new Show[1368]; // Definindo tamanho inicial do array
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null && contador < 1368) {
                if (primeiraLinha) { // Ignora cabeçalho
                    primeiraLinha = false;
                    continue;
                }

                // Usa regex para separar os valores corretamente, considerando aspas
                String[] valores = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Tratamento de valores ausentes
                for (int i = 0; i < valores.length; i++) {
                    valores[i] = valores[i].replaceAll("^\"|\"$", "").trim(); // Remove aspas extras
                    if (valores[i].isEmpty()) {
                        valores[i] = "NaN"; // Substitui valores vazios por "NaN"
                    }
                }

                // Separa e cria um objeto Show
                String[] cast = valores[4].split(",");
                String[] listedIn = valores[10].split(",");
                Show show = new Show(
                        valores[0], // Show_ID
                        valores[1], // Type
                        valores[2], // Title
                        valores[3], // Director
                        cast, // Cast
                        valores[5], // Country
                        valores[6], // Date_Added
                        Integer.parseInt(valores[7]), // Release_Year
                        valores[8], // Rating
                        valores[9], // Duration
                        listedIn // Listed_In
                );

                // Adiciona o objeto no array
                shows[contador++] = show;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter ano de lançamento: " + e.getMessage());
        }

        return shows;
    }

    /*
* Método ordenarBubbleSort:
 * Ordena um array de strings em ordem alfabética, removendo espaços e convertendo para minúsculas.
 * O Bubble Sort (Ordenação por Bolha) é um algoritmo de ordenação simples e intuitivo 
 * Que funciona ao comparar pares consecutivos de elementos em um array e realizar trocas quando necessário, até que o array esteja completamente ordenado.
 * A cada "passagem", os maiores elementos "flutuam" para o topo (final do array), como bolhas em um líquido.
 * Durante uma passagem, dois elementos consecutivos são comparados array[j] e array[j + 1].
 * Se o elemento atual (array[j]) for maior que o próximo (array[j + 1]), eles são trocados de lugar.
 * O processo continua até que o array esteja ordenado, ou seja, não haja mais trocas necessárias.
 * O algoritmo Bubble Sort é simples, mas não é o mais eficiente para grandes conjuntos de dados.
 * Ele tem complexidade de tempo O(n^2) no pior caso, onde n é o número de elementos no array.
     */
    private void ordenarBubbleSort(String[] array) {
        int n = array.length;
        // Passagens pelo array (n vezes no máximo)
        for (int i = 0; i < n - 1; i++) {
            // Comparações entre elementos consecutivos (n-i-1 vezes no máximo)
            // O último elemento já está na posição correta após cada passagem
            for (int j = 0; j < n - i - 1; j++) {
                // Remove espaços e força para minúsculas para comparação
                String atual = array[j].trim().toLowerCase();
                String proximo = array[j + 1].trim().toLowerCase();

                // Troca os elementos se estiverem fora de ordem
                if (atual.compareTo(proximo) > 0) { // Verifica a ordem alfabética
                    // Troca os elementos
                    String temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

}

public class TP02Q13 {

    // Método principal de ordenação
    /*
     * * Ordena um array de objetos Show usando o algoritmo Merge Sort.
     * * * O Merge Sort é um algoritmo de ordenação eficiente e estável que utiliza a técnica de divisão e conquista.
     * * * Ele divide o array em duas metades, ordena cada metade recursivamente e, em seguida, intercala as duas metades ordenadas.
     * * * * O Merge Sort tem complexidade de tempo O(n log n) no pior caso, onde n é o número de elementos no array.
     * * * * * O algoritmo é eficiente para grandes conjuntos de dados e é amplamente utilizado em aplicações práticas.
     * * * * * * * O Merge Sort é um algoritmo de ordenação estável, o que significa que mantém a ordem relativa dos elementos iguais.
     */
    public static void sort(Show[] shows, int n) {
        int[] contadores = new int[2]; // [0] -> Comparações, [1] -> Movimentações
        long inicio = System.nanoTime(); // Marca o tempo de início

        // Realiza o Merge Sort
        mergesort(shows, 0, n - 1, contadores);

        // Calcula o tempo total de execução
        long fim = System.nanoTime();
        long tempoTotal = fim - inicio;

        // Cria o arquivo de log
        try (FileWriter writer = new FileWriter("matricula_mergesort.txt")) {
            writer.write("00846713\t" + "Comparações: " + contadores[0] + "\t" +
                         "Movimentações: " + contadores[1] + "\t" +
                         "TempoTotal: " + tempoTotal + "ns");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    private static void mergesort(Show[] shows, int esq, int dir, int[] contadores) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergesort(shows, esq, meio, contadores); // Ordena a metade esquerda
            mergesort(shows, meio + 1, dir, contadores); // Ordena a metade direita
            intercalar(shows, esq, meio, dir, contadores); // Intercala as duas metades
        }
    }

    private static void intercalar(Show[] shows, int esq, int meio, int dir, int[] contadores) {
        int n1 = meio - esq + 1; // Tamanho do subarray esquerdo
        int n2 = dir - meio; // Tamanho do subarray direito

        Show[] a1 = new Show[n1 + 1]; // Subarray esquerdo
        Show[] a2 = new Show[n2 + 1]; // Subarray direito

        // Preenche o subarray esquerdo
        for (int i = 0; i < n1; i++) {
            contadores[1]++; // Movimentações: preenchendo o array
            a1[i] = shows[esq + i];
        }

        // Preenche o subarray direito
        for (int j = 0; j < n2; j++) {
            contadores[1]++; // Movimentações: preenchendo o array
            a2[j] = shows[meio + 1 + j];
        }

        // Sentinelas no final dos subarrays
        a1[n1] = null;
        a2[n2] = null;

        // Intercalação
        int i = 0, j = 0;
        for (int k = esq; k <= dir; k++) {
            contadores[0]++; // Comparações: entre elementos dos subarrays
            if (comparaShows(a1[i], a2[j]) <= 0) {
                shows[k] = a1[i++];
            } else {
                shows[k] = a2[j++];
            }
            contadores[1]++; // Movimentações: escrevendo no array final
        }
    }

    private static int comparaShows(Show s1, Show s2) {
        if (s1 == null) return 1; // `s1` é maior se `s2` não existe
        if (s2 == null) return -1; // `s2` é maior se `s1` não existe

        int duration1 = converterDuracaoParaMinutos(s1.getDuration());
        int duration2 = converterDuracaoParaMinutos(s2.getDuration());

        // Comparação por duração
        int comparacaoDuration = Integer.compare(duration1, duration2);
        if (comparacaoDuration != 0) {
            return comparacaoDuration;
        }

        // Desempate por título
        return s1.getTitle().compareTo(s2.getTitle());
    }

    private static int converterDuracaoParaMinutos(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0; // Duração inválida será tratada como 0
        }

        duration = duration.toLowerCase();

        if (duration.contains("h")) {
            String[] partes = duration.split("h");
            int horas = Integer.parseInt(partes[0].trim());
            int minutos = partes.length > 1 ? Integer.parseInt(partes[1].replace("m", "").trim()) : 0;
            return horas * 60 + minutos;
        } else if (duration.contains("min")) {
            return Integer.parseInt(duration.replace("min", "").trim());
        }

        return 0; // Caso nenhum formato seja reconhecido
    }


    public static void main(String[] args) {

        // Carrega todos os Shows do arquivo CSV
        Show[] todosShows = Show.Ler();

        // Array para armazenar os 300 Shows
        Show[] showsSelecionados = new Show[300];
        int contador = 0;

        // Usa Scanner para ler os IDs do teclado
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String id = scanner.nextLine();
            if (id.equals("FIM")) { // Finaliza ao receber "FIM"
                break;
            }

            for (Show show : todosShows) {
                if (show != null && show.getShow_ID().equals(id)) {
                    showsSelecionados[contador++] = show;
                    break; // Interrompe o laço ao encontrar o show correspondente
                }
            }
        }

        // Ordena os Shows selecionados pelo título
        sort(showsSelecionados, contador);

        // Exibe os Shows selecionados usando o método imprimir
        for (int i = 0; i < contador; i++) {
            System.out.println(showsSelecionados[i].imprimir());
        }
        scanner.close();
    }
}
