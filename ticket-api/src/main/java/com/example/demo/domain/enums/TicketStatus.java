package com.example.demo.domain.enums;

public enum TicketStatus {
    PENDENTE,
    ANDAMENTO,
    CONCLUIDO,
    CANCELADO;

    public static TicketStatus parse(String status) {
        if (status == null ) {
            return null;
        }
        return TicketStatus.valueOf(status.toUpperCase());
    }
}
