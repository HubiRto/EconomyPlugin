package pl.pomoku.economyplugin.service;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.repository.PlayerBalanceRepository;

import java.util.List;
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

    public List<PlayerBalance> findTopPlayersByBalance(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("balance").descending());
        Page<PlayerBalance> page = playerBalanceRepository.findAll(pageable);
        return page.getContent();
    }
}
