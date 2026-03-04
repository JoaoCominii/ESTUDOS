/*
    Um programa que receba dez números e verifique se eles são divisíveis por 3 e 9 (ao
    mesmo tempo), por 2 e por 5. Caso algum número não seja divisível por nenhum desses
    números mostre a mensagem “Número não é divisível pelos valores”. Apresente também
    ao final a quantidade de números divisíveis por 3 e 9, por 2 e por 5.
    Autor - João Comini
    Data - 28/08/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variáveis
    int valor, contaMultiplos3e9 = 0, contaMultiplos2 = 0, contaMultiplos5 = 0, i = 1;

    // Leitura e testes
    while(i <=10)
    {
        printf("Digite o %i.o valor", i);
        scanf("%i", &valor);
        if(valor % 3 == 0 && valor % 9 == 0)
            contaMultiplos3e9++;
        else if(valor % 2 == 0)
            contaMultiplos2++;
        else if(valor % 5 == 0)
            contaMultiplos5++;
        else printf("Número não é divisível pelos valores\n");
        i++;
    }

    // Escrever na tela
    printf("%i %i %i", contaMultiplos3e9, contaMultiplos2, contaMultiplos5);

    return 0;
}

