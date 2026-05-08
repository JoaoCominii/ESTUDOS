#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define bool short
#define true 1
#define false 0
#define equals(a, b) (((strcmp(a, b) == 0) ? true : false))
#define NUMENTRADA 1000
#define TAMLINHA 1000

/**
 * Arquivo le um inteiro n
 * Salva n valores sequencialmente em um arquivo texto
 * Reabre esse arquivo e faz a leitura de tras pra frente
 */


int main()
{
    FILE* arq = fopen("arquivo.txt", "w");
    int n;
    float valor;
    scanf("%i", &n);
    for(int i = 0; i < n; i++)
    {
        scanf("%f", &valor);
        fprintf(arq, "%f\n", valor);
    }
    fclose(arq);
    arq = fopen("arquivo.txt", "r");
    fseek(arq,0,SEEK_END);
    int posicao = ftell(arq) - 2;
    while(posicao != 0)
    {
        fseek(arq, --posicao, SEEK_SET);
        if(fgetc(arq) == '\n')
        {
            fscanf(arq, "%f", &valor);
            printf("%g\n", valor);
            posicao--;
        }
    }
    fseek(arq,0,SEEK_SET);
    fscanf(arq, "%f", &valor);
    printf("%g\n", valor);

    return 0;
}
