#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>
#define MAX_LISTA 300

void limparEspacos(char** array, int n) {
    for (int i = 0; i < n; i++) {
        char* temp = strdup(array[i]);
        char* ptr = temp;
        // Remove espaços do início
        while (*ptr == ' ') {
            ptr++;
        }
        // Atualiza o valor no array
        array[i] = strdup(ptr);
        free(temp);
    }
}

// Função auxiliar para trocar elementos em um array de strings
void trocar(char** a, char** b) {
    char* temp = *a;
    *a = *b;
    *b = temp;
}

// Função de partição para o Quick Sort
int particao(char** array, int low, int high) {
    char* pivot = array[high]; // Pivô
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (strcmp(array[j], pivot) < 0) { // Compara strings em ordem alfabética
            i++;
            trocar(&array[i], &array[j]);
        }
    }
    trocar(&array[i + 1], &array[high]);
    return i + 1;
}

// Função principal do Quick Sort
void quickSort(char** array, int low, int high) {
    if (low < high) {
        int pi = particao(array, low, high); // Obtém o índice de partição

        quickSort(array, low, pi - 1);  // Ordena os elementos antes da partição
        quickSort(array, pi + 1, high); // Ordena os elementos depois da partição
    }
}

// Função auxiliar para remover aspas do início e do final de uma string
char* removerAspas(const char* src) {
    if (src == NULL || strlen(src) == 0) return strdup("NaN");
    char* result = strdup(src);
    size_t len = strlen(result);

    if (len > 0 && result[0] == '"' && result[len - 1] == '"') {
        result[len - 1] = '\0';
        memmove(result, result + 1, len - 1);
    }
    return result;
}

// Função para dividir uma linha CSV, lidando com aspas e vírgulas manualmente
char** dividirCSV(const char* linha, int* count) {
    if (linha == NULL || strlen(linha) == 0) {
        *count = 0;
        return NULL;
    }

    char** resultado = NULL;
    int tamanho = 0;
    char* copia = strdup(linha);
    char* ptr = copia;
    bool dentroAspas = false;
    char buffer[2048];
    int bufferIndex = 0;

    while (*ptr != '\0') {
        if (*ptr == '"' && (ptr == copia || *(ptr - 1) != '\\')) {
            dentroAspas = !dentroAspas; // Alterna dentro ou fora das aspas
        } else if (*ptr == ',' && !dentroAspas) {
            buffer[bufferIndex] = '\0';
            resultado = realloc(resultado, (tamanho + 1) * sizeof(char*));
            resultado[tamanho++] = removerAspas(buffer);
            bufferIndex = 0;
        } else {
            buffer[bufferIndex++] = *ptr;
        }
        ptr++;
    }

    // Adicionar o último campo ao resultado
    buffer[bufferIndex] = '\0';
    resultado = realloc(resultado, (tamanho + 1) * sizeof(char*));
    resultado[tamanho++] = removerAspas(buffer);

    free(copia);
    *count = tamanho;
    return resultado;
}

// Estrutura para armazenar informações do Show
typedef struct {
    char* Show_ID;
    char* Type;
    char* Title;
    char* Director;
    char** Cast;
    int Cast_Count;
    char* Country;
    char* Date_Added;
    int Release_Year;
    char* Rating;
    char* Duration;
    char** Listed_In;
    int Listed_In_Count;
} Show;

// Função para liberar memória alocada de um Show
void freeShow(Show* show) {
    free(show->Show_ID);
    free(show->Type);
    free(show->Title);
    free(show->Director);

    for (int i = 0; i < show->Cast_Count; i++) {
        free(show->Cast[i]);
    }
    free(show->Cast);

    free(show->Country);
    free(show->Date_Added);
    free(show->Rating);
    free(show->Duration);

    for (int i = 0; i < show->Listed_In_Count; i++) {
        free(show->Listed_In[i]);
    }
    free(show->Listed_In);
}

