package net.laffeymyth.spleef.api.board;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
abstract class BoardImpl implements Board {
    private final Map<Integer, Component> lineMap = new HashMap<>();
    private BoardUpdater updater;
    private Component title;

    public BoardImpl(BoardUpdater updater) {
        this.updater = updater;
    }
}
