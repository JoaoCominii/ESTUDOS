class Celula {

    public Show elemento;
    public Celula prox;

    /**
     * Construtor da classe sem parâmetros.
     */
    public Celula() {
        this.elemento = null;
        this.prox = null;
    }

    /**
     * Construtor da classe com um elemento do tipo Show.
     *
     * @param elemento Show inserido na célula.
     */
    public Celula(Show elemento) {
        this.elemento = elemento;
        this.prox = null;
    }
}