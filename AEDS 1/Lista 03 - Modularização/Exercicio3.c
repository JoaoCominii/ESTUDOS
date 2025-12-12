/*
    Faça um procedimento que recebe 3 valores inteiros por parâmetro e os exiba em ordem crescente.
    Faça um programa que leia N conjuntos de 3 valores e acione o procedimento para cada conjunto.
    (N deve ser lido do teclado)
    Autor - João Comini
    Data - 09/09/24
*/
#include <stdio.h>
#include <stdlib.h>
void ordemCrescente(int, int, int);

int main()
{
    int N, val1, val2, val3;
    printf("Valor de N:\n");
    scanf("%i", &N);
    for(int i = 0; i < N; i++)
    {
        printf("valor 1, valor 2, valor 3:\n");
        scanf("%i %i %i", &val1, &val2, &val3);
        ordemCrescente(val1, val2, val3);
    }

    return 0;
}

void ordemCrescente(int val1, int val2, int val3)
{
    int aux;
    if(val1 > val2)
    {
        aux = val1;
        val1 = val2;
        val2 = aux;
    }
    if(val1 > val3)
    {
        aux = val1;
        val1 = val3;
        val3 = aux;
    }
    if(val2 > val3)
    {
        aux = val2;
        val2 = val3;
        val3 = aux;
    }

    printf("%i %i %i\n", val1, val2, val3);
}
