# Modelos de Dados dos Componentes

Documentação dos modelos de dados de cada componente do **FarmáciaFinder**:
filtros, consulta espacial, mapa, resultados e compartilhamento.

---

## 1. Componente Filtros (`filtros.js` + `regras.js`)

Responsável por configurar a busca: tipo de estabelecimento, raio de busca ao
centro do sistema de dados e categoria.

### 1.1 Estabelecimento

Estrutura normalizada para qualquer unidade (hospital, farmácia ou laboratório),
originada da API WFS da PBH ou do dataset de demonstração (fallback).

| Campo      | Tipo            | Descrição                                      |
| ---------- | --------------- | ---------------------------------------------- |
| `id`       | `string`        | Identificador único (ID da API ou gerado)      |
| `nome`     | `string`        | Nome do estabelecimento                        |
| `tipo`     | `string`        | `'hospital'` \| `'farmacia'` \| `'laboratorio'` |
| `categoria`| `string`        | Categoria (ex.: `rede`, `independente`, `análises clínicas`, `público`) |
| `lat`      | `number`        | Latitude (EPSG:4326)                           |
| `lng`      | `number`        | Longitude (EPSG:4326)                          |
| `endereco` | `string`        | Endereço formatado                              |
| `telefone` | `string`        | Telefone (se disponível)                       |
| `origem`   | `'api' \| 'fallback'` | Fonte dos dados                          |

### 1.2 Filtros

| Campo       | Tipo     | Descrição                                              |
| ----------- | -------- | ------------------------------------------------------ |
| `tipo`      | `string` | `'todos'` ou um tipo de estabelecimento                |
| `categoria` | `string` | `'todos'` ou uma categoria específica                  |
| `raio`      | `number` | Raio de busca em metros (distância ao centro)          |
| `centroId`  | `string` | Id do hospital selecionado ou `'__custom__'`           |

### 1.3 Centro de Referência

| Campo  | Tipo     | Descrição                                             |
| ------ | -------- | ----------------------------------------------------- |
| `id`   | `string` | Id do hospital ou `'__custom__'` (ponto do mapa)      |
| `nome` | `string` | Nome exibido do centro                                |
| `lat`  | `number` | Latitude                                              |
| `lng`  | `number` | Longitude                                             |

---

## 2. Componente Consulta Espacial (`consultaEspacial.js` + `geometria.js`)

Responsável por executar consultas espaciais com os operadores
**está contido**, **contém** e **intercepta**.

### 2.1 Geometria de busca

| Tipo de geometria | Campos                        | Descrição                          |
| ----------------- | ----------------------------- | ---------------------------------- |
| `poligono`        | `vertices: [[lat, lng], …]`   | Polígono desenhado no mapa          |
| `circulo`         | `centro: {lat, lng}`, `raio`   | Círculo centrado em um ponto       |

### 2.2 Consulta

| Campo             | Tipo     | Descrição                                             |
| ----------------- | -------- | ----------------------------------------------------- |
| `operador`        | `string` | `'estacontido'` \| `'contem'` \| `'intercepta'`       |
| `raioInfluencia`  | `number` | Buffer (metros) em torno do estabelecimento           |
| `geometria`       | `object` | Polígono ou círculo usado na consulta                 |

### 2.3 Semântica dos operadores

| Operador    | Significado (ponto `P`, geometria `G`, buffer `b`)           |
| ----------- | ------------------------------------------------------------- |
| estacontido | `P` está **estritamente** dentro de `G`                       |
| contem      | `G` contém `P` **e** o buffer `b` de `P` está totalmente dentro de `G` |
| intercepta  | o buffer `b` de `P` **toca ou cruza** `G` (inclui `P` na borda) |

**Relação:** `intercepta ⊇ estacontido ⊇ contem` (mais restritivo → mais
abrangente). Essa interpretação mapeia os operadores da especificação DCSU aos
operadores padronizados OGC/PostGIS: `ST_Within`, `ST_Contains`, `ST_Intersects`
(o próprio servidor WFS da PBH declara suporte a `Within`, `Contains` e
`Intersects` em suas capacidades espaciais).

### 2.4 Algoritmos

