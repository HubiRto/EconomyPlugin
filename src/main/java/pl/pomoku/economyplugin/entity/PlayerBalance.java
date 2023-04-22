package pl.pomoku.economyplugin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBalance {
    @Id
    private int id;
    private double balance;
    private String playerUUID;
    private String playerName;
}
