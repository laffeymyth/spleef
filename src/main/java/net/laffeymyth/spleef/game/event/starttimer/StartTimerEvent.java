package net.laffeymyth.spleef.game.event.starttimer;

import net.kyori.adventure.text.Component;
import net.laffeymyth.spleef.game.event.GameEvent;
import org.bukkit.Bukkit;

public class StartTimerEvent implements GameEvent {
    @Override
    public void tick(int leftTime) {
        Bukkit.broadcast(Component.text(leftTime));
    }

    @Override
    public int time() {
        return 10;
    }
}
