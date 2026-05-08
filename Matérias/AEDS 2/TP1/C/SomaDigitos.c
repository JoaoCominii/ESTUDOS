#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define bool short
#define true 1
#define false 0
#define equals(a, b) (((strcmp(a, b) == 0) ? true : false))
#define NUMENTRADA 1000
#define TAMLINHA 1000

/**
 * A função SomaDigitos contém um método recursivo para calcular a soma dos digitos de um número.
 * O caso base, ou seja, a condição de parada é quando o número é igual a 0.
 * O método principal inicializa o contador e chama o método recursivo.
 */

  int somaDigitosRec(int num)
  {
      return somaDigitos(num, 0);
  }

  int somaDigitos(int num, int soma)
  {
    if(num == 0)
    {
        return soma;
    }
    else return somaDigitos(num / 10, soma + (num % 10));
  }

int main()
{
    char entrada[TAMLINHA];
    int numero;
    do{
        scanf("%s", entrada);
        if(equals(entrada, "FIM"))
            break;
        numero = atoi(entrada); // Conversor de string para int
        printf("%i\n", somaDigitosRec(numero));
    }while(1);

    return 0;
}
