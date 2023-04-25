package pl.pomoku.economyplugin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double balance;
    private String playerUUID;
    private String playerName;
}
