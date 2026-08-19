# 💊 FarmáciaFinder

Aplicação WebGIS que permite localizar **farmácias e laboratórios próximos a um
hospital** em Belo Horizonte, usando **filtros de busca**, **consultas
espaciais** (está contido / contém / intercepta) e **compartilhamento do mapa
em tempo real** entre usuários.

## ✨ Funcionalidades

- **Filtros de busca**
  - Tipo de estabelecimento (hospital, farmácia, laboratório)
  - Raio de busca ao centro do sistema de dados (predefinido ou definido pelo usuário)
  - Categoria (rede, independente, análises clínicas, ...)
- **Consulta espacial** com operadores:
  - **Está contido** (within) — ponto estritamente dentro da área
  - **Contém** (contains) — área contém o ponto + buffer de influência
  - **Intercepta** (intersects) — ponto/buffer toca ou cruza a área
- **Mapa interativo** (Leaflet)
  - Resultados em ordem de proximidade
  - Compartilhamento em tempo real via WebSocket
  - Mudança de centro pelo clique no mapa
- **Exibição dos resultados**: lista, mapa ou lista + mapa

## 🗂️ Estrutura

```
webgis/
├── backend/
│   └── server.js            # Servidor estático + WebSocket + proxy da API PBH
├── frontend/
│   ├── index.html           # Interface
│   ├── style.css            # Estilos
│   └── js/
│       ├── app.js               # Orquestração
│       ├── filtros.js           # Componente Filtros
│       ├── consultaEspacial.js  # Componente Consulta Espacial
│       ├── mapa.js              # Componente Mapa
│       ├── resultados.js        # Componente Resultados (lista/mapa)
│       ├── compartilhamento.js  # Componente Tempo Real (WebSocket)
│       ├── dados.js             # Fonte de dados (API PBH + fallback)
│       ├── geometria.js         # Operações espaciais puras
│       └── regras.js            # Regras de negócio puras
├── tests/                   # Testes automatizados (Node)
├── docs/                    # Documentação exigida
│   ├── MODELOS_DE_DADOS.md
│   ├── TESTES.md
│   └── REGRAS.md
└── package.json
```

## 🚀 Como executar

```bash
npm install     # primeira vez apenas
npm start       # servidor em http://localhost:3000
```

Acesse **http://localhost:3000** no navegador.

Para testar o **compartilhamento em tempo real**, abra uma segunda aba com a
mesma URL: centro, filtros, operador e visão do mapa ficam sincronizados.

## 🧪 Testes

```bash
npm test
```

39 testes cobrem filtros/ordenação, operadores espaciais e cálculos do mapa.
Veja `docs/TESTES.md`.

## 📖 Documentação

- **Modelos de dados** de cada componente: `docs/MODELOS_DE_DADOS.md`
- **Testes** implementados: `docs/TESTES.md`
- **Regras** extraídas das estórias e justificativas: `docs/REGRAS.md`

## 🏥 Fonte de dados

- **Primária:** API WFS da Prefeitura de Belo Horizonte (via proxy local para
  contornar CORS).
  - Hospitais: `ide_bhgeo:HOSPITAIS` (98)
  - Farmácias: `ide_bhgeo:ATIVIDADE_ECONOMICA` (CNAE `4771%`, 1.365)
  - Laboratórios: `ide_bhgeo:ATIVIDADE_ECONOMICA` (CNAE `864%`, 916)
- **Fallback:** dataset de demonstração embutido (40 registros) caso a API fique
  indisponível.

## 🛠️ Tecnologias

- Frontend: HTML5, CSS3, JavaScript (módulos ES)
- Mapa: Leaflet.js + Leaflet.Draw
- Backend: Node.js + `ws`
- Consultas espaciais: Haversine, ray casting (JavaScript puro)
- Testes: Node.js (sem framework)
