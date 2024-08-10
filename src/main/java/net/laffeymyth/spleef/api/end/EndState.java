package net.laffeymyth.spleef.api.end;

import net.kyori.adventure.text.Component;
import net.laffeymyth.spleef.api.State;
import org.bukkit.Bukkit;

public class EndState implements State {
    @Override
    public void start() {
        Bukkit.broadcast(Component.text("конец игры!"));
    }

    @Override
    public void end() {

    }
}
