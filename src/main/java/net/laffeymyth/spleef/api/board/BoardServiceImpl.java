package net.laffeymyth.spleef.api.board;

import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

                FastBoard fastBoard = playerScoreboards.get(player);

                if (fastBoard == null) {
                    fastBoard = new FastBoard(player);
                    playerScoreboards.put(player, fastBoard);
                }

                updaterTaskMap.computeIfAbsent(player, player1 -> Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    FastBoard fastBoard1 = playerScoreboards.get(player);

                    if (!player.isOnline()) {
                        return;
                    }

                    updater.onUpdate(this);

                    if (getTitle() != null) {
                        fastBoard1.updateTitle(getTitle());
                    }

                    if (getLineMap() != null && !getLineMap().isEmpty()) {
                        var lines = new ArrayList<>(getLineMap().values().stream().toList());
                        Collections.reverse(lines);
                        fastBoard1.updateLines(lines);
                    }
                }, delay, period));
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
