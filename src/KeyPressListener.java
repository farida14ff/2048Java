import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static java.awt.event.KeyEvent.*;

public class KeyPressListener extends KeyAdapter{


    private static final HashMap<Integer, Method> keyMapping = new HashMap<>();
    
    private static Integer[] KEYS = { VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT, VK_R };
   
    private static String[] methodName = { "up", "down", "left", "right", "initializeTiles" };
    private static Grid board;
    private static final KeyPressListener INSTANCE = new KeyPressListener();
    
    private KeyPressListener() {
        initKey(KEYS);
    }
    
    public static KeyPressListener getKeyPress(Grid g) {
        board = g;
        return INSTANCE;
    }


    void initKey(Integer[] kcs) {
        for (int i = 0; i < kcs.length; i++) {
            try {
                keyMapping.put(kcs[i], Grid.class.getMethod(methodName[i]));
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent k) {
        super.keyPressed(k);
        Method action = keyMapping.get(k.getKeyCode());
        if (action == null) {
            return;
        }
        try {
            action.invoke(board);
            board.repaint();
        } catch (InvocationTargetException | IllegalAccessException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (!board.checkIfCanMove()) {
            board.host.lose();
        }

    }

}
