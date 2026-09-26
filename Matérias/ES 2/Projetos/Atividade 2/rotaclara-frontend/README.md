# RotaClara — Front-end

Front-end do MVP de monitoramento de tempo parado em roteiros
(Engenharia de Software II, PUC Minas), implementado em **React 18 +
TypeScript + Vite**, seguindo o mesmo layout validado no protótipo
HTML entregue anteriormente.

## Como rodar

```bash
npm install
npm run dev
```

Abre em `http://localhost:5173`. Para gerar a versão de produção:

```bash
npm run build   # gera ./dist
npm run preview # serve o build de produção localmente
```

## Estrutura

```
src/
  components/     Sidebar, Layout, Kpi, Pill, RouteStrip, ícones
  domain/regras.ts  Regras de negócio RN01–RN07 como funções puras
  types/domain.ts   Tipos alinhados ao modelo de dados da especificação
  api/client.ts     Cliente HTTP tipado, autenticação JWT e endpoints REST
  data/mock.ts      Utilitários de formatação e valores padrão de fallback
  pages/            Uma página por tela do protótipo:
                     Painel, Historico, Roteiros, Pontos,
                     Registrar, Motoristas, Parametros
  App.tsx           Rotas (react-router-dom)
  main.tsx          Ponto de entrada
```

## Sobre as regras de negócio

As regras RN01–RN07 do documento de especificação estão implementadas
como funções puras em `src/domain/regras.ts`
(`calcularTempoParadoPonto`, `calcularTempoTotalParadoRoteiro`,
`calcularCustoRoteiro`, `calcularCustoPorKm`, `percentualDaJornadaParado`,
`pontoEmAlerta`), separadas dos dados e da UI. Isso facilita:

- Testar essas regras isoladamente (ex.: com Vitest) sem precisar
  renderizar componentes.
- Trocar `src/data/mock.ts` por chamadas reais ao back-end (Spring
  Boot) sem tocar nas regras nem nas páginas — as páginas continuam
  chamando as mesmas funções.

## Integração com o back-end

O frontend usa a API REST do Spring Boot em `http://localhost:8080` por
padrão. A URL pode ser alterada com `VITE_API_URL`. O fluxo de acesso é:

1. Subir o PostgreSQL e o backend com `mvn spring-boot:run`.
2. Subir o frontend com `npm run dev`.
3. Entrar com `administrador` / `admin123` (ou um dos usuários de exemplo
  documentados no README do backend).

As telas de painel, histórico, roteiros, pontos, motoristas, parâmetros e
registro de chegada/saída usam chamadas reais autenticadas com `Bearer JWT`.
Os tipos de `types/domain.ts` são adaptados dos DTOs do backend no cliente
`src/api/client.ts`.

## Pendências conhecidas

- Exportação de relatório (RF12) é só um botão de exemplo.
