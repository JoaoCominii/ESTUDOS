/*
    Escreva um algoritmo que lê um valor n inteiro e positivo e que calcula a seguinte soma:
    S = 1 + 1/2 + 1/3 + 1/4 + ... + 1/n
    O algoritmo deve escrever cada termo gerado e o valor final de S.
    Autor - João Comini
    Data - 06/09/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variaveis
    int N;
    float S = 0;

    // Leitura dados
    printf("N:");
    scanf("%i", &N);

    // Calcular ( S = 1 + 1/2 + 1/3 + 1/4 + ... + 1/n )
    for(int i = N; i > 0; i--)
    {
        S += 1.0/i;
    }

    // Escrever resultados na tela
    printf("%.2f", S);


    return 0;
}

