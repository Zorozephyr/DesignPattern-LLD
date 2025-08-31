package org.example.tictactoelld.model;

import java.util.HashSet;
import java.util.Set;

public class Board {
    int size;
    PlayingPiece[][] playingPieces;
    Set<String> notFreeCells;
    public Board(int size) {
        this.size = size;
        this.playingPieces = new PlayingPiece[size][size];
        this.notFreeCells = new HashSet<>();
    }

    public int getSize() {
        return size;
    }

    public boolean addPiece(int r, int c, PlayingPiece playingPiece){
        if(notFreeCells.contains(r + "," + c)){
            return false;
        }
        else{
            this.playingPieces[r][c]=playingPiece;
            notFreeCells.add(r + "," + c);
            return true;
        }
    }

    public Set<String> getNotFreeCells() {
        return notFreeCells;
    }

    public void printBoard(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(notFreeCells.contains(i + "," + j)){
                    System.out.print(playingPieces[i][j].pieceType.toString()+"|");
                }
                else{
                    System.out.print(" |");
                }
            }
            System.out.println();
        }
    }

    public boolean verifyWinner(int row, int col, PlayingPiece playingPiece) {
        if (playingPiece == null || playingPiece.pieceType == null) {
            return false;
        }

        PieceType checker = playingPiece.pieceType;

        return checkRow(row, checker) ||
                checkColumn(col, checker) ||
                checkMainDiagonal(row, col, checker) ||
                checkAntiDiagonal(row, col, checker);
    }

    private boolean checkRow(int row, PieceType checker) {
        for (int i = 0; i < size; i++) {
            if (playingPieces[row][i] == null ||
                    playingPieces[row][i].pieceType == null ||
                    !playingPieces[row][i].pieceType.equals(checker)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int col, PieceType checker) {
        for (int i = 0; i < size; i++) {
            if (playingPieces[i][col] == null ||
                    playingPieces[i][col].pieceType == null ||
                    !playingPieces[i][col].pieceType.equals(checker)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMainDiagonal(int row, int col, PieceType checker) {
        if (row != col) return false;

        for (int i = 0; i < size; i++) {
            if (playingPieces[i][i] == null ||
                    playingPieces[i][i].pieceType == null ||
                    !playingPieces[i][i].pieceType.equals(checker)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAntiDiagonal(int row, int col, PieceType checker) {
        if (row + col != size - 1) return false;

        for (int i = 0; i < size; i++) {
            if (playingPieces[i][size - 1 - i] == null ||
                    playingPieces[i][size - 1 - i].pieceType == null ||
                    !playingPieces[i][size - 1 - i].pieceType.equals(checker)) {
                return false;
            }
        }
        return true;
    }

}
