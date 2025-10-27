package com.example.demo.repository.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "islands")
public class Island extends BaseEntity {

    public enum Disposition {
        PAIR(2),
        TRIANGLE(3),
        SQUARE(4),
        RECTANGULAR(6),
        CIRCULAR(8);

        private final int placements;
        Disposition(int placements) { this.placements = placements; }
        public int getPlacements() { return placements; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Disposition disposition;

    @Column(name = "content", nullable = false) // Mapeia description para coluna content
    private String description;

    @OneToMany(mappedBy = "island", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Workstation> workstations = new HashSet<>();

    // ====== Construtores ======
    public Island() {
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public Island(Disposition disposition, String description) {
        this();
        this.disposition = disposition;
        this.description = description;
    }

    // ====== Lógica de domínio ======
    public static Island selecionarMelhorIsland(List<Island> islands) {
        if (islands == null || islands.isEmpty()) {
            throw new IllegalArgumentException("Lista de ilhas vazia");
        }

        return islands.stream()
                .sorted(Comparator.comparingLong(Island::contarWorkstationsOcupadas)
                        .thenComparing(Island::getId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma ilha válida encontrada"));
    }

    public long contarWorkstationsOcupadas() {
        return this.workstations.stream()
                .filter(Workstation::estaOcupada)
                .count();
    }

    public void alocarUsuario(User user) {
        Workstation workstationDisponivel = this.workstations.stream()
                .filter(Workstation::estaDisponivel)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhuma workstation disponível na ilha"));

        workstationDisponivel.alocarUsuario(user);
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public boolean temWorkstationDisponivel() {
        return this.workstations.stream()
                .anyMatch(Workstation::estaDisponivel);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Disposition getDisposition() { return disposition; }
    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public Set<Workstation> getWorkstations() { return workstations; }
    public void setWorkstations(Set<Workstation> workstations) {
        this.workstations = workstations;
        this.updatedAt = java.time.LocalDateTime.now();
    }
}