TIC TAC TOE - LOW LEVEL DESIGN

## Current Object Relationships

```
                    ┌─────────────┐
                    │  TicTacToe  │
                    │   (Main)    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │    Board    │
                    │  - size     │
                    │  - pieces[][] │
                    │  - notFreeCells │
                    └──────┬──────┘
                           │ contains
                    ┌──────▼──────┐
                    │PlayingPiece │
                    │ - pieceType │
                    └──────┬──────┘
                           │ uses
                    ┌──────▼──────┐
                    │  PieceType  │
                    │   (Enum)    │
                    │   X, O      │
                    └─────────────┘

    ┌─────────────┐         ┌─────────────┐
    │   Player    │         │PlayingPieceX│
    │ - name      │◆────────│   extends   │
    │ - piece     │         │PlayingPiece │
    └─────────────┘         └─────────────┘
                                   │
                            ┌──────▼──────┐
                            │PlayingPieceO│
                            │   extends   │
                            │PlayingPiece │
                            └─────────────┘
```

## Design Patterns Currently Used

1. **Inheritance**: PlayingPieceX and PlayingPieceO extend PlayingPiece
2. **Enum**: PieceType for type safety
3. **Composition**: Player has-a PlayingPiece, Board has-a PlayingPiece[][]

## Current Issues & Improvement Suggestions

### 1. **Violation of Single Responsibility Principle**
**Issue**: Board class handles multiple responsibilities:
- Board state management
- Game logic (winner verification)
- Display logic (printBoard)

**Solution**: 
```java
// Separate concerns
class Board { /* only board state */ }
class GameEngine { /* game logic & winner verification */ }
class DisplayManager { /* UI/printing logic */ }
```

### 2. **Missing Game State Management**
**Issue**: Game state is scattered across TicTacToe main method

**Solution**: Create a Game class to manage state:
```java
class Game {
    - gameStatus (ONGOING, WON, DRAW)
    - currentPlayer
    - winner
    + playMove()
    + isGameOver()
    + getGameResult()
}
```

### 3. **Poor Extensibility**
**Issue**: Hard to extend for different board sizes, player counts, or game rules

**Solution**: Use Strategy Pattern for game rules:
```java
interface WinningStrategy {
    boolean checkWin(Board board, int row, int col, PieceType piece);
}
```

### 4. **Tight Coupling**
**Issue**: TicTacToe main class directly manages all game logic

**Solution**: Use Dependency Injection and interfaces:
```java
interface GameController {
    void startGame();
}
interface InputHandler {
    Move getPlayerMove();
}
```

### 5. **Missing Error Handling**
**Issue**: No validation for invalid inputs or edge cases

**Solution**: Add proper validation and exception handling

### 6. **Hard-coded Values**
**Issue**: Board size (3), player count (2) are hard-coded

**Solution**: Make configurable through constructor parameters

## Improved LLD Architecture

```
┌─────────────────┐    ┌─────────────────┐
│  GameController │────│  InputHandler   │
│                 │    │  (Interface)    │
└─────────┬───────┘    └─────────────────┘
          │
    ┌─────▼─────┐
    │   Game    │
    │ - status  │
    │ - players │
    │ - board   │
    └─────┬─────┘
          │
    ┌─────▼─────┐      ┌─────────────────┐
    │   Board   │──────│ WinningStrategy │
    │ - cells   │      │  (Interface)    │
    │ - size    │      └─────────────────┘
    └───────────┘
          │
    ┌─────▼─────┐
    │  Player   │
    │ - id      │
    │ - piece   │
    └───────────┘
```

## Recommended Design Patterns to Implement

1. **Strategy Pattern**: For different winning conditions
2. **Factory Pattern**: For creating different game types
3. **Observer Pattern**: For UI updates and game events
4. **Command Pattern**: For move validation and undo functionality
5. **State Pattern**: For game state management

## Benefits of Improved Design

- **Extensible**: Easy to add new game modes, board sizes, or piece types
- **Testable**: Each class has single responsibility
- **Maintainable**: Clear separation of concerns
- **Reusable**: Components can be reused for other grid-based games
- **Scalable**: Can support multiplayer, AI players, or network play
