/*
    Escreva um programa que concatene o conteúdo de dois arquivos. O conteúdo dos dois arquivos
    deverá ser adicionado em um terceiro arquivo.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    int num;
    FILE* arq = fopen("arquivo1.txt", "w");
    for(int i = 1; i <= 10; i++)
    {
        fprintf(arq, "%i\n", i);
    }
    fclose(arq);

    FILE* arq2 = fopen("arquivo2.txt", "w");
    for(int i = 10; i <= 20; i++)
    {
        fprintf(arq2, "%i\n", i);
    }
    fclose(arq2);

    arq = fopen("arquivo1.txt", "r");
    FILE* arq3 = fopen("arquivo3.txt", "w");
    while(fscanf(arq, "%d", &num) != EOF)
    {
        fprintf(arq3, "%d\n", num);
    }
    fclose(arq);
    fclose(arq3);
    arq2 = fopen("arquivo2.txt", "r");
    arq3 = fopen("arquivo3.txt", "a");
    while(fscanf(arq2, "%d", &num) != EOF)
    {
        fprintf(arq3, "%d\n", num);
    }
    fclose(arq2);
    fclose(arq3);


    return 0;
}
