/*
    Faça um programa que imprima todos os elementos da série de Fibonacci menores que L
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
    printf("Digite um valor máximo para a série de Fibonacci: ");
    scanf("%i", &L);

    // Calcular elementos da série de Fibonacci
    while (primeiro < L) {
        printf("%lld ", primeiro);
        proximo = primeiro + segundo;
        primeiro = segundo;
        segundo = proximo;
    }

    return 0;
}

