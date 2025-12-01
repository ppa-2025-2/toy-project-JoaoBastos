package com.example.demo.controller;

import com.example.demo.controller.dto.OnboardingTicketRequest;
import com.example.demo.controller.dto.TicketCreateDTO;
import com.example.demo.controller.dto.TicketUpdateDTO;
import com.example.demo.controller.dto.UserIdDTO;
import com.example.demo.domain.Ticket;
import com.example.demo.domain.enums.TicketStatus;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.User;
import com.example.demo.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {
    private final TicketService service;
    private final TicketMapper mapper;
    private final TicketRepository repository;

    public TicketController(TicketService ticketService, TicketMapper mapper, TicketRepository repository) {
        this.service = ticketService;
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostMapping("/onBoarding")
    public ResponseEntity<Ticket> onBoarding(@RequestBody OnboardingTicketRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        user.setEmail(request.getEmail());

        if (request.getName() != null) {
            Profile profile = new Profile();
            profile.setName(request.getName());
            user.setProfile(profile);
        }

        user.setHandle(request.getHandle());

        var response = service.createUserOnboardingTickets(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Ticket> create(@RequestBody TicketCreateDTO ticket) {
        var ticketMapped = mapper.toEntity(ticket);

        var ticketToCreate = service.create(ticketMapped);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketToCreate);
    }

    @PatchMapping("/get-ticket/{ticket_id}")
    public ResponseEntity<Ticket> assignService(@PathVariable Long ticket_id, @RequestBody UserIdDTO user_id) {
        var ticketToGet = service.technicalAssignService(ticket_id, user_id.user_id());

        return ResponseEntity.status(HttpStatus.OK).body(ticketToGet);
    }

    @PatchMapping("/update/{ticket_id}")
    public ResponseEntity<Ticket> update(@PathVariable Long ticket_id, @RequestBody TicketUpdateDTO dto) {
        var ticketToUpdate = service.updateStatus(ticket_id, TicketStatus.parse(dto.status()), dto.user_id());
        return ResponseEntity.ok(ticketToUpdate);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        var tickets = repository.findAll();

        return ResponseEntity.ok(tickets);
    }
}
