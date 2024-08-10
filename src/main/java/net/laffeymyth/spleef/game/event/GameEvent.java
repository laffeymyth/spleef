package net.laffeymyth.spleef.game.event;

public interface GameEvent {
    default void start() {
    }

    default void onTick(int leftTime) {
    }

    default void onEndGame() {
    }
}
