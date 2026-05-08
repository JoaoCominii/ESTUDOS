
import java.util.Scanner;

class Atleta
{
    String nome;
    int pesoMaximo;

    public Atleta(String nome, int pesoMaximo)
    {
        this.nome = nome;
        this.pesoMaximo = pesoMaximo;
    }
}

public class LevantamentoDePeso
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        String nome;
        int pesoMaximo;
        Atleta[] atletas = new Atleta[N];
        // Ler todos N atletas
        for(int i = 0; i < N; i++)
        {
            nome = scanner.next();
            pesoMaximo = scanner.nextInt();
            atletas[i] = new Atleta(nome, pesoMaximo);
        }

        // Ordenar por peso e desempate nome com insercao
        for(int i = 1; i < N; i++)
        {
            Atleta chave = atletas[i];
            int j = i - 1; 
            while(j >= 0 && (atletas[j].pesoMaximo < chave.pesoMaximo || atletas[j].pesoMaximo == chave.pesoMaximo && atletas[j].nome.compareTo(chave.nome) > 0))
            {
                atletas[j + 1] = atletas[j];
                j--;
            }
            atletas[j + 1] = chave;
        }

        // Imprimir atletas
        for(int i = 0; i < N; i++)
        {
            System.out.println(atletas[i].nome + " " + atletas[i].pesoMaximo);
        }
    }
}