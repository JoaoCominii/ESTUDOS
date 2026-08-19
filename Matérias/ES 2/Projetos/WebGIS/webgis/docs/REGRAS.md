# Regras Extraídas das Estórias e Justificativas

Regras de negócio derivadas das estórias de usuário da especificação DCSU do
FarmáciaFinder e onde cada uma está implementada/testada.

## Regras

### Regra 1 — O ponto de referência inicial é o hospital

**Enunciado:** "O ponto de referência inicial é o hospital."

**Implementação:** ao carregar os dados, a aplicação seleciona automaticamente
o primeiro hospital da lista como centro de referência (`app.js` →
`definirCentro(hospitais[0])`). O hospital de referência **não aparece nos
resultados** (ele é a origem da busca, não o alvo).

**Justificativa:** o propósito do sistema é "localizar farmácias próximas a um
hospital"; portanto o hospital é a âncora do cálculo de distâncias e do raio de
busca.

**Onde:** `frontend/js/app.js`, `frontend/js/regras.js` (`executarConsulta`).
**Teste:** `tests/regras.test.js` (o próprio hospital não entra nos resultados).

---

### Regra 2 — O usuário deve poder definir ou escolher um raio de busca

**Enunciado:** "O usuário deve poder definir ou escolher um raio de busca."

**Implementação:** o componente de filtros oferece valores predefinidos
(200 m, 500 m, 1 km, 2 km, 5 km, 10 km) e um campo numérico livre que
sincroniza com o preselecionado (`filtros.js`). O raio é aplicado como filtro
espacial por distância ao centro.

**Justificativa:** atende às duas formas previstas — escolher um valor pronto
ou definir um valor próprio.

**Onde:** `frontend/js/filtros.js` (campo + presets), `frontend/js/regras.js`
(`dentroDoRaio`, `executarConsulta`).
**Testes:** `tests/regras.test.js` (raio elimina resultados além do limite).

---

### Regra 3 — Apresentar em ordem de proximidade

**Enunciado:** "As farmácias e os laboratórios encontrados devem ser
apresentados em ordem de proximidade."

**Implementação:** `executarConsulta` ordena os resultados por distância
crescente ao centro (Haversine). Essa ordenação é obrigatória (não é opcional
na interface).

**Justificativa:** o principal valor da ferramenta é mostrar o que está "mais
perto primeiro", facilitando a decisão do usuário.

**Onde:** `frontend/js/regras.js` (sort por `distancia`), `frontend/js/resultados.js`.
**Teste:** `tests/regras.test.js` (ordem não decrescente, mais próximo primeiro).

---

### Regra 4 — O sistema deve permitir filtrar por categoria

**Enunciado:** "O sistema deve permitir filtrar por categoria."

**Implementação:** existe um filtro de categoria (`filtros.js`) populado
dinamicamente a partir das categorias reais presentes nos dados
(`extrairCategorias`). Exemplos: farmácias `rede`/`independente`, laboratórios
`análises clínicas`/`anatomia patológica`, hospitais `público`/`privado`.

**Justificativa:** categorias permitem refinar a busca além do tipo de
estabelecimento, conforme solicitado.

**Onde:** `frontend/js/filtros.js`, `frontend/js/regras.js` (`aplicarFiltros`).
**Testes:** `tests/regras.test.js` (filtro por categoria).

---

## Funcionalidades adicionais

| Funcionalidade | Implementação | Justificativa |
| -------------- | ------------- | ------------- |
| **Mudança de centro** (cidade/região de interesse) | Botão "Mudar centro pelo mapa" define novo centro por clique; `'__custom__'` identifica centro personalizado. | Permite buscar em qualquer região, não apenas nos hospitais. |
| **Exibição em lista ou no mapa** | Seletor Lista / Mapa / Lista + Mapa no componente de resultados. | O usuário escolhe a forma mais conveniente de visualizar. |
| **Filtro por categoria** | `select` de categorias populado pelos dados. | Complementa o filtro por tipo. |

---

## Operadores espaciais (consulta espacial)

| Operador | Interpretação adotada | Equivalente OGC/PostGIS |
| -------- | --------------------- | ----------------------- |
| Está contido | Ponto do estabelecimento estritamente dentro da área | `ST_Within` |
| Contém | A área contém o ponto e todo o seu buffer | `ST_Contains` + buffer |
| Intercepta | O ponto ou seu buffer toca/cruza a área | `ST_Intersects` |

A relação `intercepta ⊇ estacontido ⊇ contem` torna os operadores distintos e
testáveis. O servidor WFS da PBH declara suporte nativo aos três operadores,
validando a compatibilidade da consulta com o sistema de dados.

---

## Documentação de testes

- Modelos de dados de cada componente: `docs/MODELOS_DE_DADOS.md`
- Testes implementados e como executá-los: `docs/TESTES.md`
