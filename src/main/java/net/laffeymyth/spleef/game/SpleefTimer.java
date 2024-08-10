package net.laffeymyth.spleef.game;

import lombok.Getter;
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

    public SpleefTimer(SpleefState spleefState, Plugin plugin) {
        this.spleefState = spleefState;
        this.plugin = plugin;
    }

    public void start() {
        currentEvent = spleefState.getGameEvents().pop();

        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            currentEvent.tick(time);

            if (eventTime == currentEvent.time()) {
                currentEvent.end();
                eventTime = 0;
                currentEvent = spleefState.getGameEvents().pop();
            } else {
                eventTime++;
            }

            time++;
        }, 0L, 20L);
    }

    public void end() {
        bukkitTask.cancel();
    }
}
