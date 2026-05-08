public class Teste {
    public static void main(String[] args) {
        ListaLinear lista = new ListaLinear(10);
        lista.inserirInicio(21);
        lista.inserirInicio(43);
        lista.inserir(13, 1);
        lista.inserirFim(54);
        lista.inserirFim(1);
        System.out.println(lista.pesquisar(43));


        lista.mostrar();
    }
}
