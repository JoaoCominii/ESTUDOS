/*
    Crie um programa que escreva de 1 até 10 em um arquivo, armazenando um número por linha.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    FILE* arq = fopen("teste.txt", "w");
    for(int i = 1; i <= 10; i++)
    {
        fprintf(arq, "%i\n", i);
    }
    fclose(arq);


    return 0;
}
