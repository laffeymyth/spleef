package net.laffeymyth.spleef.api.board;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Map;

public interface Board {

    Component getTitle();

    void setTitle(Component title);

    Map<Integer, Component> getLineMap();

    BoardUpdater getUpdater();

    void setUpdater(BoardUpdater updater);

    void send(Player player);

}
