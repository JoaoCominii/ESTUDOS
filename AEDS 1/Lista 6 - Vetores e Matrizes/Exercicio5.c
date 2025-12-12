/*
    Faça um procedimento que preencha uma matriz M 5 x 5. Faça uma função que receba uma
    matriz preenchida, calcule e retorne cada uma das somas a seguir (uma função para cada letra
    abaixo):
    (a) da linha 4 de M
    (b) da coluna 2 de M
    (c) da diagonal principal
    (d) da diagonal secundária
    (e) de todos os elementos da matriz.
    Faça um programa que faça as devidas declarações e acione os módulos para exemplificar o seu
    uso.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>
#define ln 5
#define col 5


void preencheMatriz(int M[ln][col])
{
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("Valor: \n");
            scanf("%i", &M[i][j]);
        }
    }
}

int somaLinha4(int M[ln][col]) // a
{
    int soma = 0;
    for(int j = 0; j < col; j++)
    {
        soma += M[4][j];
    }
    return soma;
}
int somaColuna2(int M[ln][col]) // b
{
    int soma = 0;
    for(int i = 0; i < ln; i++)
    {
        soma += M[i][2];
    }

    return soma;
}

int somaDiagonalPrincipal(int M[ln][col]) // c
{
    int soma = 0;
    for(int i = 0; i < ln; i++)
    {
        soma += M[i][i];
    }

    return soma;
}

int somaDiagonalSecundaria(int M[ln][col]) // d
{
    int soma = 0;
    int j = col -1;
    for(int i = 0; i < ln; i++)
    {
        soma += M[i][j];
        j--;
    }


    return soma;
}

int somaMatriz(int M[ln][col]) // e
{
    int soma = 0;
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            soma += M[i][j];
        }
    }


    return soma;
}

int main()
{
    int matriz[ln][col];
    preencheMatriz(matriz);

    printf("Soma linha 4: %i\n", somaLinha4(matriz));
    printf("Soma coluna 2: %i\n", somaColuna2(matriz));
    printf("Soma diagonal principal: %i\n", somaDiagonalPrincipal(matriz));
    printf("Soma diagonal secundaria: %i\n", somaDiagonalSecundaria(matriz));
    printf("Soma de todos os elementos da matriz: %i\n", somaMatriz(matriz));
    return 0;
}
