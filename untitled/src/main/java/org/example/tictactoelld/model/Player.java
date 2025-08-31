package org.example.tictactoelld.model;

public class Player {
    PlayingPiece playingPiece;
    String name;

    public Player(String name, PlayingPiece playingPiece) {
        this.name = name;
        this.playingPiece = playingPiece;
    }

    public PlayingPiece getPlayingPiece() {
        return playingPiece;
    }

    public String getName() {
        return name;
    }

    @Override
    public String  toString() {
        return "Player{" +
                "playingPiece=" + playingPiece +
                ", name='" + name + '\'' +
                '}';
    }
}
