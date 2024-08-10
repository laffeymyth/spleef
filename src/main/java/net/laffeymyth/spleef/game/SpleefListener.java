package net.laffeymyth.spleef.game;

import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.GamerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpleefListener implements Listener {
    private final Game game;
    private final int loseLevel;

    public SpleefListener(Game game, int loseLevel) {
        this.game = game;
        this.loseLevel = loseLevel;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!(event.getTo().getY() < loseLevel)) {
            return;
        }

        Player player = event.getPlayer();

        if (game.isSpectator(player)) {
            return;
        }

        game.putPlayer(player, GamerState.SPECTATOR);

        if (gameOver()) {
            game.nextState();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        game.putPlayer(event.getPlayer(), GamerState.SPECTATOR);

        if (gameOver()) {
            game.nextState();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        game.putPlayer(event.getPlayer(), GamerState.SPECTATOR);
    }

    public boolean gameOver() {
        return game.getGamers().size() <= 1;
    }
}
