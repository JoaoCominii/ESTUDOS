import java.util.Scanner;

/**
 * A classe SomaDigitos contém um método recursivo para calcular a soma dos digitos de um número.
 * O caso base, ou seja, a condição de parada é quando o número é igual a 0.
 * O método principal inicializa o contador e chama o método recursivo.
 */
class SomaDigitos 
{
   public static int somaDigitos(int num)
   {
      return somaDigitos(num, 0);
   }

   private static int somaDigitos(int num, int soma)
   {
      if(num == 0) // caso base, quando n for 0
      {
         return soma;
      }
      return somaDigitos(num / 10, soma + (num % 10));
   }

   public static void main (String[] args){
      Scanner scanner = new Scanner(System.in);
      String entrada;
      int numero;
      do {
         entrada = scanner.nextLine();
         if (entrada.equals("FIM"))
            break;

         numero = Integer.parseInt(entrada);
         System.out.println(somaDigitos(numero));
      } while (true);
      scanner.close();
   }
}