// Função para imprimir informações do Show
void imprimir(Show* show) {
    printf("=> %s ## %s ## %s ## %s ## [", show->Show_ID, show->Title, show->Type, show->Director);

    for (int i = 0; i < show->Cast_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona vírgula antes dos próximos nomes
        }
        printf("%s", show->Cast[i]);
    }
    if (show->Cast_Count == 0) {
        printf("NaN");
    }

    printf("] ## %s ## %s ## %d ## %s ## %s ## [", show->Country, show->Date_Added, show->Release_Year, show->Rating, show->Duration);

    for (int i = 0; i < show->Listed_In_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona vírgula antes dos próximos itens
        }
        printf("%s", show->Listed_In[i]);
    }
    if (show->Listed_In_Count == 0) {
        printf("NaN");
    }

    printf("] ##\n");
}

// Função para processar e carregar os dados do CSV
Show** carregarCSV(const char* caminhoArquivo, int* tamanho) {
    FILE* file = fopen(caminhoArquivo, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return NULL;
    }

    Show** shows = NULL;
    char linha[2048];
    int contador = 0;

    fgets(linha, sizeof(linha), file); // Ignorar cabeçalho

    while (fgets(linha, sizeof(linha), file)) {
        int numCampos;
        char** valores = dividirCSV(linha, &numCampos);

        // Tratar valores ausentes como "NaN"
        for (int i = 0; i < numCampos; i++) {
            if (valores[i] == NULL || strlen(valores[i]) == 0) {
                valores[i] = strdup("NaN");
            }
        }

        // Criar objeto Show
        Show* show = malloc(sizeof(Show));
        show->Show_ID = valores[0];
        show->Type = valores[1];
        show->Title = valores[2];
        show->Director = valores[3];
        show->Cast = dividirCSV(valores[4], &show->Cast_Count);
        show->Country = valores[5];
        show->Date_Added = valores[6];
        show->Release_Year = atoi(valores[7]);
        show->Rating = valores[8];
        show->Duration = valores[9];
        show->Listed_In = dividirCSV(valores[10], &show->Listed_In_Count);

        // Limpar espaços antes de ordenar
        limparEspacos(show->Cast, show->Cast_Count);
        limparEspacos(show->Listed_In, show->Listed_In_Count);

        // Ordenar `Cast` e `Listed_In` usando Quick Sort
        if (show->Cast_Count > 1) {
            quickSort(show->Cast, 0, show->Cast_Count - 1);
        }
        if (show->Listed_In_Count > 1) {
            quickSort(show->Listed_In, 0, show->Listed_In_Count - 1);
        }

        shows = realloc(shows, (contador + 1) * sizeof(Show*));
        shows[contador++] = show;
    }

    fclose(file);
    *tamanho = contador;
    return shows;
}

// Estrutura do nó AVL
typedef struct NoAVL {
    Show* show;
    struct NoAVL* esq;
    struct NoAVL* dir;
    int altura;
} NoAVL;

// Função para obter a altura de um nó
int altura(NoAVL* no) {
    return (no == NULL) ? 0 : no->altura;
}

// Renomear para evitar conflito com macro do Windows
int maximo(int a, int b) {
    return (a > b) ? a : b;
}

// Rotação simples à direita
NoAVL* rotacaoDireita(NoAVL* y) {
    NoAVL* x = y->esq;
    NoAVL* T2 = x->dir;
    x->dir = y;
    y->esq = T2;
    y->altura = maximo(altura(y->esq), altura(y->dir)) + 1;
    x->altura = maximo(altura(x->esq), altura(x->dir)) + 1;
    return x;
}

// Rotação simples à esquerda
NoAVL* rotacaoEsquerda(NoAVL* x) {
    NoAVL* y = x->dir;
    NoAVL* T2 = y->esq;
    y->esq = x;
    x->dir = T2;
    x->altura = maximo(altura(x->esq), altura(x->dir)) + 1;
    y->altura = maximo(altura(y->esq), altura(y->dir)) + 1;
    return y;
}

// Fator de balanceamento
int fatorBalanceamento(NoAVL* no) {
    return (no == NULL) ? 0 : altura(no->esq) - altura(no->dir);
}

