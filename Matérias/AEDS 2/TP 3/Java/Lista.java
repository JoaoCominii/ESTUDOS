class Lista {

    private Celula primeiro;
    private Celula ultimo;

    /**
     * Construtor da classe que cria uma lista sem elementos (somente o
     * cabeçalho).
     */
    public Lista() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    /**
     * Insere um elemento na primeira posição da lista.
     *
     * @param show Show a ser inserido.
     */
    public void inserirInicio(Show show) {
        Celula tmp = new Celula(show);
        tmp.prox = primeiro.prox;
        primeiro.prox = tmp;
        if (primeiro == ultimo) {
            ultimo = tmp;
        }
        tmp = null;
    }

    /**
     * Insere um elemento na última posição da lista.
     *
     * @param show Show a ser inserido.
     */
    public void inserirFim(Show show) {
        ultimo.prox = new Celula(show);
        ultimo = ultimo.prox;
    }

    /**
     * Remove um elemento da primeira posição da lista.
     *
     * @return Show elemento removido.
     * @throws Exception Se a lista estiver vazia.
     */
    public Show removerInicio() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (lista vazia)!");
        }

        Celula tmp = primeiro;
        primeiro = primeiro.prox;
        Show resp = primeiro.elemento;
        tmp.prox = null;
        tmp = null;
        return resp;
    }

    /**
     * Remove um elemento da última posição da lista.
     *
     * @return Show elemento removido.
     * @throws Exception Se a lista estiver vazia.
     */
    public Show removerFim() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (lista vazia)!");
        }

        // Caminha até a penúltima célula
        Celula i;
        for (i = primeiro; i.prox != ultimo; i = i.prox);

        Show resp = ultimo.elemento;
        ultimo = i;
        i = ultimo.prox = null;

        return resp;
    }

    /**
     * Insere um elemento em uma posição específica.
     *
     * @param show Show a ser inserido.
     * @param pos Posição de inserção.
     * @throws Exception Se a posição for inválida.
     */
    public void inserir(Show show, int pos) throws Exception {
        int tamanho = tamanho();

        if (pos < 0 || pos > tamanho) {
            throw new Exception("Erro ao inserir na posição (" + pos + " / tamanho = " + tamanho + ") inválida!");
        } else if (pos == 0) {
            inserirInicio(show);
        } else if (pos == tamanho) {
            inserirFim(show);
        } else {
            Celula i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox);

            Celula tmp = new Celula(show);
            tmp.prox = i.prox;
            i.prox = tmp;
            tmp = i = null;
        }
    }

    /**
     * Remove um elemento de uma posição específica.
     *
     * @param pos Posição da remoção.
     * @return Show elemento removido.
     * @throws Exception Se a posição for inválida.
     */
    public Show remover(int pos) throws Exception {
        Show resp;
        int tamanho = tamanho();

        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover (lista vazia)!");
        } else if (pos < 0 || pos >= tamanho) {
            throw new Exception("Erro ao remover (posição " + pos + " / " + tamanho + " inválida!)");
        } else if (pos == 0) {
            resp = removerInicio();
        } else if (pos == tamanho - 1) {
            resp = removerFim();
        } else {
            Celula i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox);

            Celula tmp = i.prox;
            resp = tmp.elemento;
            i.prox = tmp.prox;
            tmp.prox = null;
            i = tmp = null;
        }

        return resp;
    }

    /**
     * Mostra os elementos da lista separados por espaços.
     */
    public void mostrar() {
        System.out.print("[ ");
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            System.out.print(i.elemento.getTitle() + " ");
        }
        System.out.println("] ");
    }

    /**
     * Procura um elemento e retorna se ele existe.
     *
     * @param titulo Título do Show a pesquisar.
     * @return true se o elemento existir, false caso contrário.
     */
    public boolean pesquisar(String titulo) {
        boolean resp = false;
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            if (i.elemento.getTitle().equals(titulo)) {
                resp = true;
                i = ultimo;
            }
        }
        return resp;
    }

    /**
     * Calcula e retorna o tamanho da lista.
     *
     * @return Número de elementos na lista.
     */
    public int tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++);
        return tamanho;
    }
}