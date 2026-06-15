"""
8-Puzzle Solver - Atividade Prática 2
Disciplina: Inteligência Artificial - PUC Minas Lourdes
Aluno: João Comini Cesar de Andrade

Algoritmos implementados:
  - A* com 3 heurísticas (Manhattan, Peças Fora do Lugar, Linear Conflict)
  - BFS (Busca em Largura)
  - DFS (Busca em Profundidade)
  - UCS (Busca de Custo Uniforme)
  - Greedy (Busca Gulosa)

Estado objetivo:
  [0, 1, 2]
  [3, 4, 5]
  [6, 7, 8]
  (0 representa o espaço vazio)
"""

import heapq
import time
from collections import deque
from copy import deepcopy

# ─────────────────────────────────────────────
# 1. REPRESENTAÇÃO DO ESTADO
# ─────────────────────────────────────────────

GOAL = (0, 1, 2, 3, 4, 5, 6, 7, 8)   # tupla plana; posição = linha*3+coluna
GOAL_POS = {v: (v // 3, v % 3) for v in range(9)}   # posição objetivo de cada peça


def to_tuple(board_2d):
    """Converte lista 2D → tupla plana."""
    return tuple(board_2d[r][c] for r in range(3) for c in range(3))


def to_2d(state):
    """Converte tupla plana → lista 2D."""
    return [[state[r * 3 + c] for c in range(3)] for r in range(3)]


def print_board(state):
    """Imprime o tabuleiro de forma legível."""
    board = to_2d(state)
    for row in board:
        print(" ".join(str(x) if x != 0 else "_" for x in row))
    print()


def is_solvable(state):
    """
    Verifica se o estado é solucionável contando inversões.
    Um estado 8-puzzle é solucionável sse o número de inversões é par.
    """
    tiles = [x for x in state if x != 0]
    inversions = sum(
        1
        for i in range(len(tiles))
        for j in range(i + 1, len(tiles))
        if tiles[i] > tiles[j]
    )
    return inversions % 2 == 0


# ─────────────────────────────────────────────
# 2. GERAÇÃO DE SUCESSORES
# ─────────────────────────────────────────────

MOVES = [(-1, 0, "Cima"), (1, 0, "Baixo"), (0, -1, "Esquerda"), (0, 1, "Direita")]


def get_successors(state):
    """
    Retorna lista de (novo_estado, ação) para todos os movimentos válidos.
    """
    board = to_2d(state)
    # Encontra o espaço vazio
    zero_r = zero_c = 0
    for r in range(3):
        for c in range(3):
            if board[r][c] == 0:
                zero_r, zero_c = r, c

    successors = []
    for dr, dc, action in MOVES:
        nr, nc = zero_r + dr, zero_c + dc
        if 0 <= nr < 3 and 0 <= nc < 3:
            new_board = deepcopy(board)
            new_board[zero_r][zero_c], new_board[nr][nc] = (
                new_board[nr][nc],
                new_board[zero_r][zero_c],
            )
            successors.append((to_tuple(new_board), action))
    return successors


# ─────────────────────────────────────────────
# 3. HEURÍSTICAS PARA A*
# ─────────────────────────────────────────────

def heuristic_misplaced(state):
    """H1 - Peças fora do lugar (admissível)."""
    return sum(1 for i in range(9) if state[i] != 0 and state[i] != GOAL[i])


def heuristic_manhattan(state):
    """H2 - Distância Manhattan (admissível e consistente)."""
    dist = 0
    for i in range(9):
        if state[i] != 0:
            curr_r, curr_c = i // 3, i % 3
            goal_r, goal_c = GOAL_POS[state[i]]
            dist += abs(curr_r - goal_r) + abs(curr_c - goal_c)
    return dist


def heuristic_linear_conflict(state):
    """
    H3 - Conflito Linear + Manhattan (admissível, domina Manhattan).
    Adiciona 2 para cada par de peças em conflito linear na mesma linha/coluna.
    """
    h = heuristic_manhattan(state)
    board = to_2d(state)

    # Conflitos em linhas
    for r in range(3):
        for c1 in range(3):
            for c2 in range(c1 + 1, 3):
                tj = board[r][c1]
                tk = board[r][c2]
                if tj != 0 and tk != 0:
                    gj_r, gj_c = GOAL_POS[tj]
                    gk_r, gk_c = GOAL_POS[tk]
                    if gj_r == r and gk_r == r and gj_c > gk_c:
                        h += 2

    # Conflitos em colunas
    for c in range(3):
        for r1 in range(3):
            for r2 in range(r1 + 1, 3):
                tj = board[r1][c]
                tk = board[r2][c]
                if tj != 0 and tk != 0:
                    gj_r, gj_c = GOAL_POS[tj]
                    gk_r, gk_c = GOAL_POS[tk]
                    if gj_c == c and gk_c == c and gj_r > gk_r:
                        h += 2

    return h


HEURISTICS = {
    "misplaced": heuristic_misplaced,
    "manhattan": heuristic_manhattan,
    "linear_conflict": heuristic_linear_conflict,
}


# ─────────────────────────────────────────────
# 4. ALGORITMOS DE BUSCA
# ─────────────────────────────────────────────

class SearchResult:
    def __init__(self, path, actions, nodes_visited, nodes_generated, depth, elapsed):
        self.path = path
        self.actions = actions
        self.nodes_visited = nodes_visited
        self.nodes_generated = nodes_generated
        self.depth = depth
        self.elapsed = elapsed
        self.found = path is not None

    def __repr__(self):
        if not self.found:
            return "Sem solução encontrada."
        return (
            f"Profundidade: {self.depth} | "
            f"Nós visitados: {self.nodes_visited} | "
            f"Nós gerados: {self.nodes_generated} | "
            f"Tempo: {self.elapsed:.6f}s"
        )


def reconstruct_path(came_from, state):
    path, actions = [], []
    while state in came_from:
        prev, action = came_from[state]
        actions.append(action)
        path.append(state)
        state = prev
    path.append(state)
    path.reverse()
    actions.reverse()
    return path, actions


# ── BFS ──────────────────────────────────────

def bfs(initial):
    start = time.time()
    if initial == GOAL:
        return SearchResult([initial], [], 0, 1, 0, time.time() - start)

    frontier = deque([(initial, None, None)])   # (state, parent, action)
    came_from = {}   # state → (parent, action)
    visited = {initial}
    nodes_generated = 1

    while frontier:
        state, parent, action = frontier.popleft()
        if parent is not None:
            came_from[state] = (parent, action)

        for next_state, act in get_successors(state):
            nodes_generated += 1
            if next_state not in visited:
                visited.add(next_state)
                if next_state == GOAL:
                    came_from[next_state] = (state, act)
                    path, actions = reconstruct_path(came_from, next_state)
                    return SearchResult(path, actions, len(visited), nodes_generated, len(actions), time.time() - start)
                frontier.append((next_state, state, act))

    return SearchResult(None, None, len(visited), nodes_generated, -1, time.time() - start)


# ── DFS ──────────────────────────────────────

def dfs(initial, depth_limit=50):
    start = time.time()
    if initial == GOAL:
        return SearchResult([initial], [], 0, 1, 0, time.time() - start)

    stack = [(initial, None, None, 0)]   # (state, parent, action, depth)
    came_from = {}
    visited = {initial}
    nodes_generated = 1

    while stack:
        state, parent, action, depth = stack.pop()
        if parent is not None and state not in came_from:
            came_from[state] = (parent, action)

        if state == GOAL:
            path, actions = reconstruct_path(came_from, state)
            return SearchResult(path, actions, len(visited), nodes_generated, len(actions), time.time() - start)

        if depth < depth_limit:
            for next_state, act in reversed(get_successors(state)):
                nodes_generated += 1
                if next_state not in visited:
                    visited.add(next_state)
                    stack.append((next_state, state, act, depth + 1))

    return SearchResult(None, None, len(visited), nodes_generated, -1, time.time() - start)


# ── UCS (Custo Uniforme) ──────────────────────

def ucs(initial):
    start = time.time()
    if initial == GOAL:
        return SearchResult([initial], [], 0, 1, 0, time.time() - start)

    # (custo, contador, estado, parent, action)
    counter = 0
    heap = [(0, counter, initial, None, None)]
    came_from = {}
    cost_so_far = {initial: 0}
    visited = set()
    nodes_generated = 1

    while heap:
        cost, _, state, parent, action = heapq.heappop(heap)

        if state in visited:
            continue
        visited.add(state)

        if parent is not None:
            came_from[state] = (parent, action)

        if state == GOAL:
            path, actions = reconstruct_path(came_from, state)
            return SearchResult(path, actions, len(visited), nodes_generated, len(actions), time.time() - start)

        for next_state, act in get_successors(state):
            nodes_generated += 1
            new_cost = cost + 1
            if next_state not in visited and (next_state not in cost_so_far or new_cost < cost_so_far[next_state]):
                cost_so_far[next_state] = new_cost
                counter += 1
                heapq.heappush(heap, (new_cost, counter, next_state, state, act))

    return SearchResult(None, None, len(visited), nodes_generated, -1, time.time() - start)


# ── GREEDY (Gulosa) ───────────────────────────

def greedy(initial, heuristic=heuristic_manhattan):
    start = time.time()
    if initial == GOAL:
        return SearchResult([initial], [], 0, 1, 0, time.time() - start)

    counter = 0
    heap = [(heuristic(initial), counter, initial, None, None)]
    came_from = {}
    visited = set()
    nodes_generated = 1

    while heap:
        _, _, state, parent, action = heapq.heappop(heap)

        if state in visited:
            continue
        visited.add(state)

        if parent is not None:
            came_from[state] = (parent, action)

        if state == GOAL:
            path, actions = reconstruct_path(came_from, state)
            return SearchResult(path, actions, len(visited), nodes_generated, len(actions), time.time() - start)

        for next_state, act in get_successors(state):
            nodes_generated += 1
            if next_state not in visited:
                counter += 1
                heapq.heappush(heap, (heuristic(next_state), counter, next_state, state, act))

    return SearchResult(None, None, len(visited), nodes_generated, -1, time.time() - start)


# ── A* ────────────────────────────────────────

def astar(initial, heuristic=heuristic_manhattan):
    start = time.time()
    if initial == GOAL:
        return SearchResult([initial], [], 0, 1, 0, time.time() - start)

    counter = 0
    heap = [(heuristic(initial), counter, initial, None, None, 0)]
    came_from = {}
    g_score = {initial: 0}
    visited = set()
    nodes_generated = 1

    while heap:
        f, _, state, parent, action, g = heapq.heappop(heap)

        if state in visited:
            continue
        visited.add(state)

        if parent is not None:
            came_from[state] = (parent, action)

        if state == GOAL:
            path, actions = reconstruct_path(came_from, state)
            return SearchResult(path, actions, len(visited), nodes_generated, len(actions), time.time() - start)

        for next_state, act in get_successors(state):
            nodes_generated += 1
            new_g = g + 1
            if next_state not in visited and (next_state not in g_score or new_g < g_score[next_state]):
                g_score[next_state] = new_g
                f_score = new_g + heuristic(next_state)
                counter += 1
                heapq.heappush(heap, (f_score, counter, next_state, state, act, new_g))

    return SearchResult(None, None, len(visited), nodes_generated, -1, time.time() - start)


# ─────────────────────────────────────────────
# 5. INTERFACE DE LINHA DE COMANDO
# ─────────────────────────────────────────────

def parse_input(raw):
    """Converte entrada do usuário em tupla de estado."""
    nums = list(map(int, raw.strip().split()))
    if len(nums) != 9 or set(nums) != set(range(9)):
        raise ValueError("Entrada inválida: informe 9 números distintos de 0 a 8.")
    return tuple(nums)


def select_algorithm():
    print("\nEscolha o algoritmo:")
    print("  1 - BFS (Busca em Largura)")
    print("  2 - DFS (Busca em Profundidade)")
    print("  3 - UCS (Busca de Custo Uniforme)")
    print("  4 - Greedy (Busca Gulosa)")
    print("  5 - A* com heurística Peças Fora do Lugar")
    print("  6 - A* com heurística Distância Manhattan")
    print("  7 - A* com heurística Conflito Linear")
    choice = input("Opção: ").strip()
    return choice


def run_algorithm(choice, state):
    if choice == "1":
        return "BFS", bfs(state)
    elif choice == "2":
        return "DFS", dfs(state)
    elif choice == "3":
        return "UCS (Custo Uniforme)", ucs(state)
    elif choice == "4":
        return "Greedy (Manhattan)", greedy(state, heuristic_manhattan)
    elif choice == "5":
        return "A* (Peças Fora do Lugar)", astar(state, heuristic_misplaced)
    elif choice == "6":
        return "A* (Manhattan)", astar(state, heuristic_manhattan)
    elif choice == "7":
        return "A* (Conflito Linear)", astar(state, heuristic_linear_conflict)
    else:
        print("Opção inválida.")
        return None, None


def print_solution(name, result):
    print(f"\n{'='*50}")
    print(f"Algoritmo: {name}")
    print(f"{'='*50}")
    if not result.found:
        print("Sem solução encontrada.")
        return
    print(f"Nós visitados   : {result.nodes_visited}")
    print(f"Nós gerados     : {result.nodes_generated}")
    print(f"Profundidade    : {result.depth}")
    print(f"Tempo           : {result.elapsed:.6f}s")
    print(f"\nCaminho ({result.depth} movimentos):")
    for i, state in enumerate(result.path):
        if i == 0:
            print("  Estado inicial:")
        elif i == len(result.path) - 1:
            print(f"  Passo {i} — {result.actions[i-1]} → GOAL:")
        else:
            print(f"  Passo {i} — {result.actions[i-1]}:")
        board = to_2d(state)
        for row in board:
            print("    " + " ".join(str(x) if x != 0 else "_" for x in row))
        print()


def main():
    print("=" * 50)
    print("  8-PUZZLE SOLVER — PUC Minas IA")
    print("  Aluno: João Comini Cesar de Andrade")
    print("=" * 50)
    print("\nEstado objetivo:")
    print_board(GOAL)

    raw = input("Informe o estado inicial (9 números separados por espaço, 0=vazio):\n> ")
    try:
        state = parse_input(raw)
    except ValueError as e:
        print(f"Erro: {e}")
        return

    print("\nEstado inicial:")
    print_board(state)

    if not is_solvable(state):
        print("Este estado NÃO tem solução (número de inversões ímpar).")
        return

    print("Estado solucionável ✓")

    choice = select_algorithm()
    name, result = run_algorithm(choice, state)
    if result:
        print_solution(name, result)


if __name__ == "__main__":
    main()
