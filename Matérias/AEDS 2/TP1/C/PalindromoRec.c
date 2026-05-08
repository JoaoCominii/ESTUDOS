#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define bool short
#define true 1
#define false 0
#define equals(a, b) (((strcmp(a, b) == 0) ? true : false))
#define NUMENTRADA 1000
#define TAMLINHA 1000

/**
 * A função ehPalindromo verifica se uma palavra é um palíndromo.
 * Verifica de forma recursiva.
 */

    bool ehPalindromoRecursivo(char palavra[], int esquerda, int direita)
  {
    bool resp;
    if (esquerda >= direita) {
      resp = true;
    } else if (palavra[esquerda] != palavra[direita]) {
      resp = false;
    } else {
      resp = ehPalindromoRecursivo(palavra, esquerda + 1, direita - 1);
    }
    return resp;
  }

    bool ehPalindromo(char palavra[]) {
    return ehPalindromoRecursivo(palavra, 0, strlen(palavra) - 1);
  }


int main()
{
    while(1)
    {
    char palavra[TAMLINHA];
    scanf(" %[^\n]", palavra);
    if(strcmp(palavra, "FIM") == 0)
    {
        break;
    }
    if(ehPalindromo(palavra))
    {
        printf("SIM\n");
    }
    else printf("NAO\n");
    }
    return 0;
}
