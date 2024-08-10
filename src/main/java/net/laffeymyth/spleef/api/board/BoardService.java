package net.laffeymyth.spleef.api.board;

public interface BoardService {

    Board createBoard(BoardUpdater updater, long delay, long period);

}
