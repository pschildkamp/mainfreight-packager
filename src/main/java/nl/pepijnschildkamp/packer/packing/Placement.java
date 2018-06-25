package nl.pepijnschildkamp.packer.packing;

/**
 * Placement as in item in a space.
 * 
 * The item does not necessarily fill the whole space.
 */

public class Placement {
    public transient Space space;
    public transient Item item;

    public Placement(Space space) {
        this.space = space;
    }

    public Space getSpace() {
        return this.space;
    }

    public Item getItem() {
        return this.item;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
