package com.example.demo.domain;

import com.example.demo.controller.dto.NewIslandDTO;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class IslandDomainService {

    public Island createIsland(NewIslandDTO newIslandDTO) {
        Island island = new Island();

        island.setDisposition(newIslandDTO.disposition());

        return island;
    }

    public Island selecionarIslandParaAlocacao(List<Island> islands) {

        Island freeIsland = islands.getFirst();

        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = islands.stream()
                    .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                    .findFirst();

            if (possibleIsland.isPresent()) {
                freeIsland = possibleIsland.get();
                break;
            }
        }

        return freeIsland;
    }

    public void alocarUsuarioNaIsland(Island island, User user) {
        island.getWorkstations().stream()
                .filter(ws -> ws.getUser() == null)
                .findFirst()
                .ifPresent(ws -> ws.setUser(user));
    }
}
