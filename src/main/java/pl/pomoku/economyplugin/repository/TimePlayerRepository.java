package pl.pomoku.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pomoku.economyplugin.entity.TimePlayer;

import java.util.List;

public interface TimePlayerRepository extends JpaRepository<TimePlayer, Long> {
    TimePlayer findByPlayerUUID(String playerUUID);
}
