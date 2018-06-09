package packing;

/**
 * Placement as in box in a space.
 * 
 * The box does not necessarily fill the whole space.
 */

public class Placement {

    public transient Space space;
    public transient Box box;

    public Placement(Space space) {
        this.space = space;
    }

    public Space getSpace() {
        return space;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "Placement [" + space.getX() + "x" + space.getY() + "x" + space.getZ() + ", width=" + box.getWidth() + ", depth=" + box.getDepth() + ", height="
                + box.getHeight() + "]";
    }
}
