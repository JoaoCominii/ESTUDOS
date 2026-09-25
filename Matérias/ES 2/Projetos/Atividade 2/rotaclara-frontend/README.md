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
  data/mock.ts      Dados de exemplo (substituir por chamadas HTTP)
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

## Integração futura com o back-end

Hoje `src/data/mock.ts` simula o que virá da API REST. Quando o
back-end (Java + Spring Boot + PostgreSQL, conforme decidido) estiver
disponível, a ideia é:

1. Criar um cliente HTTP (`src/api/client.ts`) com `fetch`.
2. Trocar as constantes de `mock.ts` por hooks (`useMotoristas()`,
   `useRoteiros()`, etc.) que chamam a API.
3. Manter os tipos de `types/domain.ts` como contrato entre front e
   back (o DTO do Spring Boot deve espelhar esses campos).

## Pendências conhecidas

- Autenticação/login e controle de acesso por perfil (RNF04) ainda
  não implementados — hoje o usuário logado é fixo ("Ana Souza").
- Exportação de relatório (RF12) é só um botão de exemplo.
- Os formulários salvam em memória (`useState`) — sem persistência
  real até a integração com o back-end.
