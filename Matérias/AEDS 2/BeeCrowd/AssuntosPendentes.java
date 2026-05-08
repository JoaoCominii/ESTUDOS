
import java.util.Scanner;

public class AssuntosPendentes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String S = scanner.nextLine();
        int aberto = 0;
        for(int i = 0; i < S.length(); i++)
        {
            char c = S.charAt(i);
            if(c == '(')
                aberto++;
            else{
                if(aberto > 0)
                    aberto--;
            }
        }
        if(aberto > 0)
            System.out.println("Ainda temos " + aberto + " assunto(s) pendente(s)!");
        else 
            System.out.println("Partiu RU!");
    }
}
