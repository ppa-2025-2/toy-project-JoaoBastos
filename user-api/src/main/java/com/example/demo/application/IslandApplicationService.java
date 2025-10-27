package com.example.demo.application;

import com.example.demo.controller.dto.NewIslandDTO;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Island;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IslandApplicationService {
    private final IslandRepository islandRepository;
    private final UserRepository userRepository;

    public IslandApplicationService(IslandRepository islandRepository, UserRepository userRepository) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
    }

    public Island create(@Valid NewIslandDTO newIslandDTO) {
        Island island = new Island();
        island.setDisposition(newIslandDTO.disposition());
        island.setDescription(newIslandDTO.description());

        return islandRepository.save(island);
    }

    @Transactional
    public void alocarWorkstationDisponivel(Integer userId) {
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        final List<Island> islands = islandRepository.findByWorkstationsUserIsNull();

        if (islands.isEmpty()) {
            throw new IllegalStateException("Nenhuma workstation disponível.");
        }

        Island islandEscolhida = Island.selecionarMelhorIsland(islands);
        islandEscolhida.alocarUsuario(user);

        islandRepository.save(islandEscolhida);
    }

    public List<Island> listarIslandsComDisponibilidade() {
        return islandRepository.findAllWithWorkstations();
    }
}