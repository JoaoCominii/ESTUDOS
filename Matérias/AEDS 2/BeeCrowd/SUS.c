#include <stdio.h>

#define MAX_PACIENTES 25

typedef struct{
    int hora;
    int minuto;
    int tempoCritico;
} Paciente;



int main()
{
    int N;
    while(scanf("%d", &N) != EOF)
    {
        Paciente pacientes[MAX_PACIENTES];
        int criticos = 0;
        int horaAtendimento = 7; // Comeca as 7 da manha o atendimento
        int minutoAtendimento = 0;

        // Leitura dos pacientes
        for(int i = 0; i < N; i++)
        {
            scanf("%i %i %i", &pacientes[i].hora, &pacientes[i].minuto, &pacientes[i].tempoCritico);
        }

        // Simulacao atendimento
        for(int i = 0; i < N; i++)
        {
            int chegadaMinutos = (pacientes[i].hora * 60) + pacientes[i].minuto;
            int limiteCritico = chegadaMinutos + pacientes[i].tempoCritico;
            int atendimentoMinutos = (horaAtendimento * 60) + minutoAtendimento;

            // Ajuste horario de atendimento
            if(atendimentoMinutos < chegadaMinutos)
            {
                atendimentoMinutos = chegadaMinutos;
            }

            // Atendimento ocorre sempre na proxima meia hora ou uma hora
            while(atendimentoMinutos % 30 != 0)
            {
                atendimentoMinutos++;
            }

            // Verificacao se paciente atingiu condicao critica antes do atendimento
            if(limiteCritico < atendimentoMinutos)
            {
                criticos++;
            }

            // Atualiza proximo horario de atendimento
            horaAtendimento = atendimentoMinutos / 60;
            minutoAtendimento = minutoAtendimento % 60;
        }
        // Saida
        printf("%d\n", criticos);
    }


}
