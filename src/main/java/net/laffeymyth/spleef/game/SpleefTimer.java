package net.laffeymyth.spleef.game;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class SpleefTimer {
    private int time = 0;
    private BukkitTask bukkitTask;
    private final Plugin plugin;

    public SpleefTimer(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

        }, 0L, 20L);
    }

    public void end() {
        bukkitTask.cancel();
    }
}
