/*
    Escrever um procedimento que preenche uma matriz M(10,10) e a escreve. Faça outros procedimentos que recebam uma matriz preenchida, realize as trocas indicadas a seguir (um procedimento para cada uma delas) e exiba a matriz resultante da troca:
    (a) a linha 2 com a linha 8
    (b) a coluna 4 com a coluna 10
    (c) a diagonal principal com a diagonal secundária
    (d) a linha 5 com a coluna 10.
    Faça um programa que faça as devidas declarações e acione os módulos para exemplificar o seu
    uso.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define ln 10
#define col 10

void preencheMatriz(int M[ln][col])
{
    srand((unsigned)time(NULL));
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            M[i][j] = rand() % 200; // 0 a 199
        }
    }
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i][j]);
        }
    }

}
void trocaLn2Ln8(int M[ln][col])
{
    int aux[ln][col];
    for(int j= 0; j < col; j++)
    {
        aux[1][j] = M[1][j];
        M[1][j] = M[7][j];
        M[7][j] = aux[1][j];
    }
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i][j]);
        }
    }
}

void trocaCol4Col10(int M[ln][col])
{
    int aux[ln][col];
    for(int i = 0; i < ln; i++)
    {
        aux[i][3] = M[i][3];
        M[i][3] = M[i][9];
        M[i][9] = aux[i][3];
    }
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i][j]);
        }
    }
}

void trocaDiagonalPrincipalSecundaria(int M[ln][col])
{
    int aux[ln][col];
    int j = col - 1;
    for(int i = 0; i < ln; i++)
    {
        aux[i][i] = M[i][i];
        M[i][i] = M[i][j];
        M[i][j] = aux[i][i];
        j--;
    }
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i][j]);
        }
    }
}

void trocaLn5Col10(int M[ln][col])
{
    int aux[ln][col];
    for(int j = 0; j < ln; j++)
    {
        aux[4][j] = M[4][j];
        M[4][j] = M[j][9];
        M[j][9] = aux[4][j];
    }
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i][j]);
        }
    }
}

int main()
{
    int M[ln][col];

    preencheMatriz(M);
    trocaLn2Ln8(M);
    trocaCol4Col10(M);
    trocaDiagonalPrincipalSecundaria(M);
    trocaLn5Col10(M);


    return 0;
}
