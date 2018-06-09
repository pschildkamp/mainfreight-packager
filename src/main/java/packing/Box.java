package packing;

public class Box extends Dimension implements Cloneable {

    public Box(int w, int d, int h) {
        super(w, d, h);
    }

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

    /**
     * 
     * Rotate box, i.e. in 2 dimensions, keeping the height constant.
     * 
     * @return this
     */

    public Box rotate2D() {
        int depth = this.depth;

        this.depth = width;
        width = depth;

        return this;
    }

}