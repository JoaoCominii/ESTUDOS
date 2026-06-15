# Trabalho Prático N.02 — Problema dos k-Centros
## Teoria dos Grafos e Computabilidade — PUC Minas

### Arquivos

| Arquivo | Descrição |
|---|---|
| `relatorio_tp02.pdf` | Relatório em PDF (compilado do LaTeX) |
| `main.tex` | Fonte LaTeX do relatório |
| `refs.bib` | Referências bibliográficas |
| `sbc2023.cls` | Classe JBCS (arquivos do template) |
| `KCentrosExato.java` | Algoritmo exato (busca binária + cobertura gulosa) |
| `KCentrosGonzalez.java` | Heurística de Gonzalez (2-aproximação) |
| `Experimentos.java` | Roda as 40 instâncias e salva resultados em CSV |

### Como compilar e executar

```bash
# Compilar
javac KCentrosExato.java
javac KCentrosGonzalez.java
javac -cp . Experimentos.java

# Algoritmo exato em uma instância
java KCentrosExato pmed1.txt

# Heurística de Gonzalez em uma instância
java KCentrosGonzalez pmed1.txt

# Experimentos completos (todas as 40 instâncias)
java Experimentos <pasta_com_instancias>
```

### Instâncias

As 40 instâncias devem ser baixadas da OR-Library:
http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files
(arquivos pmed1.txt a pmed40.txt)

### Compilar o relatório LaTeX

```bash
xelatex main.tex
bibtex main
xelatex main.tex
xelatex main.tex
```
