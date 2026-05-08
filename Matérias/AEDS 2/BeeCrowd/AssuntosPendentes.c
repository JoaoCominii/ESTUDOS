#include <stdio.h>
#include <string.h>

int main() {
    char S[100005];
    int aberto = 0;

    fgets(S, sizeof(S), stdin);
    // Remove o '\n' do final, se houver
    S[strcspn(S, "\n")] = 0;

    for (int i = 0; S[i] != '\0'; i++) {
        if (S[i] == '(') {
            aberto++;
        } else if (S[i] == ')') {
            if (aberto > 0)
                aberto--;
        }
    }

    if (aberto > 0)
        printf("Ainda temos %d assunto(s) pendente(s)!\n", aberto);
    else
        printf("Partiu RU!\n");

    return 0;
}