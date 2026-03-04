/*
    Escreva um programa em C para ordenar um vetor de inteiros usando ponteiro. A primeira
    entrada deve ser o tamanho do vetor a ser inserido.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    int tam, aux;
    printf("Tamanho do vetor:");
    scanf("%i", &tam);
    int* vetor = (int*)malloc(tam * sizeof(int));
    for(int i = 0; i < tam; i++)
    {
        printf("Vetor[%i] = \n", i);
        scanf("%i", vetor + i);
    }

    for(int i = 0; i < tam - 1; i++)
    {
        for(int j = 0; j < tam - 1 -i; j++)
        {
            if(*(vetor + j) > *(vetor + j + 1))
            {
                aux = *(vetor+j);
                *(vetor+j) = *(vetor + j + 1);
                *(vetor + j + 1) = aux;
            }
        }
    }



    for(int i = 0; i < tam; i++)
    {
        printf("%i ", *(vetor + i));
    }


    return 0;
}
