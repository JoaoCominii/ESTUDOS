/*
    Escreva um programa em C para imprimir todas as letras do alfabeto maiúsculo usando um
    ponteiro.
    Autor - João Comini
    Data - 12/11/24
*/

#include <stdio.h>
#include <stdlib.h>


int main()
{
    char* letra = (char*)malloc(26*sizeof(char));
    for(int i = 0; i < 26; i++)
    {
        *(letra + i) = 'A' + i;
    }
    for(int i = 0; i < 26; i++)
    {
        printf("%c ",*(letra + i));
    }



    return 0;
}
