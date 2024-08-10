package net.laffeymyth.spleef.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MinPlayerUtil {
    public boolean isTwoThirds(int currentPlayers, int maxPlayers) { //есть ли две трети игроков
/*        if (currentPlayers == maxPlayers) {
            return true;
        }

        if (currentPlayers == 0) {
            return false;
        }

        return percent(currentPlayers, maxPlayers) >= 66;*/
        return true;
    }

    public static int percent(int a, int b) {
        return (b / a) * 100;
    }
}
