package nl.pepijnschildkamp.packer.packing;

public class Dimension {

    protected int width;
    protected int depth;
    protected int height;
    protected transient long volume;

    protected final String name;

    public Dimension(String name) {
        this.name = name;
    }

    public Dimension() {
        this(null);
    }

    public Dimension(String name, int w, int d, int h) {
        this.name = name;

        depth = d;
        width = w;
        height = h;

        volume = ((long) depth) * ((long) width) * ((long) height);
    }

    public Dimension(int w, int d, int h) {
        this(null, w, d, h);
    }

    /**
     * 
     * Check whether a dimension fits within the current object, rotated in 2D.
     *
     * @param w
     *          width of the item
     * @param d
     *          depth of the item
     * @param h
     *          height of the item
     * @return true if any rotation of the argument can be placed inside this
     * 
     */

    public boolean canHold2D(int w, int d, int h) {
        if (h > height) {
            return false;
        }
        return (w <= width && d <= depth) || (d <= width && w <= depth);
    }

    /**
     * Check whether this object fits within a dimension (without rotation).
     * 
     * @param w
     *          width of the item
     * @param d
     *          depth of the item
     * @param h
     *          height of the item
     * @return true if this can fit within the argument space
     */

    public boolean fitsInside3D(int w, int d, int h) {
        if (w >= width && h >= height && d >= depth) {
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return width <= 0 || depth <= 0 || height <= 0;
    }

    @Override
    public String toString() {
        return name + " (Hoogte: " + height + ", Breedte: " + width + ", Diepte: " + depth + ")";
    }

    public int getWidth() {
        return this.width;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getHeight() {
        return this.height;
    }

    public long getVolume() {
        return this.volume;
    }

    public String getName() {
        return this.name;
    }
}