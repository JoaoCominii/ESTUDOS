/*
    Escreva um procedimento que recebe 3 valores reais X, Y e Z e que verifique se esses valores podem
    ser os comprimentos dos lados de um triângulo e, neste caso, exibe qual é o tipo de triângulo
    formado. Para que X, Y e Z formem um triângulo é necessário que a seguinte propriedade seja
    satisfeita: o comprimento de cada lado de um triângulo é menor do que a soma do comprimento
    dos outros dois lados. O procedimento deve identificar o tipo de triângulo formado observando
    as seguintes definições:
    • Triângulo Equilátero: os comprimentos dos 3 lados são iguais;
    • Triângulo Isósceles: os comprimentos de pelo menos 2 lados são iguais.
    • Triângulo Escaleno: os comprimentos dos 3 lados são diferentes.
    Faça um programa que leia um número indeterminado (até lado negativo) de triângulos (valores
    dos 3 lados) e para cada triângulo, acione o procedimento.
    Autor - João Comini
    Data - 10/09/24
*/
#include <stdio.h>
#include <stdlib.h>
void ehTriangulo(float, float, float);

int main()
{
    float X, Y, Z;
    printf("LADO1, LADO2, LADO3\n");
    scanf("%f %f %f", &X, &Y, &Z);
    while(X >= 0 && Y >= 0 && Z>= 0)
    {
        ehTriangulo(X, Y, Z);
        printf("LADO1, LADO2, LADO3\n");
        scanf("%f %f %f", &X, &Y, &Z);
    }

    return 0;
}

void ehTriangulo(float X, float Y, float Z)
{
    if(X < (Y + Z) && Y < (X + Z) && Z < (X + Y)) // Condicao para saber se é triangulo
    {
        if(X == Y && Y == Z) // Triângulo Equilátero: os comprimentos dos 3 lados são iguais;
            printf("TRIANGULO EQUILATERO\n");
        else if(X == Y || Y == Z || X == Z) // Triângulo Isósceles: os comprimentos de pelo menos 2 lados são iguais.
            printf("TRIANGULO ISOSCELES\n");
        else if(X != Y && Y != Z && X != Z) // Triângulo Escaleno: os comprimentos dos 3 lados são diferentes.
            printf("TRIANGULO ESCALENO\n");
    }
    else printf("NÃO TRIANGULO\n");
}
