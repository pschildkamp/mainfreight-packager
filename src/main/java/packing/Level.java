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
            Box box = placement.getBox();
            if (box.getHeight() > height) {
                height = box.getHeight();
            }
        }

        return height;
    }

    public int getTotalAmountOfBoxes()  {
        return this.size();
    }
}