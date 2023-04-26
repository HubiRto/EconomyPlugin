package pl.pomoku.economyplugin.service;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Service;
import pl.pomoku.economyplugin.entity.TimePlayer;
import pl.pomoku.economyplugin.repository.TimePlayerRepository;

import java.util.List;
import java.util.UUID;

@Service("timePlayerService")
@RequiredArgsConstructor
public class TimePlayerService {
    private final TimePlayerRepository timePlayerRepository;

    public TimePlayer findByPlayerUUID(String stringUUID) {
        return timePlayerRepository.findByPlayerUUID(stringUUID);
    }

    public TimePlayer findByPlayerUUID(UUID uuid) {
        return findByPlayerUUID(uuid.toString());
    }

    public TimePlayer findByPlayer(Player player) {
        return findByPlayerUUID(player.getUniqueId());
    }

    public void addTimePlayer(TimePlayer timePlayer) {
        timePlayerRepository.save(timePlayer);
    }

    public void updateAll(List<TimePlayer> timePlayerList) {
        timePlayerRepository.saveAll(timePlayerList);
    }

    public List<TimePlayer> findAll() {
        return timePlayerRepository.findAll();
    }
}
