package net.laffeymyth.spleef.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MinPlayerUtil {
    public boolean isTwoThirds(int currentPlayers, int maxPlayers) {
        if (currentPlayers == 0) {
            return false;
        }

        if (currentPlayers == maxPlayers) {
            return true;
        }

        double twoThirds = (2.0 / 3.0) * maxPlayers;
        return currentPlayers >= twoThirds;
    }
}
