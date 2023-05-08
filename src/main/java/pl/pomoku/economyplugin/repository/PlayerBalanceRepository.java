package pl.pomoku.economyplugin.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pomoku.economyplugin.entity.PlayerBalance;

import java.util.List;

public interface PlayerBalanceRepository extends JpaRepository<PlayerBalance, Long> {
    PlayerBalance findByPlayerUUID(String playerUUID);
    PlayerBalance findByPlayerName(String playerName);

    @Query("SELECT p FROM PlayerBalance p ORDER BY p.balance DESC")
    List<PlayerBalance> findTop10ByOrderByBalanceDesc(Pageable pageable);
}
