package packing;

public class Item extends Dimension implements Cloneable {

    public Item(String name, int w, int d, int h) {
        super(name, w, d, h);
    }

    @Override
    protected Item clone() {
        return new Item(name, width, depth, height);
    }
}