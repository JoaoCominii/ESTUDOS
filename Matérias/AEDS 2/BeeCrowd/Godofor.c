#include <stdio.h>
#include <string.h>

int main() {
    int N;
    scanf("%d", &N);

    char vencedor[105] = "";
    int maxPoder = -1, maxMatou = -1, minMorreu = 101;

    for (int i = 0; i < N; i++) {
        char nome[105];
        int poder, matou, morreu;
        scanf("%s %d %d %d", nome, &poder, &matou, &morreu);

        int melhor = 0;
        if (poder > maxPoder) {
            melhor = 1;
        } else if (poder == maxPoder) {
            if (matou > maxMatou) {
                melhor = 1;
            } else if (matou == maxMatou) {
                if (morreu < minMorreu) {
                    melhor = 1;
                } else if (morreu == minMorreu) {
                    if (strcmp(nome, vencedor) < 0) {
                        melhor = 1;
                    }
                }
            }
        }
        if (melhor) {
            strcpy(vencedor, nome);
            maxPoder = poder;
            maxMatou = matou;
            minMorreu = morreu;
        }
    }
    printf("%s\n", vencedor);
    return 0;