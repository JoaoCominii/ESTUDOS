/*
    Faça uma função em C para contar os dígitos de um determinado número usando recursão. Faça
    um programa principal que leia um número, acione a função e exiba o resultado gerado
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

int contaDigitos (int numero)
{
    if (numero < 10) return 1;
    else return (1 + contaDigitos(numero/10));
}

int main()
{
    int N;

    //printf("Digite: ");
    scanf("%d",&N);
    printf("%i \n",contaDigitos(N));
    return 0;
}
