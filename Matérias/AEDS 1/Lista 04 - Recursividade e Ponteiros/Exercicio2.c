/*
    Faça uma função para encontrar a soma dos dígitos de um número usando recursão. Faça um
    programa principal que leia um número, acione a função e exiba o resultado gerado.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int somaDigitos (int numero)
{
    if (numero < 10) return numero;
    else return ((numero%10) + somaDigitos(numero/10));
}

int main()
{
    int N;

    //printf("Digite: ");
    scanf("%d",&N);
    printf("%d\n",somaDigitos(N));
    return 0;
}

