package packing;

import java.util.ArrayList;

/**
 * A level within a container
 * 
 */

public class Level extends ArrayList<Placement> {

    private static final long serialVersionUID = 1L;

    public int getHeight() {
        int height = 0;

        for (Placement placement : this) {
            Item item = placement.getItem();
            if (item.getHeight() > height) {
                height = item.getHeight();
            }
        }

        return height;
    }

    public int getTotalAmountOfBoxes() {
        return size();
    }
}