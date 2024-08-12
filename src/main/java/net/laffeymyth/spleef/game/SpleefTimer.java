package net.laffeymyth.spleef.game;

import lombok.Getter;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.game.event.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class SpleefTimer {
    private final SpleefState spleefState;
    private int time = 0;
    private int eventTime = 0;
    private BukkitTask bukkitTask;
    private final Plugin plugin;
    private GameEvent currentEvent;
    private final Game game;

    public SpleefTimer(SpleefState spleefState, Plugin plugin, Game game) {
        this.spleefState = spleefState;
        this.plugin = plugin;
        this.game = game;
    }

    public void start() {
        currentEvent = spleefState.getGameEvents().pop();

        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (getLeftTime() != 0) {
                currentEvent.tick(eventTime, getLeftTime());
            }

            if (eventTime == currentEvent.timeToNextEvent()) {
                currentEvent.end();
                eventTime = 0;

                if (spleefState.getGameEvents().isEmpty()) {
                    game.nextState();
                } else {
                    currentEvent = spleefState.getGameEvents().pop();
                }

                currentEvent.start();
            } else {
                eventTime++;
            }

            time++;
        }, 0L, 20L);
    }

    public void end() {
        if (bukkitTask == null) {
            return;
        }

        if (bukkitTask.isCancelled()) {
            return;
        }

        bukkitTask.cancel();
    }

    public GameEvent getNextEvent() {
        return spleefState.getGameEvents().peek();
    }

    public int getLeftTime() {
        return currentEvent.timeToNextEvent() - eventTime;
    }
}
