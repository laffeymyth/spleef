package net.laffeymyth.spleef.api.end;

import net.kyori.adventure.text.Component;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.State;
import org.bukkit.Bukkit;

public class EndState implements State {
    private final Game game;

    public EndState(Game game) {
        this.game = game;
    }

    @Override
    public void start() {
        if (game.getWinners().isEmpty()) {
            Bukkit.broadcast(Component.text("конец игры, победитель не определён!"));
            return;
        }

        if (game.getWinners().size() > 1) {
            throw new RuntimeException("пока что не может быть больше одного победителя"); //todo накодить))
         }

        game.getWinners().stream().findFirst().ifPresent(winner -> Bukkit.broadcast(Component.text("Игра оконочена, победил ").append(winner.displayName()).append(Component.text("!"))));
    }
}
