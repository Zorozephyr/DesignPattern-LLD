package org.example.tictactoelld;

import org.example.tictactoelld.model.*;

import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player("player1", new PlayingPieceX());
        Player player2 = new Player("player2", new PlayingPieceO());
        Player[] players = {player1, player2};
        Board gameBoard = new Board(3);
        Player winningPlayer = null;
        int currentPlayerIndex = 0;
        int totalCells = gameBoard.getSize() * gameBoard.getSize();

        while(winningPlayer == null && gameBoard.getNotFreeCells().size() < totalCells){
            gameBoard.printBoard();
            Player currentPlayer = players[currentPlayerIndex];
            System.out.println(currentPlayer.getName() + " please give row and column where you need to place your symbol");

            boolean playerMoved = false;
            while(!playerMoved){
                System.out.println("Type in Row number");
                int row = scanner.nextInt();
                System.out.println("Type in Column number");
                int col = scanner.nextInt();

                playerMoved = gameBoard.addPiece(row, col, currentPlayer.getPlayingPiece());
                if(!playerMoved) {
                    System.out.println("Invalid move! Cell is already occupied. Try again.");
                }

                if(playerMoved && gameBoard.verifyWinner(row, col, currentPlayer.getPlayingPiece())){
                    winningPlayer = currentPlayer;
                }
            }

            // Switch to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        }

        if(winningPlayer == null){
            System.out.println("It's a tie! No winner.");
        } else {
            System.out.println("Winner is " + winningPlayer.getName());
        }
    }
}
