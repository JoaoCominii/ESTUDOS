/*
    Faça uma função recursiva que calcula a divisão usando subtrações sucessivas:
    int divisao (int numerador, int denominador)
    Faça um programa principal que leia dois números, acione a função e exiba o resultado gerado.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int divisao(int numerador, int denominador)
{
    if (numerador < denominador)
        return 0;
    return 1 + divisao(numerador - denominador, denominador);
}

int main()
{
    int numerador, denominador, resultado;

    //printf("Digite o numerador e o denominador: ");
    scanf("%i %i", &numerador, &denominador);

    resultado = divisao(numerador, denominador);
    printf("%i\n", resultado);

    return 0;
}
