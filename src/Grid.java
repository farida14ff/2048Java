import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Grid extends JPanel{

	public static final int ROW = 4;
    final Start host;
    
    private Tile[] tiles;
    public static NumbersAndColors goal = NumbersAndColors._2048;

    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final Font STR_FONT = new Font(Font.SERIF, Font.BOLD, 16);
    private static final int SIDE = 64;
    private static final int MARGIN = 16;
    

    public Grid(Start main){
    	host = main;
    	setFocusable(true);
    	initializeTiles();
    }
    
    private void addNewTile(){
    	List<Integer> list = findEmptyIndex();
    	int index = list.get((int) (Math.random() * list.size()));
    	tiles[index] = Tile.randomTile();
    }


    public void initializeTiles() {
        tiles = new Tile[ROW * ROW];

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.ZERO;
        }

        addNewTile();
        addNewTile();
    }
    
    private List<Integer> findEmptyIndex() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].empty())
                list.add(i);
        }
        return list;
    }
    

    private boolean isGridFull() {
        return findEmptyIndex().size() == 0;
    }

    private Tile tileAt(int x, int y) {
        return tiles[x + y * ROW];
    }
    
    public boolean checkIfCanMove() {
        if (!isGridFull()) {
            return true;
        }
        return false;
    }
    

    private Tile[] rotate(int degree) {
        Tile[] newTiles = new Tile[ROW * ROW];
        int offsetX = 3, offsetY = 3;
        if (degree == 90) {
            offsetY = 0;
        } else if (degree == 180) {

        } else if (degree == 270) {
            offsetX = 0;
        }
        double radians = Math.toRadians(degree);
        int cos = (int) Math.cos(radians);
        int sin = (int) Math.sin(radians);
        for (int x = 0; x < 4; x++){
        	for (int y = 0; y < 4; y++){
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * ROW] = tileAt(x, y);
            }
        }
        return newTiles;
    }


    private Tile[] getLine(int index) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++){
            result[i] = tileAt(i, index);
        }
        return result;
    }
    

    private static void ensureSize(List<Tile> l, int s) {
        while (l.size() < s) {
            l.add(Tile.ZERO);
        }
    }
    

    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i = 0; i < 4; i++){
            if (!oldLine[i].empty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++){
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }
    

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> tilesList = new LinkedList<>();
        for (int i = 0; i < ROW; i++) {
            if (i < ROW - 1
                    && !oldLine[i].empty()
                    && oldLine[i].equals(oldLine[i + 1])) {
                Tile merged = oldLine[i].getTilesNumericValue();
                i++;
                tilesList.add(merged);
                if (merged.value() == goal) {
                    host.win();
                }
            } else {
                tilesList.add(oldLine[i]);
            }
        }
        ensureSize(tilesList, 4);
        return tilesList.toArray(new Tile[4]);
    }
    

    private void setLine(int index, Tile[] re) {
    	for (int i = 0; i < 4; i++){
            tiles[i + index * ROW] = re[i];
        }
    }

    public void moveGrid(){
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++){
            Tile[] origin = getLine(i);
            Tile[] afterMove = moveLine(origin);
            Tile[] merged = mergeLine(afterMove);
            setLine(i, merged);
            if (!needAddTile && !Arrays.equals(origin, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addNewTile();
        }
    }
    
    public void left(){
        moveGrid();
    }

    public void right() {
        tiles = rotate(180);
        moveGrid();
        tiles = rotate(180);
    }

    public void up() {
        tiles = rotate(270);
        moveGrid();
        tiles = rotate(90);
    }

    public void down() {
        tiles = rotate(90);
        moveGrid();
        tiles = rotate(270);
    }
 

    private static int offsetCoors(int n) {
        return n * (MARGIN + SIDE) + MARGIN;
    }
    

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        NumbersAndColors val = tile.value();
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(val.getColor());
        g.fillRect(xOffset, yOffset, SIDE, SIDE);
        g.setColor(val.getFontColor());
        
        if (val.getValue() != 0)
            g.drawString(tile.toString(), xOffset
                    + (SIDE >> 1) - MARGIN, yOffset + (SIDE >> 1));
    }
    

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.setFont(STR_FONT);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        for (int x = 0; x < 4; x++){
        	for (int y = 0; y < 4; y++){
                drawTile(g, tiles[x + y * ROW], x, y);
            }
        }
    }

}
