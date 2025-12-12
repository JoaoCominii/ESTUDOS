/*
    Faça um programa que imprima os L primeiros elementos da série de Fibonacci. Por
    exemplo, se o usuário digitou o número 40, deverão ser apresentados os 40 números da
    sequência na tela.
    Autor - João Comini
    Data - 06/09/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variaveis
    int L, primeiro = 0, segundo = 1, proximo;

    // Leitura dados
    printf("Digite a quantidade de elementos da série de Fibonacci que deseja ver: ");
    scanf("%i", &L);

    // Calcular L elementos da série de Fibonacci
    for (int i = 1; i <= L; i++)
    {
        if (i == 1)
            printf("%i ", primeiro);
        else if (i == 2)
            printf("%i ", segundo);
        else
        {
            proximo = primeiro + segundo;
            printf("%i ", proximo);
            primeiro = segundo;
            segundo = proximo;
        }
    }

    return 0;
}

