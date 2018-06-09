package packing;

public class Dimension {

    protected int width; // x
    protected int depth; // y
    protected int height; // z
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
        return width <= 0 || depth <= 0 || depth <= 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + depth;
        result = prime * result + height;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (volume ^ (volume >>> 32));
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dimension other = (Dimension) obj;
        if (depth != other.depth)
            return false;
        if (height != other.height)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (volume != other.volume)
            return false;
        if (width != other.width)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name + " (Hoogte: " + height + ", Breedte: " + width + ", Diepte: " + depth + ")";
    }
}