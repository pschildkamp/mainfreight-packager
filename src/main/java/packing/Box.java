package packing;

public class Box extends Dimension implements Cloneable {

    public Box(String name, int w, int d, int h) {
        super(name, w, d, h);
    }

    protected Box clone() {
        return new Box(name, width, depth, height);
    }
}