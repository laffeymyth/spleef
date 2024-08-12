package net.laffeymyth.spleef.api.waiting;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.util.ComponentResolver;
import net.laffeymyth.spleef.api.Game;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

class WaitingTimer {
    private static final Sound START_SOUND = Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MUSIC, 1f, 1f);
    private final Game game;
    private final Plugin plugin;
    private final ComponentLocalizationService lang;
    @Getter
    private int time;
    private BukkitTask bukkitTask;


    public WaitingTimer(Game game, Plugin plugin, ComponentLocalizationService lang) {
        this.game = game;
        this.plugin = plugin;
        this.lang = lang;
    }

    public void start() {
        time = game.getStartTime();

        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                game.getGamers().forEach(player -> {
                    player.sendActionBar(lang.getMessage("start_timer_action_bar",
                            "ru",
                            ComponentResolver.tag("time", Component.text(time).append(Component.space())
                                    .append(lang.getWord("TIME_SECOND_1", time, "ru"))))
                    );

                    player.playSound(START_SOUND, Sound.Emitter.self());

                    //player.setExp((float) game.getStartTime() / time * 100); TODO: накодить)))
                    player.setLevel(time);
                });

                if (time == 1) {
                    game.nextState();
                    cancel();
                    game.getGamers().forEach(player -> player.setLevel(0));
                    return;
                }

                time--;
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public void cancel() {
        if (isStated()) {
            bukkitTask.cancel();
            game.getGamers().forEach(player -> player.sendMessage(lang.getMessage("start_timer_cancel_message", "ru")));
        }
    }

    public boolean isStated() {
        if (bukkitTask == null) {
            return false;
        }

        return !bukkitTask.isCancelled();
    }
}
