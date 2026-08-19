// executar-testes.js — Roda a suíte de testes do FarmáciaFinder.
import './geometria.test.js';
import './regras.test.js';
import './mapa.test.js';
import { relatorio } from './harness.js';

relatorio();
