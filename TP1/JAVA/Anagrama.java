import java.nio.charset.*;
import java.util.Scanner;

class Anagrama {
   public static boolean ehAnagrama(String palavra1, String palavra2) {
      palavra1 = palavra1.toUpperCase();
      palavra2 = palavra2.toUpperCase();
      
      if (palavra1.length() != palavra2.length())
         return false;

      int[] contador = new int[256]; // Array para contar a frequência dos caracteres
      for (int i = 0; i < palavra1.length(); i++) {
         contador[palavra1.charAt(i)]++;
         contador[palavra2.charAt(i)]--;
      }
      for (int count : contador) {
         if (count != 0)
            return false;
      }
      return true;
   }

   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
      String linha;

      while (scanner.hasNextLine()) { 
         linha = scanner.nextLine().trim();
         if (linha.equals("FIM")) break;

         String[] palavras = linha.split("-");
         if (palavras.length != 2) continue; // Garante que há exatamente duas palavras

         MyIO.println(ehAnagrama(palavras[0].trim(), palavras[1].trim()) ? "SIM" : "NÃO");
      }
      scanner.close();
   }
}
