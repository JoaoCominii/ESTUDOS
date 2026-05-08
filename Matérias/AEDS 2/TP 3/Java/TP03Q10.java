import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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

    // Adicione este método estático na classe Show:
    public static String converterDataISO(String data) {
        if (data == null || data.equals("NaN")) return "0000-00-00";
        try {
            String[] partes = data.split(" ");
            if (partes.length < 3) return "0000-00-00";
            int dia = Integer.parseInt(partes[1].replace(",", ""));
            int ano = Integer.parseInt(partes[2]);
            int mes = 0;
            switch (partes[0]) {
                case "January": mes = 1; break;
                case "February": mes = 2; break;
                case "March": mes = 3; break;
                case "April": mes = 4; break;
                case "May": mes = 5; break;
                case "June": mes = 6; break;
                case "July": mes = 7; break;
                case "August": mes = 8; break;
                case "September": mes = 9; break;
                case "October": mes = 10; break;
                case "November": mes = 11; break;
                case "December": mes = 12; break;
                default: mes = 0;
            }
            return String.format("%04d-%02d-%02d", ano, mes, dia);
        } catch (Exception e) {
            return "0000-00-00";
        }
    }
}

class No {
    public Show show;
    public No ant, prox;

    public No(Show show) {
        this.show = show;
        this.ant = null;
        this.prox = null;
    }
}

class ListaDupla {
    private No inicio, fim;
    private int tamanho;

    public ListaDupla() {
        inicio = fim = null;
        tamanho = 0;
    }

    public void inserirFim(Show show) {
        No novo = new No(show);
        if (fim == null) {
            inicio = fim = novo;
        } else {
            fim.prox = novo;
            novo.ant = fim;
            fim = novo;
        }
        tamanho++;
    }

    public No getInicio() { return inicio; }
    public No getFim() { return fim; }
    public int getTamanho() { return tamanho; }

    public void mostrar() {
        for (No atual = inicio; atual != null; atual = atual.prox) {
            System.out.println(atual.show.imprimir());
        }
    }
}

public class TP03Q10 {
    private static int comparacoes = 0;
    private static int movimentacoes = 0;

    public static void main(String[] args) throws Exception {
        Show[] todosShows = Show.Ler();
        ListaDupla lista = new ListaDupla();
        Scanner scanner = new Scanner(System.in);

        // Leitura dos IDs iniciais até "FIM"
        while (true) {
            String id = scanner.nextLine().trim();
            if (id.equals("FIM")) break;
            Show novoShow = buscarShow(todosShows, id);
            if (novoShow != null) {
                lista.inserirFim(novoShow);
            }
        }

        long inicio = System.currentTimeMillis();

        if (lista.getTamanho() > 1)
            quicksortLista(lista.getInicio(), lista.getFim());

        long fim = System.currentTimeMillis();

        lista.mostrar();

        // Log
        PrintWriter log = new PrintWriter("00846713_quicksort3.txt");
        log.printf("00846713\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, (fim - inicio) / 1000.0);
        log.close();

        scanner.close();
    }

    // Quicksort para lista duplamente encadeada
    private static void quicksortLista(No esq, No dir) {
        if (dir != null && esq != dir && esq != dir.prox) {
            No p = particao(esq, dir);
            quicksortLista(esq, p.ant);
            quicksortLista(p.prox, dir);
        }
    }

    private static No particao(No esq, No dir) {
        Show pivo = dir.show;
        No i = esq.ant;
        for (No j = esq; j != dir; j = j.prox) {
            comparacoes++;
            if (compararShows(j.show, pivo) < 0) {
                i = (i == null) ? esq : i.prox;
                trocarNos(i, j);
                movimentacoes++;
            }
        }
        i = (i == null) ? esq : i.prox;
        trocarNos(i, dir);
        movimentacoes++;
        return i;
    }

    private static void trocarNos(No a, No b) {
        Show tmp = a.show;
        a.show = b.show;
        b.show = tmp;
    }

    // Compara por Date_Added (ISO), depois Title
    private static int compararShows(Show s1, Show s2) {
        String data1 = Show.converterDataISO(s1.getDate_Added());
        String data2 = Show.converterDataISO(s2.getDate_Added());
        int cmp = data1.compareTo(data2);
        if (cmp != 0) return cmp;
        return s1.getTitle().compareTo(s2.getTitle());
    }

    /** Método auxiliar para buscar um Show pelo ID */
    private static Show buscarShow(Show[] todosShows, String showID) {
        for (Show show : todosShows) {
            if (show != null && show.getShow_ID().equals(showID)) {
                return show;
            }
        }
        return null;
    }
}
