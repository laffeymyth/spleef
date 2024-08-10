package net.laffeymyth.spleef.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CancelBlockBreakListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }
}
