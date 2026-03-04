/*
    Escreva um programa em C para imprimir um vetor de caracteres ao contrário usando um
    ponteiro. A primeira entrada deve ser o tamanho do vetor a ser inserido.
    Autor - João Comini
    Data - 12/11/24
*/

#include <stdio.h>
#include <stdlib.h>

int main()
{
    int tam;
    printf("Tamanho do vetor:");
    scanf("%i", &tam);
    char* vetor = (char*)malloc(tam * sizeof(char));
    for(int i = 0; i < tam; i++)
    {
        printf("vetor[%i] = \n", i);
        scanf(" %c", vetor + i);
    }
    for(int i = tam; i >= 0; i--)
    {
        printf("%c", *(vetor + i));
    }

    return 0;
}
