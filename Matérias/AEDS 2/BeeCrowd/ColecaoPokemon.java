
import java.util.Scanner;

public class ColecaoPokemon {
    public static boolean jaTem(String[] capturados, String nome, int quantidadePokemons)
    {
        boolean encontrado = false;
        for(int i = 0; i < quantidadePokemons; i++)
        {
            if(capturados[i].equals(nome))
            {
                encontrado = true;
                i = quantidadePokemons;
            }
        }
    
        return encontrado;
    } 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt(); // Quantidade de pokemons ja capturados
        scanner.next();
        String[] capturados = new String[N];
        String nome;
        int quantidadePokemons = 0;
        for(int i = 0; i < N; i++)
        {
            nome = scanner.nextLine();
            if(!(jaTem(capturados, nome, quantidadePokemons)))
            {
                capturados[i] = nome;
                quantidadePokemons++;
            }
        }

        System.out.println("Falta(m) " + (151 -quantidadePokemons) + " pokemon(s).");
        scanner.close();
    }

}
