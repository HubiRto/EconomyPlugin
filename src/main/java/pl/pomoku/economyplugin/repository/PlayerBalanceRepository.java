package pl.pomoku.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pomoku.economyplugin.entity.PlayerBalance;

public interface PlayerBalanceRepository extends JpaRepository<PlayerBalance, Long> {

}
