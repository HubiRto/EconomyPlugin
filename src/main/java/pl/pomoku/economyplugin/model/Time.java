package pl.pomoku.economyplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Time {
    private long lastTimeAdd;
    private int countAddMoney;
}
