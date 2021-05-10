import javax.swing.*;
import java.awt.*;

public class Start extends JFrame {


	JLabel statusBar;
    private static final String TITLE = "2048";
    public static final String WIN_MSG = "Winner, continue?";
    public static final String LOSE_MSG = "Loser, R to reset the game";
    
    public static void main(String[] args) {

        Start game = new Start();
        Grid board = new Grid(game);

        KeyPressListener kb = KeyPressListener.getKeyPress(board);
        board.addKeyListener(kb);
        game.add(board);
        
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }


    public Start() {
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(340, 400);
        setResizable(false);

        statusBar = new JLabel("");
        add(statusBar, BorderLayout.SOUTH);
    }

    void win() {
        statusBar.setText(WIN_MSG);
    }
    void lose() {
        statusBar.setText(LOSE_MSG);
    }
}
