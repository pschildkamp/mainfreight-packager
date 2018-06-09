package packing;

public class Box extends Dimension implements Cloneable {

    public Box(String name, int w, int d, int h) {
        super(name, w, d, h);
    }

    @Override
    public String toString() {
        return "Box [name=" + name + ", width=" + width + ", depth=" + depth + ", height=" + height + ", volume=" + volume + "]";
    }

    protected Box clone() {
        return new Box(name, width, depth, height);
    }

}