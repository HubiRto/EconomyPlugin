package pl.pomoku.economyplugin.service;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Service;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.repository.PlayerBalanceRepository;

import java.util.UUID;

@Service("playerBalanceService")
@RequiredArgsConstructor
public class PlayerBalanceService {
    private final PlayerBalanceRepository playerBalanceRepository;

    public void addPlayerBalance(PlayerBalance playerBalance) {
        playerBalanceRepository.save(playerBalance);
    }

    public void updatePlayerBalance(PlayerBalance playerBalance) {
        addPlayerBalance(playerBalance);
    }

    public PlayerBalance findPlayerBalanceByPlayerUUID(UUID uuid) {
        return playerBalanceRepository.findByPlayerUUID(uuid.toString());
    }
    public PlayerBalance findPlayerBalanceByPlayer(Player player){
        return findPlayerBalanceByPlayerUUID(player.getUniqueId());
    }
}
