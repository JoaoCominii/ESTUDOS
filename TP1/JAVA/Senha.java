import java.util.Scanner;

/** 
 * A classe ValidacaoDeSenha verifica se uma senha é valida.
 * Uma senha é valida quando tem ao menos 8 caracteres, uma letra maiuscula, uma minuscula, um número e um caracter especial.
 * O programa finaliza quando é digitado "FIM".
 */
class Senha {
    public boolean senhaValida(String palavra) {
        if (palavra.length() < 8) return false;
        boolean temMaiuscula = false;
        boolean temMinuscula = false;
        boolean temNumero = false;
        boolean temEspecial = false;
        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            if (Character.isUpperCase(c)) temMaiuscula = true;
            else if (Character.isLowerCase(c)) temMinuscula = true;
            else if (Character.isDigit(c)) temNumero = true;
            else temEspecial = true;
            if (temMaiuscula && temMinuscula && temNumero && temEspecial) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Senha senha = new Senha();
        Scanner scanner = new Scanner(System.in, "UTF-8");
        String palavra;
        do {
            palavra = scanner.nextLine();
            if (palavra.equals("FIM")) break;
            boolean ehValido = senha.senhaValida(palavra);
            MyIO.println(ehValido ? "SIM" : "NAO");
        } while (!palavra.equals("FIM"));
        scanner.close();
    }
}

