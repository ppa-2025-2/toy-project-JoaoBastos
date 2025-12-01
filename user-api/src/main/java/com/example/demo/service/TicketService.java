package com.example.demo.service;


import com.example.demo.domain.Ticket;
import com.example.demo.domain.enums.TicketStatus;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository =  userRepository;
    }

    public Ticket create(Ticket ticket) {
        ticket.setStatus(TicketStatus.PENDENTE);

        return ticketRepository.save(ticket);
    }

    public Ticket technicalAssignService(Long ticket_id, Integer user) {
        var ticket = ticketRepository.findById(ticket_id)
                .orElseThrow(() -> new NoSuchElementException("Ticket não encontrado com id: " + ticket_id));

        var technicalUser = userRepository.findById(user)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com id: " + user));

        System.out.println("TIPO DO USUARIO " + technicalUser.getTypes());



        if (technicalUser.isNotTecnico()) {
            throw new IllegalArgumentException("Usuário não é técnico");
        }

        ticket.setAssignee(technicalUser);
        ticket.setStatus(TicketStatus.ANDAMENTO);

        return ticketRepository.saveAndFlush(ticket);
    }

    public Ticket updateStatus(long id, TicketStatus status, Integer user_id) {

        if (!Ticket.TERMINAL_STATES.contains(status)) {
            throw new IllegalArgumentException("Status inválido");
        }

        var user = userRepository.findById(user_id).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado")
        );

        System.out.println("TIPO DO USUARIO " + user.getTypes());


        if (user.isNotTecnico()) {
            throw new IllegalArgumentException("Usuário não é técnico");
        }

        var ticket = findById(id);

        ticket.setStatus(status);

        return ticketRepository.saveAndFlush(ticket);
    }


    public Ticket findById(long id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Ticket not found")
        );
    }
}