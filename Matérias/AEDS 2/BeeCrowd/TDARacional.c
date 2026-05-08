#include <stdio.h>
#include <stdlib.h>

// Função para calcular o MDC (Máximo Divisor Comum)
int mdc(int a, int b) {
    if (b == 0)
        return abs(a);
    return mdc(b, a % b);
}

int main() {
    int N;
    scanf("%d", &N);
    for (int i = 0; i < N; i++) {
        int n1, d1, n2, d2;
        char op, barra;
        scanf("%d %c %d %c %d %c %d", &n1, &barra, &d1, &op, &n2, &barra, &d2);

        int num = 0, den = 0;
        if (op == '+') {
            num = n1 * d2 + n2 * d1;
            den = d1 * d2;
        } else if (op == '-') {
            num = n1 * d2 - n2 * d1;
            den = d1 * d2;
        } else if (op == '*') {
            num = n1 * n2;
            den = d1 * d2;
        } else if (op == '/') {
            num = n1 * d2;
            den = n2 * d1;
        }

        int divisor = mdc(num, den);
        int simpNum = num / divisor;
        int simpDen = den / divisor;

        // Ajusta sinais para o denominador ser sempre positivo
        if (simpDen < 0) {
            simpNum *= -1;
            simpDen *= -1;
        }

        printf("%d/%d = %d/%d\n", num, den, simpNum, simpDen);
    }
}
