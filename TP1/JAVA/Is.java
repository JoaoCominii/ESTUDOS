

/**
 * A classe Is verifica se a palavra tem alguma das caracteristicas abaixo:
 * 1. Se a palavra é composta apenas por vogais.
 * 2. Se a palavra é composta apenas por consoantes.
 * 3. Se a palavra é um número inteiro.
 * 4. Se a palavra é um número real.
 * Para cada palavra, o programa verifica as condições acima e imprime "SIM" ou "NAO" para cada uma.
 * O programa finaliza quando é digitado "FIM".
 */
class Is {
    
    public static void main(String[] args) {
        String palavra;
        do {
            palavra = MyIO.readLine();
            if (palavra.equals("FIM")) break;

            palavra = palavra.toLowerCase();

            boolean X1 = verificarVogais(palavra);
            boolean X2 = verificarConsoantes(palavra);
            boolean X3 = verificarInteiro(palavra);
            boolean X4 = verificarReal(palavra);

            MyIO.println((X1 ? "SIM" : "NAO") + " " + (X2 ? "SIM" : "NAO") + " " + (X3 ? "SIM" : "NAO") + " " + (X4 ? "SIM" : "NAO"));

        } while (!palavra.equals("FIM"));
    }

    // Verificar se a palavra é composta apenas por vogais
    public static boolean verificarVogais(String palavra) {
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) != 'a' && palavra.charAt(i) != 'e' && palavra.charAt(i) != 'i' && palavra.charAt(i) != 'o' && palavra.charAt(i) != 'u') {
                return false;
            }
        }
        return true;
    }

    // Verificar se a palavra é composta apenas por consoantes
    public static boolean verificarConsoantes(String palavra) {
        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            if (!(c >= 'a' && c <= 'z' && c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u')) {
                return false;
            }
        }
        return true;
    }

    // Verificar se a palavra corresponde a um número inteiro
    public static boolean verificarInteiro(String palavra) {
        for (int i = 0; i < palavra.length(); i++) {
            if (!Character.isDigit(palavra.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Verificar se a palavra corresponde a um número real
    public static boolean verificarReal(String palavra) {
        int pontos = 0;
        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            if (c == '.' || c == ',') {
                pontos++;
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return pontos <= 1;
    }
}