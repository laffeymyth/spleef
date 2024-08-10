package net.laffeymyth.spleef.game;

import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.State;
import net.laffeymyth.spleef.api.board.Board;
import net.laffeymyth.spleef.api.board.BoardService;
import net.laffeymyth.spleef.api.board.BoardServiceImpl;
import net.laffeymyth.spleef.game.event.GameEvent;
import net.laffeymyth.spleef.game.event.StartGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class SpleefState implements State {
    private final Map<Integer, GameEvent> gameEventMap = new HashMap<>();
    private final Game game;
    private final Location startLocation;
    private final Plugin plugin;
    private final int loseLevel;
    private final Board gameBoard;
    private final SpleefTimer spleefTimer;
    private Listener spleefListener;
    private Listener cancelBlockListener;

    public SpleefState(Game game, Location startLocation, Plugin plugin, int loseLevel, BoardService boardService) {
        this.game = game;
        this.startLocation = startLocation;
        this.plugin = plugin;
        this.loseLevel = loseLevel;
        this.spleefTimer = new SpleefTimer(plugin);
        this.gameBoard = boardService.createBoard(new SpleefBoardUpdater(game, game.getLang(), spleefTimer), 0L, 1L);
    }

    @Override
    public void start() {
        cancelBlockListener = new CancelBlockBreakListener();
        Bukkit.getPluginManager().registerEvents(cancelBlockListener, plugin);
        gameEventMap.put(10, new StartGameEvent(game, plugin, cancelBlockListener));

        game.getGamers().forEach(player -> {
            game.normalize(player);
            gameBoard.send(player);

            player.teleport(startLocation);
            giveShovel(player);
        });

        spleefListener = new SpleefListener(game, loseLevel);
        Bukkit.getPluginManager().registerEvents(spleefListener, plugin);
    }

    public void giveShovel(Player player) {
        ItemStack itemStack = new ItemStack(Material.IRON_SHOVEL);

        itemStack.addEnchantment(Enchantment.DIG_SPEED, 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack);
    }

    @Override
    public void end() {
        HandlerList.unregisterAll(spleefListener);
        HandlerList.unregisterAll(cancelBlockListener);
    }
}