| Algoritmo                     | Arquivo       | Função                                        |
| ----------------------------- | ------------- | --------------------------------------------- |
| Distância entre pontos        | `geometria.js`| `calcularDistancia` (Haversine)               |
| Ponto em polígono (com borda) | `geometria.js`| `pontoEmPoligono` (ray casting)               |
| Ponto estritamente dentro     | `geometria.js`| `pontoDentroPoligono`                         |
| Distância ponto-polígono      | `geometria.js`| `distanciaMinimaAoPoligono`                   |
| Avaliação do operador         | `geometria.js`| `avaliarOperadorEspacial`                     |
| Bounds de enquadramento       | `geometria.js`| `calcularBoundsEmVolta`                       |

---

## 3. Componente Mapa (`mapa.js`)

Estado visual mantido pelo mapa (Leaflet + OpenStreetMap).

| Estado               | Tipo     | Descrição                                   |
| -------------------- | -------- | ------------------------------------------- |
| `camadaCentro`       | `L.layerGroup` | Marcador do centro de referência        |
| `camadaRaio`         | `L.layerGroup` | Círculo do raio de busca                |
| `camadaResultados`   | `L.layerGroup` | Marcadores de farmácias/laboratórios    |
| `camadaPoligono`     | `L.layer` | Polígono desenhado para consulta espacial   |
| `drawnItems`         | `L.featureGroup` | Camada de edição do Leaflet.Draw         |

Marcadores:
- Centro: ícone `🏥` (borda vermelha, maior).
- Farmácia: ícone `💊` (borda verde).
- Laboratório: ícone `🔬` (borda roxa).

---

## 4. Componente Resultados (`resultados.js` + `regras.js`)

### 4.1 Resultado

| Campo           | Tipo            | Descrição                             |
| --------------- | --------------- | ------------------------------------- |
| `estabelecimento` | `Estabelecimento` | Unidade encontrada                  |
| `distancia`     | `number \| null` | Distância (m) do centro (null p/ centro ausente) |

### 4.2 Modo de exibição

| Valor   | Exibição                                   |
| ------- | ------------------------------------------ |
| `lista` | Apenas lista (ordenada por proximidade)    |
| `mapa`  | Apenas marcadores no mapa                  |
| `ambos` | Lista + mapa (padrão)                      |

---

## 5. Componente Compartilhamento (`compartilhamento.js` + `backend/server.js`)

Estado compartilhado em tempo real via WebSocket.

### 5.1 Mensagem `estado` (cliente → servidor → outros clientes)

| Campo      | Tipo   | Descrição                                      |
| ---------- | ------ | ---------------------------------------------- |
| `visao`    | `{lat, lng, zoom}` | Visão atual do mapa                    |
| `centro`   | `Centro` | Centro de referência                     |
| `filtros`  | `Filtros` | Filtros de busca                        |
| `consulta` | `{operador, raioInfluencia, geometria}` | Consulta espacial |
| `modo`     | `string` | Modo de exibição dos resultados          |
| `data`     | `number` | Timestamp da mensagem                   |

### 5.2 Mensagens de controle

| Tipo              | Descrição                                    |
| ----------------- | -------------------------------------------- |
| `bem-vindo`       | Enviada ao conectar (contém id e nº usuários)|
| `usuarios`        | Contagem de usuários online (broadcast)      |
| `estado-inicial`  | Último estado conhecido (ao conectar)        |
| `estado`          | Estado atual (broadcast para os demais)      |

---

## 6. Fonte de dados (`dados.js`)

| Fonte | Origem | Descrição |
| ----- | ------ | --------- |
| API WFS PBH | `http://bhmap.pbh.gov.br/v2/api/idebhgeo/wfs` | Dados reais, via proxy local `/api/pbh` (contorna CORS) |
| Fallback | dataset embutido | 40 estabelecimentos de demonstração em BH, usado se a API falhar |

### Camadas reais utilizadas

| Tipo        | Camada / filtro                          | Registros |
| ----------- | ---------------------------------------- | --------- |
| Hospitais   | `ide_bhgeo:HOSPITAIS`                    | 98        |
| Farmácias   | `ide_bhgeo:ATIVIDADE_ECONOMICA` (CNAE `4771%`) | 1.365 |
| Laboratórios| `ide_bhgeo:ATIVIDADE_ECONOMICA` (CNAE `864%`)  | 916 |

> Observação: o CDN da PBH bloqueia parênteses e `OR` na query string (retorna
> HTTP 403); por isso os filtros CQL usam a forma `LIKE` sem parênteses.
