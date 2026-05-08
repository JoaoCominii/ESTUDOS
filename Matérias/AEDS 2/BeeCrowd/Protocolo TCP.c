#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_N 1000
#define MAX_LEN 100

int main()
{
    int N;
    scanf("%d", &N);
    getchar(); // Consumir o '\n' após o número

    char numero[MAX_N][MAX_LEN];
    char linha[MAX_LEN * 2];

    for (int i = 0; i < N; i++) {
        fgets(linha, sizeof(linha), stdin);
        // Remove o '\n' do final, se houver
        linha[strcspn(linha, "\n")] = 0;

        // Divide a linha em partes
        char *token = strtok(linha, " ");
        token = strtok(NULL, " "); // Pega o segundo elemento
        strcpy(numero[i], token);
    }

    // Selection sort
    for (int i = 0; i < N - 1; i++) {
        int menor = i;
        for (int j = i + 1; j < N; j++) {
            if (strcmp(numero[menor], numero[j]) > 0)
                menor = j;
        }
        if (menor != i) {
            char tmp[MAX_LEN];
            strcpy(tmp, numero[menor]);
            strcpy(numero[menor], numero[i]);
            strcpy(numero[i], tmp);
        }
    }

    // Print
    for (int i = 0; i < N; i++) {
        printf("Package %s\n", numero[i]);
    }

    return 0;
}
