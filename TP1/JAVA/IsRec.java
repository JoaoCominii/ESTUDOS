import java.util.Scanner;

/**
 * A classe Is verifica se a palavra tem alguma das caracteristicas abaixo:
 * 1. Se a palavra é composta apenas por vogais.
 * 2. Se a palavra é composta apenas por consoantes.
 * 3. Se a palavra é um número inteiro.
 * 4. Se a palavra é um número real.
 * Para cada palavra, o programa verifica as condições acima e imprime "SIM" ou "NAO" para cada uma.
 * O programa finaliza quando é digitado "FIM".
 */
public class IsRec {
    public static void main(String[] args) {
        String palavra;
        Scanner scanner = new Scanner(System.in, "UTF-8");
        do {
        palavra = scanner.nextLine();
        if (palavra.equals("FIM")) break;
        palavra = palavra.toLowerCase();
        boolean X1 = verificarVogaisRec(palavra);
        boolean X2 = verificarConsoantesRec(palavra);
        boolean X3 = verificarInteiroRec(palavra);
        boolean X4 = verificarRealRec(palavra);
        MyIO.println((X1 ? "SIM" : "NAO") + " " + (X2 ? "SIM" : "NAO") + " " + (X3 ? "SIM" : "NAO") + " " + (X4 ? "SIM" : "NAO"));
        } while (!palavra.equals("FIM"));
        scanner.close();
    }

    // Verificar se a palavra é composta apenas por vogais
    public static boolean verificarVogaisRec(String palavra)
    {
        return verificarVogaisRec(palavra, 0);
    }
    public static boolean verificarVogaisRec(String palavra, int i)
    {
        if(i == palavra.length())
            return true;
        if (palavra.charAt(i) != 'a' && palavra.charAt(i) != 'e' && palavra.charAt(i) != 'i' && palavra.charAt(i) != 'o' && palavra.charAt(i) != 'u')
            return false;
        else return verificarVogaisRec(palavra, i+1);
    }

    // Verificar se a palavra é composta apenas por consoantes
    public static boolean verificarConsoantesRec(String palavra)
    {
        return verificarConsoantesRec(palavra, 0);
    }
    public static boolean verificarConsoantesRec(String palavra, int i)
    {
        if(i == palavra.length())
            return true;
        char c = palavra.charAt(i);
        if(!(c >= 'a' && c <= 'z' && c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u'))
        return false;
        else return verificarConsoantesRec(palavra, i + 1);
    }

    // Verificar se a palavra corresponde a um número inteiro
    public static boolean verificarInteiroRec(String palavra)
    {
        return verificarInteiroRec(palavra, 0);
    }
        public static boolean verificarInteiroRec(String palavra, int i)
        {
        if(i == palavra.length())
            return true;
        char c = palavra.charAt(i);
        if(!Character.isDigit(c))
            return false;
        return verificarInteiroRec(palavra, i + 1);
        }

    // Verificar se a palavra corresponde a um número real
    public static boolean verificarRealRec(String palavra)
    {
        return verificarRealRec(palavra, 0, 0);
    }
    public static boolean verificarRealRec(String palavra, int i, int pontos)
    {
        if(i == palavra.length())
            return pontos == 1;
        char c = palavra.charAt(i);
        if(!Character.isDigit(c))
        if (c == '.' || c == ',') {
            return verificarRealRec(palavra, i + 1, pontos + 1);
        } else {
            return false;
        } else {
            return verificarRealRec(palavra, i + 1, pontos);
        }
    }
}
