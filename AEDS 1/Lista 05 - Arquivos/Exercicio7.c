/*
    Faça um programa para inserir N letras informadas pelo usuário em um arquivo texto. Onde N
    é uma quantidade de letras definida pelo usuário. Depois de inseridas as N letras, o programa
    deverá ler todas as N letras do arquivo, calcular e mostrar a quantidade de vogais, ou seja,
    quantas letras a, e, i, o, u.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>
void quantidadeVogais(FILE* arq)
{
    char letra;
    int vogais = 0;
    arq = fopen("arquivo.txt", "r");
    while(fscanf(arq, " %c", &letra) != EOF)
    {
        if(letra == 'a' || letra == 'e' || letra == 'i' || letra == 'o' || letra == 'u' || letra == 'A' || letra == 'E' || letra == 'I' || letra == 'O' || letra == 'U')
            vogais++;
    }
    fclose(arq);
    printf("%i", vogais);
}

int main()
{
    int n;
    char letra;
    printf("quantidade de letras:");
    scanf("%d", &n);
    FILE* arq = fopen("arquivo.txt", "w");
        printf("digite a %i.a letra: ", 1);
        scanf("%c\n", &letra);
        fprintf(arq, "%c", letra);
    for(int i = 2; i <= n; i++)
    {
        printf("digite a %i.a letra: ", i);
        scanf(" %c\n", &letra);
        fprintf(arq, "%c", letra);
    }
    fclose(arq);
    quantidadeVogais(arq);
    return 0;
}
