/*
    Faça uma função que recebe a idade de um nadador por parâmetro e retorna a categoria desse
    nadador de acordo com a tabela abaixo.
    Autor - João Comini
    Data - 12/09/24
*/
#include <stdio.h>
#include <stdlib.h>
char categoria(int);

int main()
{
   int idade;
   printf("idade:");
   scanf("%i", &idade);
   printf("% c", categoria(idade));

    return 0;
}

char categoria(int idade)
{
    char categoria;
    if(idade >= 5 && idade <= 8)
        categoria = 'F';
    else if(idade >= 8 && idade <= 10)
        categoria = 'E';
    else if(idade >= 11 && idade <= 13)
        categoria = 'D';
    else if(idade >= 14 && idade <= 15)
        categoria = 'C';
    else if(idade >= 16 && idade <= 17)
        categoria = 'B';
    else if(idade >= 18)
        categoria = 'A';

    return categoria;
}
