.data
# Buffer temporário para leitura inicial da string (antes de saber o tamanho exato)
buffer_temp: .space 100  
prompt_nome: .string "Digite o Nome: "
prompt_idade:.string "Digite a Idade: "
msg_saida:   .string "\n--- Dados da Pessoa (Struct Dinamica) ---\n"
txt_nome:    .string "Nome: "
txt_idade:   .string "Idade: "

.text
.globl main

# =================================================================
# MAIN: Testa a criação e impressão da Pessoa
# =================================================================
main:
    # 1. Chama LePessoa (Retorna o endereço da struct em a0)
    call LePessoa
    mv s0, a0           # Salva o ponteiro da struct em s0

    # 2. Imprime cabeçalho
    li a0, 4
    la a1, msg_saida
    ecall

    # 3. Chama ImprimePessoa passando a struct
    mv a0, s0
    call ImprimePessoa

    # 4. Encerra o programa
    li a0, 10
    ecall

# =================================================================
# ATIVIDADE 8/10: LePessoa
# Aloca struct dinâmica (12 bytes) e usa nome dinâmico
# Retorno: a0 = endereço da struct alocada
# =================================================================
LePessoa:
    addi sp, sp, -16
    sw ra, 0(sp)
    sw s0, 4(sp)        # Vai guardar o ponteiro do nome
    sw s1, 8(sp)        # Vai guardar o ponteiro da struct

    # 1. Ler o Nome Dinamicamente (Função da Atividade 7)
    #    Isso retorna um ponteiro para o Heap em a0
    call LeString
    mv s0, a0           # s0 = char *nome

    # 2. Alocar memória para a STRUCT Pessoa
    #    Tamanho: 4 (ptr nome) + 4 (int idade) + 4 (ptr prox) = 12 bytes
    li a7, 9            # sbrk (malloc)
    li a1, 12           # tamanho
    ecall
    mv s1, a0           # s1 = endereço da struct (Pessoa*)

    # 3. Preencher a Struct
    sw s0, 0(s1)        # pessoa->nome = s0 (ponteiro do nome)

    # 4. Ler a Idade
    li a0, 4
    la a1, prompt_idade
    ecall
    li a0, 5            # read_int
    ecall
    sw a0, 4(s1)        # pessoa->idade = a0

    # 5. Inicializar prox com NULL
    sw zero, 8(s1)      # pessoa->prox = NULL

    # Retorna o endereço da struct
    mv a0, s1

    lw ra, 0(sp)
    lw s0, 4(sp)
    lw s1, 8(sp)
    addi sp, sp, 16
    ret

# =================================================================
# ATIVIDADE 9: ImprimePessoa
# Recebe: a0 = endereço da struct Pessoa
# =================================================================
ImprimePessoa:
    addi sp, sp, -4
    sw ra, 0(sp)
    
    mv t0, a0           # t0 = endereço da struct

    # Imprime Label "Nome: "
    li a0, 4
    la a1, txt_nome
    ecall

    # Imprime o CONTEÚDO do nome
    # Atenção: 0(t0) contém o ENDEREÇO da string, não a string em si.
    lw a0, 0(t0)        # Carrega o ponteiro char*
    li a7, 4            # print_string
    ecall

    # Nova linha
    li a0, 11
    li a1, 10
    ecall

    # Imprime Label "Idade: "
    li a0, 4
    la a1, txt_idade
    ecall

    # Imprime a Idade
    lw a0, 4(t0)        # Carrega o int idade
    li a7, 1            # print_int
    ecall

    # Nova linha final
    li a0, 11
    li a1, 10
    ecall

    lw ra, 0(sp)
    addi sp, sp, 4
    ret

# =================================================================
# ATIVIDADE 7: LeString (Alocação Dinâmica)
# Lê string do teclado -> Buffer temp -> Aloca Heap -> Copia
# Retorno: a0 = endereço da string no Heap
# =================================================================
LeString:
    addi sp, sp, -16
    sw ra, 0(sp)
    sw s0, 4(sp)        # Tamanho
    sw s1, 8(sp)        # Ponteiro Heap

    # 1. Prompt
    li a0, 4
    la a1, prompt_nome
    ecall

    # 2. Ler para buffer estático temporário
    la a0, buffer_temp
    call LeStringSimples # Lê via polling

    # 3. Calcular tamanho exato da string lida
    la a0, buffer_temp
    call strlen
    mv s0, a0            # s0 = tamanho

    # 4. Alocar memória no Heap (malloc)
    addi a0, s0, 1       # Tamanho + 1 (para o \0)
    li a7, 9             # sbrk
    mv a1, a0
    ecall
    mv s1, a0            # s1 = endereço da nova memória

    # 5. Copiar do buffer estático para o Heap
    mv a0, s1            # Destino
    la a1, buffer_temp   # Origem
    call strcpy

    # Retorna endereço do Heap
    mv a0, s1

    lw ra, 0(sp)
    lw s0, 4(sp)
    lw s1, 8(sp)
    addi sp, sp, 16
    ret

# =================================================================
# FUNÇÕES AUXILIARES (Strings e Polling)
# =================================================================

# Leitura caractere por caractere (Polling)
# a0 = buffer destino
LeStringSimples:
    mv t0, a0
    # Habilita teclado
    li a7, 0x130
    ecall
loop_ls:
    li a7, 0x131        # Check teclado
    ecall
    li t1, 1
    beq a7, t1, loop_ls # Se nada digitado, repete
    beqz a7, fim_ls     # Se erro/eof, sai
    
    # Verifica Enter (\n = 10)
    li t2, 10
    beq a1, t2, fim_ls
    
    sb a1, 0(t0)        # Salva char
    addi t0, t0, 1
    j loop_ls

fim_ls:
    sb zero, 0(t0)      # Add \0
    ret

# strlen(char *s) -> a0 = tamanho
strlen:
    mv t0, a0
loop_len:
    lbu t1, 0(t0)
    beqz t1, fim_len
    addi t0, t0, 1
    j loop_len
fim_len:
    sub a0, t0, a0
    ret

# strcpy(char *dest, char *src) -> copia src para dest
strcpy:
    mv t0, a0           # Salva inicio dest
loop_cpy:
    lbu t1, 0(a1)
    sb t1, 0(a0)
    beqz t1, fim_cpy
    addi a0, a0, 1
    addi a1, a1, 1
    j loop_cpy
fim_cpy:
    mv a0, t0
    ret