package com.example.demo.Agendamento;

public enum EnumAgendamentoStatus {
    CANCELADO("CANCELADO"),
    CONFIRMADO("CONFIRMADO"),
    PENDENTE("PENDENTE"),
    CONCLUIDO("CONCLUIDO");

    private String enumAgendamentoStatus;


    EnumAgendamentoStatus(String enumAgendamentoStatus){
        this.enumAgendamentoStatus = enumAgendamentoStatus;
    }

    public String getEnumAgendamentoStatus(){
        return enumAgendamentoStatus;
    }
}
