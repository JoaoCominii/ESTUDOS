# Testes

Documentação dos testes implementados no FarmáciaFinder.

## Como executar

```bash
npm install       # primeira vez (instala a dependência `ws`)
npm test          # executa a suíte de testes
```

A suíte usa **apenas Node.js** (sem framework externo). O runner está em
`tests/executar-testes.js` e os utilitários de asserção em `tests/harness.js`.

## Cobertura (39 testes)

| Arquivo de teste            | Componente alvo      | Qtde | O que valida |
| --------------------------- | -------------------- | ---- | ------------ |
| `tests/geometria.test.js`   | Consulta espacial    | 24   | Operadores espaciais, distâncias, polígono/círculo, bounds |
| `tests/regras.test.js`      | Filtros / Resultados | 13   | Filtros por tipo/categoria, ordenação por proximidade, raio, combinação com operador, normalização |
| `tests/mapa.test.js`        | Mapa                 | 4    | Cálculos de enquadramento usados na visualização |

### 1. Teste de validação de filtros (ordenação correta)

Cobre a **Regra 3** das estórias: "farmácias e laboratórios encontrados devem
ser apresentados em ordem de proximidade".

- `resultados são ordenados por distância crescente (mais próximo primeiro)` —
  verifica que o resultado é uma sequência não decrescente de distâncias e que
  o mais próximo vem primeiro.
- `raio de busca elimina resultados além do raio` e
  `nenhum estabelecimento além do raio é retornado` — validam o filtro espacial
  por raio.
- `filtrar por tipo "farmacia"` / `por categoria` / `tipo + categoria` —
  validam os filtros de tipo de estabelecimento e categoria.

### 2. Teste de consultas espaciais (compatibilidade com o sistema de dados)

Valida a semântica dos operadores **está contido / contém / intercepta** sobre
polígono e círculo:

- `estacontido: ponto interno atende, ponto externo não` — inclusão estrita.
- `estacontido: ponto na borda NÃO atende` — borda não é "dentro".
- `contem: ponto interno com buffer maior que a distância à borda não atende` —
  o buffer deve caber inteiro.
- `intercepta: ponto próximo da borda (dentro do buffer) atende` — alcance do
  buffer.
- `hierarquia: intercepta ⊇ estacontido ⊇ contem`.
- Casos com círculo: pontos a 500 m, 900 m, 1.100 m e 2.000 m do centro com
  raio de 1.000 m, confirmando a compatibilidade entre os três operadores.
- `círculo de 300 m + estacontido` combina consulta espacial com filtros.

> As mesmas operações são suportadas nativamente pelo WFS da PBH
> (`Within`, `Contains`, `Intersects`), conforme declarado em suas capacidades.

### 3. Teste do mapa (visualização correta)

A camada visual usa Leaflet e exige navegador; os cálculos determinísticos da
visualização são automatizados em `tests/mapa.test.js`:

- `bounds contêm o centro de referência` e `pontos dentro do raio cabem no
  bounds` — garantem que o enquadramento (`fitBounds`) não corta resultados.
- `raio maior produz área maior` e `span leste-oeste acompanha a latitude` —
  garantem proporcionalidade correta.

#### Checklist manual (teste de usabilidade do mapa)

Realizar com o servidor rodando (`npm start` → `http://localhost:3000`):

1. O mapa abre centrado no primeiro hospital, com marcador 🏥 e círculo do raio.
2. Alterar o raio nos filtros redesenha o círculo e atualiza resultados.
3. Selecionar outro hospital no dropdown reposiciona o centro.
4. Clicar em "Mudar centro pelo mapa" e clicar no mapa cria novo centro 📍.
5. Desenhar um polígono e aplicar cada operador; conferir que os marcadores
   dentro da área mudam conforme `estacontido` → `contem` → `intercepta`.
6. Alternar Lista / Mapa / Lista + Mapa e conferir que a lista mantém a ordem
   de proximidade (mais próximo no topo).
7. Abrir uma segunda aba apontando para a mesma URL e conferir que centro,
   filtros, operador e visão do mapa são sincronizados em tempo real.

## Saída esperada

```
==================================================
  ✅ Todos os 39 testes passaram.
==================================================
```

Em caso de falha, o runner detalha cada teste que falhou e encerra com código
de saída diferente de zero (útil para CI).
