/*
    Faça uma função recursiva que calcula o valor de S da série a seguir para n > 0:
    S = 1/1! + 1/2! + 1/3! + ... + 1/N !
    double serie (int n)
    Faça um programa principal que leia um número, acione a função e exiba o resultado gerado.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int fatorial(int N)
{
    if (N == 0 || N == 1)
        return 1;
    else
        return N * fatorial(N - 1);
}

float serie(int N)
{
    if (N == 1)
        return 1.0;
    else
        return serie(N - 1) + (1.0 / fatorial(N));
}

int main()
{
    int num;
    // printf("Digite um número inteiro: ");
    scanf("%d", &num);
    printf("%.2f\n", serie(num));
    return 0;
}

