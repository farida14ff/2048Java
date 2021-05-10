import java.util.HashMap;

public class Tile {
    private final NumbersAndColors nac;
    private final static HashMap<Integer, Tile> cache = new HashMap<>();

    public final static Tile ZERO = new Tile(NumbersAndColors._0);
    public final static Tile TWO = new Tile(NumbersAndColors._2);
    public final static Tile FOUR = new Tile(NumbersAndColors._4);
    
    static {
        for (NumbersAndColors n : NumbersAndColors.values()) {
            cache.put(n.getValue(), new Tile(n));
        }
    }


    public Tile(NumbersAndColors n) {
        nac = n;
    }

    NumbersAndColors value() {
        return nac;
    }

    public static Tile valueOf(int num) {
        return cache.get(num);
    }

    public Tile getTilesNumericValue() {
        return valueOf(nac.getValue() << 1);
    }

    boolean empty() {
        return nac == NumbersAndColors._0;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Tile))
            return false;
        Tile other = (Tile) obj;
        if (nac != other.nac)
            return false;
        return true;
    }

    static Tile randomTile() {
        return Math.random() < 0.25 ? FOUR : TWO;
    }

    public String toString() {
        return String.format("%d", nac.getValue());

    }

}

