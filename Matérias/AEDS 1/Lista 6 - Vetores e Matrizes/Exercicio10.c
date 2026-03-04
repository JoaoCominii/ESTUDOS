/*
    Escreva um programa em C para contar o número de vogais e consoantes em uma cadeia de
    caracteres (vetor de char como string) usando um ponteiro.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    char frase[80];
    int tam, vogais = 0, consoantes = 0;
    printf("Digite o texto:");
    scanf("%s", frase);
    tam = strlen(frase);
    for(int i = 0; i < tam; i++)
    {
        if(*(frase + i) == 'A' || *(frase + i) == 'a' || *(frase + i) == 'E' || *(frase + i) == 'e' || *(frase + i) == 'I' || *(frase + i) == 'i' || *(frase + i) == 'O' || *(frase + i) == 'o' || *(frase + i) == 'U' || *(frase + i) == 'u')
            vogais++;
        else consoantes++;
    }
    printf("Vogais = %i\n", vogais);
    printf("Consoantes = %i", consoantes);

    return 0;
}
