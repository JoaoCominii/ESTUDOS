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
 * A função InversaoString escreve uma string de forma inversa.
 * Inversão é feita de maneira recursiva.
 */

void InverteString(char palavra[], int i, int j)
{
    if(i >= j)
        return;
    char aux = palavra[i];
    palavra[i] = palavra[j];
    palavra[j] = aux;

    InverteString(palavra, i + 1, j - 1);
}

void InverteStringRec(char palavra[])
{
    int tamanho = strlen(palavra);
    InverteString(palavra, 0, tamanho - 1);
}

int main()
{
    char palavra[TAMLINHA];

    while(1){
        scanf(" %[^\n]", palavra);
    if(strcmp(palavra, "FIM") == 0) break;

    InverteStringRec(palavra);
    printf("%s\n", palavra);
    }


    return 0;
}
