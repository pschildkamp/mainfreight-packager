package nl.pepijnschildkamp.packer.packing;

import lombok.Getter;
import lombok.Setter;

/**
 * Placement as in item in a space.
 * 
 * The item does not necessarily fill the whole space.
 */

@Getter
@Setter
public class Placement {
    public transient Space space;
    public transient Item item;

    public Placement(Space space) {
        this.space = space;
    }
}
