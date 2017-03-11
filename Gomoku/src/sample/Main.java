package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

/*
* Gomoku is a game played in the go board and go pieces, also named "Five in a Row"
* The rule is that two players alternatively place black or white pieces.
* The winner is the one who get an unbroken row of five stones horizontally, vertically, or diagonally.
* More description about the game can be found in https://en.wikipedia.org/wiki/Gomoku
* */

public class Main extends Application {

    private boolean gameOver = false;
    private int nFilled = 0;
    private char whoseTurn = 'X'; // 'X' or 'O'
    private Cell[][] cell =  new Cell[20][20];
    private Label statusLabel = new Label("X's turn to play");

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                pane.add(cell[i][j] = new Cell(i,j), j, i);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setBottom(statusLabel);

        Scene scene = new Scene(borderPane, 450.0f, 450.0f);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean isFull() {
        return nFilled >= 400; // > should never happen
    }

    public boolean hasWon(char tkn, int posX, int posY) {
        // counter is used for counting the number of pieces in a line
        if(nFilled<9) return false;
        int counter = 1;
        int i;
        int j;
        for (j = posY-1; j >= posY-4 && j>-1; j--)
        {
            if(cell[posX][j].getToken() == tkn)
                counter++;
            else
                break;
        }
        for (j = posY+1; j <= posY+4 && j<20; j++)
        {
            if(cell[posX][j].getToken() == tkn)
                counter++;
            else
                break;
        }
        if(counter >= 5)
            return true;

        // vertical check
        counter = 1;
        for (i = posX-1; i >= posX-4 && i>-1; i--)
        {
            if(cell[i][posY].getToken() == tkn)
                counter++;
            else
                break;
        }
        for (i = posX+1; i <= posX+4 && i<20; i++)
        {
            if(cell[i][posY].getToken() == tkn)
                counter++;
            else
                break;
        }
        if(counter >= 5)
            return true;

        // diagonal check towards northeast
        counter = 1;
        for (i = posX-1, j = posY+1; i >= posX-4 && i>-1 && j <= posY+4 && j<20; )
        {
            if(cell[i][j].getToken() == tkn)
                counter++;
            else
                break;
            i--;
            j++;
        }
        // towards southwest
        for (i = posX+1, j = posY-1; i <= posX+4 && i <20 && j >= posY-4 && j>-1; )
        {
            if(cell[i][j].getToken() == tkn)
                counter++;
            else
                break;
            i++;
            j--;
        }
        if(counter >= 5)
            return true;


        // diagonal check towards northwest
        counter = 1;
        for (i = posX-1, j = posY-1; i >= posX-4 && i>-1 && j >= posY-4 && j>-1; )
        {
            if(cell[i][j].getToken() == tkn)
                counter++;
            else
                break;
            i--;
            j--;
        }
        for (i = posX+1, j = posY+1; i <= posX+4 && i<20 && j <= posY+4 && j<20; )
        {
            if(cell[i][j].getToken() == tkn)
                counter++;
            else
                break;
            i++;
            j++;
        }
        if(counter >= 5)
            return true;

        return false;
    }

    public class Cell extends Pane {
        private int indX;
        private int indY;
        private boolean filled = false;
        private char token = ' ';   // one of blank, X, or O

        public Cell(int i, int j) {
            this.indX = i;
            this.indY = j;
            setStyle("-fx-border-color: black");
            setPrefSize(20.0f, 20.0f);
            setOnMouseClicked(e -> handleMouseClick(i,j));
        }

        public char getToken() {
            return token;
        }

        public void setFilled() { filled = true; }

        public boolean drawX() {
            if(filled == true){

                return false;
            }

            double w = getWidth(), h = getHeight();
            Line line1 = new Line(2.0f, 2.0f, w - 2.0f, h - 2.0f);
            Line line2 = new Line(2.0f, h - 2.0f, w - 2.0f, 2.0f);
            getChildren().addAll(line1, line2);
            return true;
        }

        public boolean drawO() {
            if(filled == true){
                return false;
            }

            double w = getWidth(), h = getHeight();
            Ellipse ellipse = new Ellipse(w/2, h/2, w/2 - 2.0f, h/2 - 2.0f);
            ellipse.setStroke(Color.BLACK);
            ellipse.setFill(Color.WHITE);
            getChildren().add(ellipse);
            return true;
        }

        public boolean setToken(char c) {
            boolean success;
            if (c == 'X')
                success = drawX();
            else
                success = drawO();

            if(success == true) {
                setFilled();
                token = c;
                nFilled++;
                return true;
            }
            return false;
        }

        private void handleMouseClick(int i, int j) {
            String s = "";
            boolean success;
            if (!gameOver) {
                success = setToken(whoseTurn);
                if (success == false) {
                    s = whoseTurn + " cannot placed in the same slot";
                } else {
                    if (hasWon(whoseTurn, i, j)) {
                        gameOver = true;
                        s = whoseTurn + " won! The game is over";
                    } else if (isFull()) {
                        gameOver = true;
                        s = "Draw! The game is over";
                    } else {
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        s = whoseTurn + "'s turn";
                    }
                }
                statusLabel.setText(s);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
