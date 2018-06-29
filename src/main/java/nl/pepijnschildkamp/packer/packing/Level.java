package nl.pepijnschildkamp.packer.packing;

import java.util.ArrayList;

public class Level extends ArrayList<Placement> {

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

    public int getTotalAmountOfItems() {
        return size();
    }
}