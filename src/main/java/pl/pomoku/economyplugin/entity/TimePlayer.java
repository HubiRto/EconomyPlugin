package pl.pomoku.economyplugin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String playerUUID;
    private boolean tookAllMoney;
    private int collectCounter;
    private long lastTimeCollect;
}
