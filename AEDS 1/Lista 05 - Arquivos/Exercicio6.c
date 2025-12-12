/*
    Faça um programa que solicite ao usuário um número, em seguida, imprima na tela todos os seus
    divisores. Salve em um arquivo texto a soma total desses divisores.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>
void escreveArquivo(int soma)
{
    FILE* arq = fopen ("arquivo.txt", "w");
    fprintf(arq, "%d", soma);
}

int main()
{
    int n, soma = 0;
    printf("num:", n);
    scanf("%d", &n);
    for(int i = 1; i <= n; i++)
    {
        if(n % i == 0)
        {
            soma += i;
            printf("%d\n", i);
        }
    }
    printf("%d", soma);
    escreveArquivo(soma);



    return 0;
}
