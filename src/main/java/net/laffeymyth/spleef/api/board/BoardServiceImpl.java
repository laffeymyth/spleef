package net.laffeymyth.spleef.api.board;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class BoardServiceImpl implements BoardService {
    private final Map<Player, FastBoard> playerScoreboards = new HashMap<>();
    private final Map<Player, BukkitTask> updaterTaskMap = new HashMap<>();
    private final Plugin plugin;
    private Listener listener;

    public BoardServiceImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Board createBoard(BoardUpdater updater, long delay, long period) {
        return new BoardImpl(updater) {
            @Override
            public void send(Player player) {
                if (player == null) {
                    return;
                }

                if (updater == null) {
                    return;
                }

                remove(player);
                FastBoard fastBoard = playerScoreboards.computeIfAbsent(player, FastBoard::new);

                updaterTaskMap.computeIfAbsent(player, player1 -> Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    if (!player.isOnline()) {
                        return;
                    }

                    updater.onUpdate(this);

                    if (getTitle() != null) {
                        fastBoard.updateTitle(getTitle());
                    }

                    if (getLineMap() != null && !getLineMap().isEmpty()) {
                        List<Component> components = getLineMap().values().stream().collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                            Collections.reverse(list);
                            return list;
                        }));

                        fastBoard.updateLines(components);
                    }
                }, delay, period));
            }

            @Override
            public void remove(Player player) {
                FastBoard fastBoard = playerScoreboards.remove(player);

                if (fastBoard != null) {
                    fastBoard.delete();
                }

                BukkitTask bukkitTask = updaterTaskMap.remove(player);

                if (bukkitTask != null && !bukkitTask.isCancelled()) {
                    bukkitTask.cancel();
                }
            }
        };
    }

    public void enable() {
        listener = new BoardListener(playerScoreboards, updaterTaskMap);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void disable() {
        updaterTaskMap.values().forEach(bukkitTask -> {
            if (!bukkitTask.isCancelled()) {
                bukkitTask.cancel();
            }
        });

        playerScoreboards.values().forEach(componentBaseBoard -> {
            if (!componentBaseBoard.isDeleted()) {
                componentBaseBoard.delete();
            }
        });

        playerScoreboards.clear();
        updaterTaskMap.clear();
        HandlerList.unregisterAll(listener);
    }
}
