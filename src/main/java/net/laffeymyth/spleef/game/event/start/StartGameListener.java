package net.laffeymyth.spleef.game.event.start;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class StartGameListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.SNOW_BLOCK)) {
            event.setDropItems(false);
            return;
        }

        event.setCancelled(true);
    }
}
