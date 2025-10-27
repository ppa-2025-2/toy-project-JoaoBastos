package com.example.demo.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workstations")
public class Workstation extends BaseEntity {

    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private Island island;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String specs;

    public boolean estaDisponivel() {
        return this.user == null;
    }

    public boolean estaOcupada() {
        return !estaDisponivel();
    }

    public void alocarUsuario(User user) {
        if (!estaDisponivel()) {
            throw new IllegalStateException("Workstation já está ocupada");
        }
        this.user = user;
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void desalocarUsuario() {
        this.user = null;
        this.setUpdatedAt(LocalDateTime.now());
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Island getIsland() { return island; }
    public void setIsland(Island island) { this.island = island; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }
}