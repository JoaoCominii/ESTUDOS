// Fila Circular

public class FilaCircular {
    private int[] array;
    private int primeiro; // Remove do indice "primeiro".
    private int ultimo; // Insere no indice "ultimo".

    public FilaCircular (int max) // cria uma fila capaz de armazenar, no m´aximo, max elementos.
    {
        array = new int[tamanho + 1];
        primeiro = ultimo = 0;
    }
    public void enfileirar(int x) // insere o elemento na fila
    {
        if((ultimo + 1) % array.length == primeiro)
        {
            System.out.println("Erro, vetor cheio!");
        }
        else
        {
            array[ultimo] = x;
            ultimo = (ultimo + 1) % array.length;
        }
    }
    public int desinfileirar() // remove e retorna o elemento da fila
    {
        if(ultimo == primeiro)
        {
            System.out.println("Erro, array vazio!");
        }
        int resp = array[primeiro];
        primeiro = (primeiro + 1) % array.length;
        return resp;
    }
    public void mostrar() //  imprime os elementos da fila, na ordem que foram inseridos, metodo iterativo
    {
        if(ultimo == primeiro)
        {
            System.out.println("Array vazio!");
        }
        for(int i = primeiro; i != ultimo; i = (i + 1) % array.length)
        {
            System.out.println(array[i] + "");
        }
    }
    public boolean pesquisar(int x) // pesquisa se o elemento est´a presente na fila, retornando verdadeiro em caso afirmativo
    {
        boolean achou = false;
        if(ultimo == primeiro)
        {
            System.out.println("Erro, array vazio!");
        }
        for(int i = primeiro; i != ultimo; i = (i+1) % array.length)
        {
            if(array[i] == x)
            {
                achou = true;
                i = ultimo % array.length;
            }
        }
        return achou;
    }
}
