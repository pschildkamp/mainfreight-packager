package packing;

public class Space extends Dimension {

    public transient Space parent;
    public transient Space remainder;

    public int x; // width
    public int y; // depth
    public int z; // height

    public Space() {
        super();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setParent(Space parent) {
        this.parent = parent;
    }

    public void setRemainder(Space dual) {
        remainder = dual;
    }

    public Space getRemainder() {
        return remainder;
    }

    @Override
    public String toString() {
        return "Space [name=" + name + ", " + x + "x" + y + "x" + z + ", width=" + width + ", depth=" + depth + ", height=" + height + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((remainder == null) ? 0 : remainder.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Space other = (Space) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (remainder == null) {
            if (other.remainder != null)
                return false;
        } else if (!remainder.equals(other.remainder))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    public void copyFrom(Space space) {

        parent = space.parent;
        x = space.x;
        y = space.y;
        z = space.z;

        width = space.width;
        depth = space.depth;
        height = space.height;
    }

    public void copyFrom(int w, int d, int h, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        width = w;
        depth = d;
        height = h;
    }

}
