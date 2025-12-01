package com.example.demo.domain;

import com.example.demo.domain.enums.TicketStatus;
import com.example.demo.repository.entity.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "ticket")
public class Ticket {

    public static Set<TicketStatus> TERMINAL_STATES = EnumSet.of(TicketStatus.CONCLUIDO, TicketStatus.CANCELADO);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String object;
    private String action;
    private String details;
    private String local;
    private TicketStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}