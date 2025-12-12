/*
    Escreva um programa em C para calcular a soma de todos os elementos em um vetor de inteiros
    usando ponteiros. A primeira entrada deve ser o tamanho do vetor a ser inserido.
    Autor - João Comini
    Data - 12/11/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    int tam, soma = 0;
    printf("Tamanho vetor : ");
    scanf("%i", &tam);
    int* vetor = (int*)malloc(tam * sizeof(int));
    for(int i = 0; i < tam; i++)
    {
        printf("Vetor[%i] = \n", i);
        scanf("%i", vetor + i);
    }
    for(int i = 0; i < tam; i++)
    {
        soma += *(vetor + i);
    }

    printf("Soma dos valores do vetor = %i", soma);

    return 0;
}
