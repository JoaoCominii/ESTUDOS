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
 * A fun��o ehPalindromo verifica se uma palavra � um pal�ndromo.
 */
 bool fim(char palavra[])
 {
     if(palavra[0] == 'F' && palavra[1] == 'I' && palavra[2] == 'M')
        return false;
     else return true;
 }

bool ehPalindromo(char palavra[])
{
    int tamanho = strlen(palavra);
    for (int i = 0; i < tamanho / 2; i++)
    {
        if (palavra[i] != palavra[tamanho - 1 - i])
        {
            return false;
        }
    }
    return true;
}

int main()
{
    char palavra[TAMLINHA];
    while (fim(palavra))
    {
        scanf(" %[^\n]", palavra);

        if (equals(palavra, "FIM"))
        {
            break;
        }

        if (ehPalindromo(palavra))
        {
            printf("SIM\n");
        }
        else
        {
            printf("NAO\n");
        }
    }

    return 0;
}
