
import java.util.Scanner;

public class OrdenarStringsPorTamanho {
    public static void main(String[] args) {
        int N;
        String linha;
        Scanner scanner = new Scanner(System.in);
            N = scanner.nextInt();
            scanner.nextLine();
            for(int i = 0; i < N; i++)
            {
                linha = scanner.nextLine();
                String[] strings = linha.split(" ");
                for(int j = 0; j < strings.length - 1; j++)
                {
                    for(int k = 0; k < strings.length - j - 1; k++)
                    {
                        if(strings[k].length() < strings[k + 1].length())
                        {
                            String temp = strings[k];
                            strings[k] = strings[k + 1];
                            strings[k + 1] = temp;
                        }
                    }
                }
                System.out.println(String.join(" ", strings));
            }
        }
    }
