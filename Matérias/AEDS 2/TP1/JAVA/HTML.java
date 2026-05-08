
import java.io.*;
import java.net.*;
import java.nio.charset.*;

class MyIO {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));
    private static String charset = "ISO-8859-1";

    public static void setCharset(String charset_) {
        charset = charset_;
        in = new BufferedReader(new InputStreamReader(System.in, Charset.forName(charset)));
    }

    public static void print() {
    }

    public static void print(int x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void print(float x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void print(double x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void print(String x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void print(boolean x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void print(char x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println() {
    }

    public static void println(int x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println(float x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println(double x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println(String x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println(boolean x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void println(char x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.println(x);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static void printf(String formato, double x) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.printf(formato, x);// "%.2f"
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
    }

    public static double readDouble() {
        double d = -1;
        try {
            d = Double.parseDouble(readString().trim().replace(",", "."));
        } catch (Exception e) {
        }
        return d;
    }

    public static double readDouble(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readDouble();
    }

    public static float readFloat() {
        return (float) readDouble();
    }

    public static float readFloat(String str) {
        return (float) readDouble(str);
    }

    public static int readInt() {
        int i = -1;
        try {
            i = Integer.parseInt(readString().trim());
        } catch (Exception e) {
        }
        return i;
    }

    public static int readInt(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readInt();
    }

    public static String readString() {
        String s = "";
        char tmp;
        try {
            do {
                tmp = (char) in.read();
                if (tmp != '\n' && tmp != ' ' && tmp != 13) {
                    s += tmp;
                }
            } while (tmp != '\n' && tmp != ' ');
        } catch (IOException ioe) {
            System.out.println("lerPalavra: " + ioe.getMessage());
        }
        return s;
    }

    public static String readString(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readString();
    }

    public static String readLine() {
        String s = "";
        char tmp;
        try {
            do {
                tmp = (char) in.read();
                if (tmp != '\n' && tmp != 13) {
                    s += tmp;
                }
            } while (tmp != '\n');
        } catch (IOException ioe) {
            System.out.println("lerPalavra: " + ioe.getMessage());
        }
        return s;
    }

    public static String readLine(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readLine();
    }

    public static char readChar() {
        char resp = ' ';
        try {
            resp = (char) in.read();
        } catch (Exception e) {
        }
        return resp;
    }

    public static char readChar(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readChar();
    }

    public static boolean readBoolean() {
        boolean resp = false;
        String str = "";

        try {
            str = readString();
        } catch (Exception e) {
        }

        if (str.equals("true") || str.equals("TRUE") || str.equals("t") || str.equals("1")
                || str.equals("verdadeiro") || str.equals("VERDADEIRO") || str.equals("V")) {
            resp = true;
        }

        return resp;
    }

    public static boolean readBoolean(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        return readBoolean();
    }

    public static void pause() {
        try {
            in.read();
        } catch (Exception e) {
        }
    }

    public static void pause(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro: charset invalido");
        }
        pause();
    }
}

/**
 *
 */
class HTML {

    public static String getEndereco(String endereco) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String resp = "", line;
        try {
            url = new URI(endereco).toURL();
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                resp += line + "\n";
            }
        } catch (URISyntaxException e) {
            System.out.println("Erro: URI inválida - " + endereco);
            return "";
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return resp;
    }

    public static int[] contaCaracteres(String endereco) {
        int valores[] = new int[26];

        for (int i = 0; i < endereco.length(); i++) {
            if (endereco.charAt(i) == '\u0061') {
                valores[0] = valores[0] + 1;
            } else if (endereco.charAt(i) == '\u0065') {
                valores[1] = valores[1] + 1;
            } else if (endereco.charAt(i) == '\u0069') {
                valores[2] = valores[2] + 1;
            } else if (endereco.charAt(i) == '\u006F') {
                valores[3] = valores[3] + 1;
            } else if (endereco.charAt(i) == '\u0075') {
                valores[4] = valores[4] + 1;
            } else if (endereco.charAt(i) == '\u00E1') {
                valores[5] = valores[5] + 1;
            } else if (endereco.charAt(i) == '\u00E9') {
                valores[6] = valores[6] + 1;
            } else if (endereco.charAt(i) == '\u00ED') {
                valores[7] = valores[7] + 1;
            } else if (endereco.charAt(i) == '\u00F3') {
                valores[8] = valores[8] + 1;
            } else if (endereco.charAt(i) == '\u00FA') {
                valores[9] = valores[9] + 1;
            } else if (endereco.charAt(i) == '\u00E0') {
                valores[10] = valores[10] + 1;
            } else if (endereco.charAt(i) == '\u00E8') {
                valores[11] = valores[11] + 1;
            } else if (endereco.charAt(i) == '\u00EC') {
                valores[12] = valores[12] + 1;
            } else if (endereco.charAt(i) == '\u00F2') {
                valores[13] = valores[13] + 1;
            } else if (endereco.charAt(i) == '\u00F9') {
                valores[14] = valores[14] + 1;
            } else if (endereco.charAt(i) == '\u00E3') {
                valores[15] = valores[15] + 1;
            } else if (endereco.charAt(i) == '\u00F5') {
                valores[16] = valores[16] + 1;
            } else if (endereco.charAt(i) == '\u00E2') {
                valores[17] = valores[17] + 1;
            } else if (endereco.charAt(i) == '\u00EA') {
                valores[18] = valores[18] + 1;
            } else if (endereco.charAt(i) == '\u00EE') {
                valores[19] = valores[19] + 1;
            } else if (endereco.charAt(i) == '\u00F4') {
                valores[20] = valores[20] + 1;
            } else if (endereco.charAt(i) == '\u00FB') {
                valores[21] = valores[21] + 1;
            } else if (endereco.charAt(i) >= 97 && endereco.charAt(i) <= 122 && endereco.charAt(i) != '\u0061'
                    && endereco.charAt(i) != '\u0065'
                    && endereco.charAt(i) != '\u0069' && endereco.charAt(i) != '\u006F' && endereco.charAt(i) != '\u0075') {
                valores[22] = valores[22] + 1;
            }
        }
        return valores;
    }

    public static void contaTags(String endereco, int vetor[], String nome) {
        for (int i = 0; i < endereco.length(); i++) {
            if (endereco.charAt(i) == '<' && endereco.charAt(i + 1) == 'b' && endereco.charAt(i + 2) == 'r'
                    && endereco.charAt(i + 3) == '>') {
                vetor[23] = vetor[23] + 1;
                vetor[22] = vetor[22] - 2;
            } else if (endereco.charAt(i) == '<' && endereco.charAt(i + 1) == 't' && endereco.charAt(i + 2) == 'a'
                    && endereco.charAt(i + 3) == 'b' && endereco.charAt(i + 4) == 'l' && endereco.charAt(i + 5) == 'e'
                    && endereco.charAt(i + 6) == '>') {
                vetor[24] = vetor[24] + 1;
                vetor[22] = vetor[22] - 3;
                vetor[0] = vetor[0] - 1;
                vetor[1] = vetor[1] - 1;

            }
        }
    }

    public static void main(String[] args) {
        String endereco, site, palavra;
        palavra = MyIO.readLine();
        int vetor[] = new int[26];
        while (!palavra.equals("FIM")) {
            endereco = MyIO.readLine();
            site = getEndereco(endereco);
            vetor = contaCaracteres(site);
            contaTags(site, vetor, palavra);
            MyIO.println("a(" + (vetor[0]) + ") " + "e(" + (vetor[1]) + ") " + "i(" + (vetor[2]) + ") " + "o(" + (vetor[3]) + ") "
                    + "u(" + (vetor[4]) + ") " + "á(" + (vetor[5]) + ") " + "é(" + (vetor[6]) + ") " + "í(" + (vetor[7]) + ") "
                    + "ó(" + (vetor[8]) + ") " + "ú(" + (vetor[9]) + ") " + "à(" + (vetor[10]) + ") " + "è(" + (vetor[11]) + ") "
                    + "ì(" + (vetor[12]) + ") " + "ò(" + (vetor[13]) + ") " + "ù(" + (vetor[14]) + ") " + "ã(" + (vetor[15]) + ") "
                    + "õ(" + (vetor[16]) + ") " + "â(" + (vetor[17]) + ") " + "ê(" + (vetor[18]) + ") " + "î(" + (vetor[19]) + ") "
                    + "ô(" + (vetor[20]) + ") " + "û(" + (vetor[21]) + ") " + "consoante(" + (vetor[22]) + ") " + "<br>(" + (vetor[23]) + ") "
                    + "<table>(" + (vetor[24]) + ") " + palavra);
            palavra = MyIO.readLine();
        }
    }
}
