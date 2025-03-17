

class ListaLinear{
    private int[]array;
    private int n; 

    // Construtor
    public ListaLinear(int tamanho){
        array = new int[tamanho];
        n = 0;
    }

    // Insere o elemento no inicio da lista
    public void inserirInicio(int x) {
        if(n >= array.length)
        {
            System.out.println("Erro ao inserir.");
        }
        for(int i = 0; i < n; i++)
        {
            array[i + 1] = array[i];
        }
        array[0] = x;
        n++;
    }

    // Insere o elemento no final da lista
    public void inserirFim(int x){
        if(n >= array.length)
        {
            System.out.println("Erro ao inserir.");
        }
        array[n] = x;
        n++;
    }

    // Insere o elemento na posicao pos da lista
    public void inserir(int x, int pos){
        if(n >= array.length || pos < 0 || pos > n)
        {
            System.out.println("Erro ao inserir.");
        }
        for(int i = n; i < pos; i--)
        {
            array[i] = array[i- 1];
        }
        array[pos] = x;
        n++;
    }

    // Remove e retorna o elemento do inicio da lista
    public int removerInicio() {
        if(n <= 0)
        {
            System.out.println("Erro nenhum elemento registrado.");
        }
        int resp = array[0];
        n--;
        for(int i = 0; i < n; i++)
        {
            array[i] = array[i+1];
        }

        return resp;
    }

    // Remove e retorna o elemnto do fim da lista
    public int removerFim(){
        if(n <= 0)
        {
            System.out.println("Erro nenhum elemento registrado.");
        }

        return array[--n];
    }

    //Remove e retorna o elemento na posicao pos da lista
    public int remover(int pos){
        if(n <= 0 || pos >= n || pos < 0)
        {
            System.out.println("Erro nenhum elemento registrado.");
        }
        int resp = array[pos];
        n--;
        for(int i = pos; i < n; i++)
        {
            array[i] = array[i + 1];
        }

        return resp;
    }

// Imprime os elementos da lista, metodo iterativo
public void mostrar(){
    for(int i = 0; i < array.length; i++)
    {
        System.out.print(array[i] + " ");
    }
}

// pesquisa se o elemento esta presente na lista, retornando verdadeiro em caso afirmativo
public boolean pesquisar(int x){
    boolean achou = false;
    for(int i = 0; i < n; i++)
    {
        if(array[i] == x)
        {
            achou = true;
            i = n;
        }
    }

    return achou;
}



}