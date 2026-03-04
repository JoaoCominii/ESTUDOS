/*
    Faça uma função recursiva que calcula o resto da divisão usando subtrações sucessivas:
    int resto (int numerador, int denominador)
    Faça um programa principal que leia dois números, acione a função e exiba o resultado gerado.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int resto(int numerador, int denominador)
{
    if (numerador < denominador)
        return numerador;
    return resto(numerador - denominador, denominador);
}

int main()
{
    int numerador, denominador, resultado;

    //printf("Digite o numerador e o denominador: ");
    scanf("%i %i", &numerador, &denominador);

    resultado = resto(numerador, denominador);
    printf("%i\n", resultado);

    return 0;
}
