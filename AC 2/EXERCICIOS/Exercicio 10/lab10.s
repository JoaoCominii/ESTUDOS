.data
buffer: .space 100
msg:    .string "Digite algo para inverter: "
newline:.string "\n"

.text
.globl main
main:
    # 1. Imprime mensagem
    li a0, 4
    la a1, msg
    ecall

    # 2. Lê a string do teclado (usando nosso fgets)
    la a0, buffer    # Onde guardar
    li a1, 100       # Tamanho máximo
    call fgets

    # 3. Inverte a string (usando nosso strrev)
    la a0, buffer
    call strrev

    # 4. Imprime o resultado (já invertido no buffer)
    li a0, 4
    la a1, buffer
    ecall

    # 5. Nova linha e Sair
    li a0, 4
    la a1, newline
    ecall

    li a0, 10
    ecall

# =================================================================
# FUNÇÃO STRREV: Inverte uma string no próprio lugar (in-place)
# void strrev(char *s (a0))
# =================================================================
strrev:
    # Salva RA e S0 na pilha (S0 para segurar o ponteiro inicial)
    addi sp, sp, -8
    sw   ra, 4(sp)
    sw   s0, 0(sp)

    mv   s0, a0          # s0 = ponteiro INICIO (preservado)

    # Chama strlen para saber onde é o fim
    # a0 já está com o endereço da string
    call strlen          # Retorna tamanho em a0

    # Se tamanho for 0 ou 1, não precisa inverter
    li   t0, 1
    ble  a0, t0, fim_rev

    # Calcula ponteiro FIM
    # fim = inicio + tamanho - 1 (para pular o \0 e pegar o último char)
    add  t1, s0, a0      # t1 = inicio + tamanho (aponta para o \0)
    addi t1, t1, -1      # t1 = aponta para o último caractere útil

    mv   t0, s0          # t0 = ponteiro INICIO (cópia para o loop)

loop_rev:
    bge  t0, t1, fim_rev # Se inicio >= fim, paramos

    # Troca (Swap)
    lbu  t2, 0(t0)       # carrega char do inicio
    lbu  t3, 0(t1)       # carrega char do fim

    sb   t3, 0(t0)       # grava fim no inicio
    sb   t2, 0(t1)       # grava inicio no fim

    addi t0, t0, 1       # inicio++
    addi t1, t1, -1      # fim--
    j    loop_rev

fim_rev:
    # Restaura pilha
    lw   s0, 0(sp)
    lw   ra, 4(sp)
    addi sp, sp, 8
    ret

# =================================================================
# FUNÇÃO STRLEN: Retorna o tamanho da string
# int strlen(char *s (a0))
# =================================================================
strlen:
    mv   t0, a0          # Salva inicio para calculo final
loop_len:
    lbu  t1, 0(a0)       # Lê byte
    beqz t1, calc_len    # Se 0, fim
    addi a0, a0, 1       # Próximo
    j    loop_len
calc_len:
    sub  a0, a0, t0      # Tamanho = End. Atual - End. Inicial
    ret

# =================================================================
# FUNÇÃO FGETS: Lê string do teclado com limite (Polling no Venus)
# char * fgets(char *s (a0), int N (a1))
# =================================================================
fgets:
    mv   t0, a0          # t0 = ponteiro de escrita
    mv   t3, a1          # t3 = Limite N
    addi t3, t3, -1      # Deixa espaço para o \0

    # Habilitar teclado (ecall 0x130)
    li   a0, 0x130
    ecall

loop_fgets:
    beqz t3, fim_fgets   # Se encheu o buffer, para

    # Verifica se tem char (Polling - ecall 0x131)
    li   a0, 0x131
    ecall

    li   t1, 1
    beq  a0, t1, loop_fgets # Se 1, nada digitado ainda, repete
    beqz a0, fim_fgets      # Se 0, entrada fechada

    # Se a0 == 2, tem char em a1
    li   t2, 10             # ASCII para \n (Enter)
    beq  a1, t2, remove_nl  # Se for Enter, para a leitura

    sb   a1, 0(t0)          # Salva o char no buffer
    addi t0, t0, 1          # Avança ponteiro
    addi t3, t3, -1         # Decrementa limite
    j    loop_fgets

remove_nl:
    # Opcional: Se quiser pular o \n e não gravar na string, vem direto pra cá.
    # Se quiser gravar o \n, faça o sb antes. Aqui optamos por não gravar o \n.

fim_fgets:
    sb   zero, 0(t0)        # Adiciona o \0 final
    ret