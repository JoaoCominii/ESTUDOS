import time
import random
import matplotlib.pyplot as plt

print("Iniciando execução do código...")

# Algoritmos de ordenação
def selection_sort(arr):
    comparacoes, movimentacoes = 0, 0
    n = len(arr)
    for i in range(n):
        if i % 5000 == 0:  # Exibir mensagens para acompanhar a execução
            print(f"Selection Sort está processando... {i}/{n}")
        min_idx = i
        for j in range(i + 1, n):
            comparacoes += 1
            if arr[j] < min_idx:
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]
        movimentacoes += 1
    return comparacoes, movimentacoes

def insertion_sort(arr):
    comparacoes, movimentacoes = 0, 0
    n = len(arr)
    for i in range(1, n):
        key = arr[i]
        j = i - 1
        while j >= 0 and arr[j] > key:
            comparacoes += 1
            arr[j + 1] = arr[j]
            j -= 1
            movimentacoes += 1
        arr[j + 1] = key
        movimentacoes += 1
    return comparacoes, movimentacoes

def bubble_sort(arr):
    comparacoes, movimentacoes = 0, 0
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):
            comparacoes += 1
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                movimentacoes += 1
    return comparacoes, movimentacoes

def quicksort(arr, left=0, right=None, comparacoes_movimentacoes=None):
    if comparacoes_movimentacoes is None:
        comparacoes_movimentacoes = [0, 0]
    if right is None:
        right = len(arr) - 1
    if left < right:
        pivot, i = arr[right], left
        for j in range(left, right):
            comparacoes_movimentacoes[0] += 1
            if arr[j] < pivot:
                arr[i], arr[j] = arr[j], arr[i]
                comparacoes_movimentacoes[1] += 1
                i += 1
        arr[i], arr[right] = arr[right], arr[i]
        comparacoes_movimentacoes[1] += 1
        quicksort(arr, left, i - 1, comparacoes_movimentacoes)
        quicksort(arr, i + 1, right, comparacoes_movimentacoes)
    return comparacoes_movimentacoes

# Ajuste do tamanho dos vetores de entrada
sizes = [100, 1000, 10000, 100000]
algorithms = {
    "Selection Sort": selection_sort,
    "Insertion Sort": insertion_sort,
    "Bubble Sort": bubble_sort,
    "Quicksort": quicksort
}

results = {alg: [] for alg in algorithms}

for size in sizes:
    print(f"\nRodando testes para tamanho: {size}")
    vetor_original = [random.randint(0, 100000) for _ in range(size)]
    for name, algorithm in algorithms.items():
        print(f"Executando {name} para {size} elementos...")
        vetor = vetor_original[:]
        start_time = time.perf_counter()
        comparacoes, movimentacoes = algorithm(vetor) if name != "Quicksort" else quicksort(vetor)
        end_time = time.perf_counter()
        results[name].append((end_time - start_time, comparacoes, movimentacoes))
        print(f"{name} concluído para {size} elementos.")

print("\nFinalizando execução dos algoritmos...")

# Gerando e salvando gráficos
for i, metric in enumerate(["Tempo de execução (s)", "Número de comparações", "Número de movimentações"]):
    plt.figure(figsize=(10, 6))
    for name in algorithms:
        plt.plot(sizes, [results[name][j][i] for j in range(len(sizes))], marker="o", label=name)
    plt.xlabel("Tamanho do vetor")
    plt.ylabel(metric)
    plt.title(f"Comparação dos algoritmos - {metric}")
    plt.legend()
    plt.grid()

    # Salvando o gráfico como imagem
    filename = f"{metric.replace(' ', '_').lower()}.png"
    plt.savefig(filename, dpi=300)
    print(f"Gráfico salvo: {filename}")

print("\nTodos os gráficos foram gerados e salvos!")