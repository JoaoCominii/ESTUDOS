/*
    Um comerciante deseja fazer o levantamento do lucro das mercadorias que ele
    comercializa. Para isto, mandou digitar uma linha para cada mercadoria com o preço de
    compra e de venda de cada uma. A última linha contém preço de compra igual a 0. Escreva
    um programa que:
    a) Determine e escreva quantas mercadorias proporcionaram:
    i) Lucro < 10%
    ii) 10% <= lucro <= 20%
    iii) Lucro > 20%
    b) Determine e escreva o valor total de compra e de venda de todas as mercadorias,
    assim como o lucro total.
    Autor - João Comini
    Data - 06/09/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variaveis
    int N, lucroMenor10 = 0, lucroMenor20 = 0, lucroMaior20 = 0;
    float precoCompra, precoVenda, compraTotal = 0, vendaTotal = 0, lucroTotal = 0;

    // Leitura dados
    printf("N:");
    scanf("%i", &N);
    for(int i = 0; i < N; i++)
    {
        printf("Preco compra:");
        scanf("%f", &precoCompra);
        printf("Preco venda:");
        scanf("%f", &precoVenda);
        compraTotal += precoCompra;
        vendaTotal += precoVenda;
        lucroTotal += precoVenda - precoCompra;
        if(precoVenda - precoCompra < precoCompra * 0.10)
            lucroMenor10++;
        else if(precoVenda - precoCompra >= precoCompra * 0.10 && precoVenda -precoCompra <= precoCompra * 0.20)
            lucroMenor20++;
        else lucroMaior20++;

    }

    printf("%i %i %i\n", lucroMenor10, lucroMenor20, lucroMaior20);
    printf("%.2f %.2f %.2f", compraTotal, vendaTotal, lucroTotal);


    return 0;
}

