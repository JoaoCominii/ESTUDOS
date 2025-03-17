import java.io.*;
import java.nio.charset.*;
import java.util.Scanner;

/** A classe SubstringMaisLonga retorna o comprimento da maior substring que não possui caracteres repetidos.
 * O programa finaliza quando é digitado "FIM".
 */
class SubstringMaisLonga
{
  public static int substringMaior(String palavra)
  {
    int[] ultimo = new int[256]; // Tamanho da tabela ASCII
    for(int i = 0; i < 256; i++)
    {
      ultimo[i] = -1; // Inicializar sem caracter
    }
    int contador = 0;
    int inicio = 0;
    for(int i = 0; i < palavra.length(); i++)
    {
      char atual = palavra.charAt(i);
       
      if(ultimo[atual] >= inicio) // Se o caractere já apareceu e está dentro da substring atual, atualiza o início
        inicio = ultimo[atual] + 1;

      ultimo[atual] = i; // Atualiza a última posição desse caractere

      // Calcula o comprimento da substring atual e atualiza o máximo se necessário
      contador = Math.max(contador, i -inicio + 1);
    }
    return contador;
  }
  public static void main (String[] args){
  String palavra;
  Scanner scanner = new Scanner(System.in, "UTF-8");
  do { 
      palavra = scanner.nextLine();
      if(palavra.equals("FIM")) break;
      MyIO.println(substringMaior(palavra));
  } while (!palavra.equals("FIM"));
  scanner.close();
}
}

