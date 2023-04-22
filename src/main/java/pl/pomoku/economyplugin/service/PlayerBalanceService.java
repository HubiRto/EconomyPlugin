package pl.pomoku.economyplugin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.repository.PlayerBalanceRepository;

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
}
