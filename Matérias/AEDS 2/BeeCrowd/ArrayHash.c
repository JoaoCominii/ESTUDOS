#include <stdio.h>
#include <string.h>


int main() {
    int N;
    scanf("%i", &N);
    int L;
    char palavra[255];
    for(int i = 0; i < N; i++)
    {
        int soma = 0;
        scanf("%i", &L);
        for(int j = 0; j < L; j++)
        {
            scanf(" %[^\n]", palavra);
            for(int k = 0; k < strlen(palavra); k++)
            {
                soma += (palavra[k] - 'A' + j + k);
            }
        }
        printf("%i\n", soma);
    }


    return 0;
}
