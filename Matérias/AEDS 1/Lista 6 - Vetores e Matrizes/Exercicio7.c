/*
    Faça um procedimento que preencha 2 matrizes, A 4 x 6 e B 4 x 6. Faça uma função para cada
    uma das situações a seguir, que recebe duas matrizes preenchidas, calcula e retorna as matrizes
    indicadas :
    (a) uma matriz S que seja a soma de A e B.
    (b) uma matriz D que seja a diferença de A e B. (A - B).
    Faça um programa que faça as devidas declarações e acione os módulos para exemplificar o seu
    uso. Escreva as matrizes resultantes do acionamento de cada uma das funções.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>
#define ln 4
#define col 6

void preencheMatriz(int A[ln][col], int B[ln][col])
{
    for(int i = 0; i < ln; i++) {
        for(int j = 0; j < col; j++) {
            printf("Valor para A[%d][%d]: \n", i, j);
            scanf("%i", &A[i][j]);
        }
    }
    for(int i = 0; i < ln; i++) {
        for(int j = 0; j < col; j++) {
            printf("Valor para B[%d][%d]: \n", i, j);
            scanf("%i", &B[i][j]);
        }
    }
}
int* SomaAB(int A[ln][col], int B[ln][col])
{
    int* S = (int*)malloc(ln * col * sizeof(int));
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            S[i * col + j] = A[i][j] + B[i][j];
        }
    }
    return S;
}

int* diferencaAB(int A[ln][col], int B[ln][col])
{
    int * D = (int*) malloc(ln * col * sizeof(int));
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            D[i * col + j] = A[i][j] - B[i][j];
        }
    }
    return D;
}

void imprimeMatriz(int *M)
{
    for(int i = 0; i < ln; i++)
    {
        for(int j = 0; j < col; j++)
        {
            printf("%i ", M[i * col + j]);
        }
    }
}

int main()
{
    int A[ln][col], B[ln][col];
    int *S = SomaAB(A, B);
    int *D = diferencaAB(A, B);

    preencheMatriz(A, B);

    printf("Soma matriz A e B: \n", *S);
    printf("Diferenca matriz A e B: \n", *D);
    printf("Soma das matrizes A e B:\n");
    imprimeMatriz(S);
    printf("Diferenca das matrizes A e B:\n");
    imprimeMatriz(D);


    return 0;
}
