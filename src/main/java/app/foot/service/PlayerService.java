package app.foot.service;

import app.foot.exception.NotFoundException;
import app.foot.model.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    public List<Player> getPlayers() {
        return repository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        return repository.saveAll(toCreate.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toUnmodifiableList())).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Player> updatePlayer(List<Player> toUpdate) {
        List<PlayerEntity> toSave = checkExistingPlayer(toUpdate);
        return repository.saveAll(toSave).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toUnmodifiableList());
    }

    private List<PlayerEntity> checkExistingPlayer(List<Player> players) {
        List<PlayerEntity> playerEntities = new ArrayList<>();
        for (Player player : players) {
            Optional<PlayerEntity> playerEntity = repository.findById(player.getId());
            if (playerEntity.isPresent()) {
                playerEntity.get().setName(player.getName());
                playerEntity.get().setGuardian(player.getIsGuardian());
                playerEntities.add(playerEntity.get());
            }
            throw new NotFoundException("player." + player.getId() + " not found.");
        }
        return playerEntities;
    }
}
