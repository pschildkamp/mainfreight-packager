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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * 
     * Check whether a dimension fits within the current object, rotated in 2D.
     * 
     * @param dimension
     *            the dimension to fit
     * @return true if any rotation of the argument can be placed inside this
     * 
     */

    public boolean canHold2D(Dimension dimension) {
        return canHold2D(dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
    }

    public boolean canHold2D(int w, int d, int h) {
        if (h > height) {
            return false;
        }
        return (w <= width && d <= depth) || (d <= width && w <= depth);
    }

    /**
     * Check whether this object fits within a dimension (without rotation).
     * 
     * @param dimension
     *            the dimensions to fit within
     * @return true if this can fit within the argument space
     */

    public boolean fitsInside3D(Dimension dimension) {
        return fitsInside3D(dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
    }

    public boolean fitsInside3D(int w, int d, int h) {
        if (w >= width && h >= height && d >= depth) {
            return true;
        }

        return false;
    }

    public long getVolume() {
        return volume;
    }

    public boolean isEmpty() {
        return width <= 0 || depth <= 0 || height <= 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (Hoogte: " + height + ", Breedte: " + width + ", Diepte: " + depth + ")";
    }
}