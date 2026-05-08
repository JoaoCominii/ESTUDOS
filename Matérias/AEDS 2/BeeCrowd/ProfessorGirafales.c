#include <stdio.h>
#include <string.h>
#include <ctype.h>

int diferencas(char* original, char* assinatura) {
    int c = 0;
    for(int i = 0; original[i] != '\0' && assinatura[i] != '\0'; i++) {
        if(tolower(original[i]) != tolower(assinatura[i])) {
            c++;
        }
        if(c > 1) return 1;
    }
    return 0;
}

int main() {
    int N, M, falsas;
    char nomes[50][21], originais[50][21];

    while (scanf("%d", &N) && N != 0) { // Corrigido para evitar leitura infinita
        for(int i = 0; i < N; i++) {
            scanf("%s %s", nomes[i], originais[i]);
        }

        scanf("%d", &M);
        falsas = 0;

        for(int i = 0; i < M; i++) {
            char nome[21], assinatura[21];
            scanf("%s %s", nome, assinatura);

            for(int j = 0; j < N; j++) {
                if(strcmp(nome, nomes[j]) == 0) {
                    if(diferencas(originais[j], assinatura)) {
                        falsas++;
                    }
                    break;
                }
            }
        }

        printf("%d\n", falsas); // Agora imprime apenas uma vez ao final do loop
    }

    return 0;
}
