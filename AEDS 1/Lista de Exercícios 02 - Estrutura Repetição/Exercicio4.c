/*
    Escrever um algoritmo que lê um valor N inteiro e positivo e que calcula e escreve o valor
    de E:
    E = 1 + 1 / 1! + 1 / 2! + 1 / 3! + .... + 1 / N!
    Autor - João Comini
    Data - 28/08/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declarar variaveis
    int N;
    float fatorial, E = 0;

    // Leitura de dados
    printf("Quantos numeros?");
    scanf("%i", &N);

    // Calculo fatorial
    for(int den = 0; den <= N; den++)
    {
        fatorial = 1;
        for(int fator = den; fator > 1; fator--)
        {
            fatorial = fatorial * fator;
        }
        E += 1/fatorial;
    }

    printf("%.2f", E);

    return 0;
}

