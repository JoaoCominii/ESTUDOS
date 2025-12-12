/*
    Faça um programa para preencher uma matriz 4 x 4, em seguida apresentar na tela a soma
    dos elementos abaixo da diagonal principal. Mostre na tela os elementos da diagonal principal
    também.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>
#define ln 4
#define col 4

void preencheMatriz(int matriz[ln][col])
{
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("Valor:\n");
            scanf("%i", &matriz[i][j]);
        }
    }
}

int somaDiagonalAbaixo(int matriz[ln][col])
{
    int soma = 0;
    for(int i = 1; i < ln; i++)
    {
        for(int j = 0; j < i; j++)
        {
            soma  += matriz[i][j];
        }
    }


    return soma;
}
void elementosDiagonalPrincipal(int matriz[ln][col])
{
    for(int i = 0; i < ln; i++)
    {
        printf("%i ", matriz[i][i]);
    }
}

int main()
{
    int matriz[ln][col];

    preencheMatriz(matriz);
    printf("Soma diagonal abaixo: %i\n", somaDiagonalAbaixo(matriz));
    elementosDiagonalPrincipal(matriz);

    return 0;
}
