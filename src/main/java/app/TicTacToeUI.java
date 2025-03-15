package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class TicTacToeUI {
    private Stage window;
    private Scene scene;
    private Game game;
    private Button[][] buttons;
    private Label turnText;
    private Label pointsX;
    private Label pointsO;
    private Label numberOfGames;
    private int gameCount;
    private double windowWidth;
    private double windowHeight;

    public TicTacToeUI(Stage window) {
        this.window = window;
        this.game = new Game();
        game.setWhoStarted(game.getTurn());
        this.buttons = new Button[3][3];
        this.gameCount = 1;
        this.windowWidth = 700;
        this.windowHeight = 400;
    }

    public void start() {
        window.setTitle("Tic-Tac-Toe");

        window.widthProperty().addListener((obs, oldVal, newVal) -> windowWidth = newVal.doubleValue());
        window.heightProperty().addListener((obs, oldVal, newVal) -> windowHeight = newVal.doubleValue());

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        turnText = new Label("Turn: " + game.getTurn());
        turnText.setFont(Font.font("Monospaced", 30));

        layout.setTop(createTopLayout());

        GridPane gameLayout = createGameLayout();
        layout.setCenter(gameLayout);

        scene = new Scene(layout, windowWidth, windowHeight);
        window.setScene(scene);
        window.show();
    }

    private VBox createPointsLayout() {
        VBox pointsLayout = new VBox();
        Label points = new Label("Points");
        points.setFont(Font.font("Monospaced", 30));
        pointsX = new Label("X:" + game.getPoints()[0]);
        pointsO = new Label("O:" + game.getPoints()[1]);
        pointsX.setFont(Font.font("Monospaced", 21));
        pointsO.setFont(Font.font("Monospaced", 21));

        HBox pointsLayout2 = new HBox();
        numberOfGames = new Label("Game: " + gameCount);
        numberOfGames.setFont(Font.font("Monospaced", 30));
        pointsLayout2.setSpacing(30);
        pointsLayout2.getChildren().addAll(pointsX, pointsO);
        pointsLayout.getChildren().addAll(points, pointsLayout2);
        pointsLayout.setAlignment(Pos.TOP_CENTER);
        return pointsLayout;
    }

    private HBox createTopLayout() {
        VBox pointsLayout = createPointsLayout();
        HBox topLayout = new HBox();
        topLayout.setAlignment(Pos.CENTER);
        topLayout.setSpacing(100);
        numberOfGames = new Label("Game: " + gameCount);
        numberOfGames.setFont(Font.font("Monospaced", 30));
        topLayout.getChildren().addAll(turnText, numberOfGames, pointsLayout);
        return topLayout;
    }

    private GridPane createGameLayout() {
        GridPane gameLayout = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button(" ");
                buttons[i][j].setFont(Font.font("Monospaced", 50));
                gameLayout.add(buttons[i][j], j, i);
                final int row = i;
                final int col = j;
                buttons[row][col].setOnAction(event -> handleButtonClick(row, col));
            }
        }
        gameLayout.setAlignment(Pos.CENTER);
        return gameLayout;
    }

    private void handleButtonClick(int row, int col) {
        if (buttons[row][col].getText().equals(" ")) {
            buttons[row][col].setText(game.getTurn());
            if (game.checkWin(buttons, game.getTurn())) {
                setButtonsDisabled(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    game.updatePoints(game.getTurn());
                    updatePointsDisplay();
                    showWinner("Winner: " + game.getTurn());
                    setButtonsDisabled(false);
                });
                pause.play();

            } else if (game.getMoveCount() == 8) {
                setButtonsDisabled(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    numberOfGames.setText("Game: " + ++gameCount);
                    showWinner("Draw");
                    setButtonsDisabled(false);
                });
                pause.play();

            } else {
                game.switchTurn();
                turnText.setText("Turn: " + game.getTurn());
            }
        }
    }

    private void showWinner(String winner) {
        BorderPane winnerView = winnerView(winner);
        window.setScene(new Scene(winnerView));
        resetGame();
    }

    private void resetGame() {
        resetBoard();
        game.reset();
        turnText.setText("Turn: " + game.getTurn());
    }

    private void updatePointsDisplay() {
        pointsX.setText("X: " + game.getPoints()[0]);
        pointsO.setText("O: " + game.getPoints()[1]);
        numberOfGames.setText("Game: " + ++gameCount);
    }

    private BorderPane winnerView(String text) {
        Label winner = new Label(text);
        winner.setFont(Font.font("Monospaced", 60));

        Label gameOver = new Label("Game Over!");
        gameOver.setFont(Font.font("Monospaced", 30));

        Button playAgain = new Button("Play Again");

        playAgain(playAgain);

        BorderPane winnerScene = new BorderPane();
        winnerScene.setPrefSize(windowWidth, windowHeight);
        winnerScene.setPadding(new Insets(10, 10, 10, 10));

        Button exit = new Button("Exit");
        exit.setOnAction(event -> {
            window.close();
        });

        Label pointsLabel = new Label("X:" + game.getPoints()[0] + "  O:" + game.getPoints()[1]);
        pointsLabel.setFont(Font.font("Monospaced", 30));
        HBox pointsLabelContainer = new HBox(pointsLabel);
        pointsLabelContainer.setAlignment(Pos.CENTER);

        HBox playExit = new HBox();
        playExit.getChildren().addAll(playAgain, exit);
        playExit.setSpacing(20);
        playExit.setAlignment(Pos.CENTER);

        GridPane resultAndRetryOrExit = new GridPane();
        resultAndRetryOrExit.setAlignment(Pos.CENTER);
        resultAndRetryOrExit.setVgap(40);
        resultAndRetryOrExit.add(winner, 0, 0);
        resultAndRetryOrExit.add(pointsLabelContainer, 0, 1);
        resultAndRetryOrExit.add(playExit, 0, 2);

        winnerScene.setTop(gameOver);
        winnerScene.setCenter(resultAndRetryOrExit);

        return winnerScene;
    }

    private void playAgain(Button playAgain) {
        playAgain.setOnAction(event -> {
            resetBoard();
            window.setScene(scene);
        });
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
            }
        }
    }

    private void setButtonsDisabled(boolean disable) {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                buttons[row][col].setDisable(disable);
            }
        }
    }
}