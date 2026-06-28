import pandas as pd, matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

df = pd.read_csv("resultados.csv")

fig, ax = plt.subplots(figsize=(6.6,3.6))
ex = df[df['exato_tempo_s']>=0]
ax.plot(ex['instancia'], ex['exato_tempo_s'], 'o-', color='#1f4e8c', label='Exato (quando concluído)')
ax.plot(df['instancia'], df['gonz_tempo_s'].clip(lower=1e-4), 's--', color='#b03a2e', label='Gonzalez', markersize=4)
ax.set_yscale('log')
ax.set_xlabel('Instância (pmedN)')
ax.set_ylabel('Tempo de execução (s) — escala log')
ax.set_title('Tempo de execução: Exato vs. Gonzalez')
ax.legend(fontsize=8)
ax.grid(True, which='both', linestyle=':', alpha=0.5)
plt.tight_layout()
plt.savefig("figs/tempo_comparativo.pdf")
plt.close()

fig, ax = plt.subplots(figsize=(6.6,3.6))
colors = ['#2e7d32' if v<=1.5 else '#b8860b' if v<=1.7 else '#b03a2e' for v in df['gonz_fa']]
ax.bar(df['instancia'], df['gonz_fa'], color=colors)
ax.axhline(2.0, color='black', linestyle='--', linewidth=1, label='Limite teórico (FA = 2)')
ax.axhline(df['gonz_fa'].mean(), color='gray', linestyle=':', linewidth=1, label=f"Média empírica (FA = {df['gonz_fa'].mean():.2f})")
ax.set_xlabel('Instância (pmedN)')
ax.set_ylabel('Fator de aproximação (raio Gonzalez / raio ótimo)')
ax.set_title('Fator de aproximação da heurística de Gonzalez')
ax.set_ylim(0,2.2)
ax.legend(fontsize=8, loc='upper right')
plt.tight_layout()
plt.savefig("figs/fa_gonzalez.pdf")
plt.close()
print("Figuras geradas em figs/")