// Inserção AVL
NoAVL* inserirAVL(NoAVL* no, Show* show, int* comparacoes) {
    if (no == NULL) {
        NoAVL* novo = (NoAVL*)malloc(sizeof(NoAVL));
        novo->show = show;
        novo->esq = novo->dir = NULL;
        novo->altura = 1;
        return novo;
    }
    (*comparacoes)++;
    int cmp = strcmp(show->Title, no->show->Title);
    if (cmp < 0) {
        no->esq = inserirAVL(no->esq, show, comparacoes);
    } else if (cmp > 0) {
        no->dir = inserirAVL(no->dir, show, comparacoes);
    } else {
        return no; // já existe
    }
    no->altura = 1 + maximo(altura(no->esq), altura(no->dir));
    int fb = fatorBalanceamento(no);
    // Rotação à esquerda
    if (fb < -1 && strcmp(show->Title, no->dir->show->Title) > 0) {
        return rotacaoEsquerda(no);
    }
    // Rotação à direita
    if (fb > 1 && strcmp(show->Title, no->esq->show->Title) < 0) {
        return rotacaoDireita(no);
    }
    // Rotação dupla à direita
    if (fb > 1 && strcmp(show->Title, no->esq->show->Title) > 0) {
        no->esq = rotacaoEsquerda(no->esq);
        return rotacaoDireita(no);
    }
    // Rotação dupla à esquerda
    if (fb < -1 && strcmp(show->Title, no->dir->show->Title) < 0) {
        no->dir = rotacaoDireita(no->dir);
        return rotacaoEsquerda(no);
    }
    return no;
}

// Função para buscar Show* por ID
Show* buscarShowPorID(Show** shows, int n, const char* id) {
    for (int i = 0; i < n; i++) {
        if (strcmp(shows[i]->Show_ID, id) == 0) {
            return shows[i];
        }
    }
    return NULL;
}

// Busca AVL com caminho
bool buscarAVL(NoAVL* no, const char* title, char* caminho, int* comparacoes) {
    if (no == NULL) {
        return false;
    }
    (*comparacoes)++;
    int cmp = strcmp(title, no->show->Title);
    if (cmp == 0) {
        return true;
    } else if (cmp < 0) {
        strcat(caminho, " esq");
        return buscarAVL(no->esq, title, caminho, comparacoes);
    } else {
        strcat(caminho, " dir");
        return buscarAVL(no->dir, title, caminho, comparacoes);
    }
}

int main() {
    int nShows = 0;
    Show** shows = carregarCSV("/tmp/disneyplus.csv", &nShows);
    if (!shows) return 1;

    NoAVL* raiz = NULL;
    int comparacoes = 0;
    char entrada[128];
    // Inserção por ID
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada, "\r\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        Show* s = buscarShowPorID(shows, nShows, entrada);
        if (s) raiz = inserirAVL(raiz, s, &comparacoes);
    }
    // Leitura dos títulos para pesquisa
    char** pesquisas = NULL;
    int nPesquisas = 0;
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada, "\r\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        pesquisas = realloc(pesquisas, (nPesquisas + 1) * sizeof(char*));
        pesquisas[nPesquisas++] = strdup(entrada);
    }
    clock_t inicio = clock();
    int comparacoesBusca = 0;
    for (int i = 0; i < nPesquisas; i++) {
        char caminho[512] = "raiz";
        bool achou = buscarAVL(raiz, pesquisas[i], caminho, &comparacoesBusca);
        printf("%s %s\n", caminho, achou ? "SIM" : "NAO");
    }
    clock_t fim = clock();
    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;
    FILE* log = fopen("matricula_avl.txt", "w");
    fprintf(log, "846713\t%.6lf\t%d\n", tempo, comparacoes + comparacoesBusca);
    fclose(log);
    // Liberação de memória
    for (int i = 0; i < nPesquisas; i++) free(pesquisas[i]);
    free(pesquisas);
    for (int i = 0; i < nShows; i++) freeShow(shows[i]);
    free(shows);
    return 0;
}
