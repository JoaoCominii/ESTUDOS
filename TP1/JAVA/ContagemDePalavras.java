import java.util.Scanner;

/**
 * A classe ContagemDePalavras conta o numero de palavras em uma frase.
 * As palavras são separadas por espaços.
 * O programa finaliza quando é digitado "FIM".
 */
class ContagemDePalavras
{
  public static int NumeroDePalavras(String palavra)
  {
    if(palavra.isEmpty()) return 0;
    int contador = 0;
    for (int i = 0; i < palavra.length(); i++) {
      if(palavra.charAt(i) == ' ')
        contador++;   
    }
    return contador + 1;
  }
  public static void main (String[] args){
  String palavra;
  Scanner scanner = new Scanner(System.in, "UTF-8");
  do { 
      palavra = scanner.nextLine();
      if(palavra.equals("FIM")) break;
      MyIO.println(NumeroDePalavras(palavra));
  } while (!palavra.equals("FIM"));
  scanner.close();
}
}

