package net.laffeymyth.spleef.api.board;

@FunctionalInterface
public interface BoardUpdater {

    void onUpdate(Board board);
}
