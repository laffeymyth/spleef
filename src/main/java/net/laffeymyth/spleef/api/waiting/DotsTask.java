package net.laffeymyth.spleef.api.waiting;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class DotsTask {
    private final Plugin plugin;
    private BukkitTask bukkitTask;
    @Getter
    private int dotCount = 0;

    public DotsTask(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (dotCount == 3) {
                dotCount = 0;
                return;
            }

            dotCount++;
        }, 0L, 20L);
    }

    public void cancel() {
        if (bukkitTask == null) {
            return;
        }

        if (bukkitTask.isCancelled()) {
            return;
        }

        bukkitTask.cancel();
    }
}
