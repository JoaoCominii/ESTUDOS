import java.util.Scanner;

public class AlgebraBooleanaRec {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String frase = scanner.nextLine();

        while (frase.charAt(0) != '0') {
            char novaString[] = new char[frase.length()];
            converteParaChar(novaString, frase);
            int quantidadeVariaveis = contaVariaveis(novaString);

            char valoresVariaveis[] = new char[quantidadeVariaveis];
            valoresVariaveis = leVariaveis(valoresVariaveis, novaString);

            substituiValoresNaString(valoresVariaveis, novaString);
            retiraEntradas(novaString, quantidadeVariaveis);

            char resultado = Opera(novaString);
            System.out.println(resultado);

            frase = scanner.nextLine();
        }

        scanner.close();
    }

    public static void converteParaChar(char novaString[], String frase) {
        for (int i = 0; i < novaString.length; i++) {
            novaString[i] = frase.charAt(i);
        }
    }

    public static void retiraEntradas(char string[], int quantidade) {
        for (int i = 0; i < 8; i++) {
            if (string[i] == 'a' || string[i] == 'n' || string[i] == 'o') {
                i = 8;
            } else {
                string[i] = ' ';
            }
        }
    }

    public static Boolean temParenteses(char string[]) {
        for (int i = 0; i < string.length; i++) {
            if (string[i] == '(' || string[i] == ')') {
                return true;
            }
        }
        return false;
    }

    public static int contaVariaveis(char novaString[]) {
        return novaString[0] - '0';
    }

    public static char[] leVariaveis(char valoresVariaveis[], char novaString[]) {
        for (int i = 2, j = 0; i <= valoresVariaveis.length * 2; i += 2, j++) {
            valoresVariaveis[j] = novaString[i];
        }
        return valoresVariaveis;
    }

    public static void substituiValoresNaString(char valoresVariaveis[], char novaString[]) {
        for (int i = 0; i < novaString.length; i++) {
            if (novaString[i] == 'A') {
                novaString[i] = valoresVariaveis[0];
            } else if (novaString[i] == 'B') {
                novaString[i] = valoresVariaveis[1];
            } else if (novaString[i] == 'C') {
                novaString[i] = valoresVariaveis[2];
            }
        }
    }

    public static int fechamentoParenteses(char string[]) {
        for (int i = 0; i < string.length; i++) {
            if (string[i] == ')') {
                return i;
            }
        }
        return -1;
    }

    public static int aberturaParenteses(int posicaoFinal, char string[]) {
        for (int i = posicaoFinal; i >= 0; i--) {
            if (string[i] == '(') {
                return i;
            }
        }
        return -1;
    }

    public static int contaVariaveisParaOperar(char string[], int posicaoInicial, int posicaoFinal) {
        int quantidade = 0;
        for (int i = posicaoInicial; i < posicaoFinal; i++) {
            if (string[i] == '0' || string[i] == '1') {
                quantidade++;
            }
        }
        return quantidade;
    }

    public static void Not(char string[], int posicaoInicial, int posicaoFinal) {
        for (int i = posicaoInicial; i < posicaoFinal; i++) {
            if (string[i] == '0') {
                string[i] = '1';
            } else if (string[i] == '1') {
                string[i] = '0';
            }
        }
        for (int i = posicaoInicial - 3; i <= posicaoFinal; i++) {
            if (string[i] != '1' && string[i] != '0') {
                string[i] = ' ';
            }
        }
    }

    public static void And(char string[], int posicaoInicial, int posicaoFinal, int tamanho) {
        Boolean valoresLogicos[] = new Boolean[tamanho];
        char resultado;

        int j = 0;
        for (int i = posicaoInicial; i < posicaoFinal; i++) {
            if (string[i] == '1') {
                valoresLogicos[j] = true;
                j++;
            } else if (string[i] == '0') {
                valoresLogicos[j] = false;
                j++;
            }
        }

        Boolean resultadoFinal = valoresLogicos[0];
        for (int k = 1; k < valoresLogicos.length; k++) {
            resultadoFinal = resultadoFinal && valoresLogicos[k];
        }

        resultado = resultadoFinal ? '1' : '0';

        int teste = 0;
        for (int p = posicaoInicial - 3; p <= posicaoFinal; p++) {
            if ((string[p] == '0' || string[p] == '1') && teste == 0) {
                string[p] = resultado;
                teste++;
            } else {
                string[p] = ' ';
            }
        }
    }

    public static void Or(char string[], int posicaoInicial, int posicaoFinal, int tamanho) {
        Boolean valoresLogicos[] = new Boolean[tamanho];
        char resultado;

        int j = 0;
        for (int i = posicaoInicial; i < posicaoFinal; i++) {
            if (string[i] == '1') {
                valoresLogicos[j] = true;
                j++;
            } else if (string[i] == '0') {
                valoresLogicos[j] = false;
                j++;
            }
        }

        Boolean resultadoFinal = valoresLogicos[0];
        for (int k = 1; k < valoresLogicos.length; k++) {
            resultadoFinal = resultadoFinal || valoresLogicos[k];
        }

        resultado = resultadoFinal ? '1' : '0';

        int teste = 0;
        for (int p = posicaoInicial - 2; p <= posicaoFinal; p++) {
            if ((string[p] == '0' || string[p] == '1') && teste == 0) {
                string[p] = resultado;
                teste++;
            } else {
                string[p] = ' ';
            }
        }
    }

    public static char verificaResultado(char string[]) {
        for (int i = 0; i < string.length; i++) {
            if (string[i] == '0' || string[i] == '1') {
                return string[i];
            }
        }
        return 'E';
    }

    public static char Opera(char string[]) {
        int posicaoFinal = fechamentoParenteses(string);
        int posicaoInicial = aberturaParenteses(posicaoFinal, string);

        if (posicaoFinal == posicaoInicial) {
            return verificaResultado(string);
        }

        if (string[posicaoInicial - 1] == 't') {
            Not(string, posicaoInicial, posicaoFinal);
        } else if (string[posicaoInicial - 1] == 'd') {
            int cont = contaVariaveisParaOperar(string, posicaoInicial, posicaoFinal);
            And(string, posicaoInicial, posicaoFinal, cont);
        } else if (string[posicaoInicial - 1] == 'r') {
            int cont = contaVariaveisParaOperar(string, posicaoInicial, posicaoFinal);
            Or(string, posicaoInicial, posicaoFinal, cont);
        }

        return Opera(string);
    }
}