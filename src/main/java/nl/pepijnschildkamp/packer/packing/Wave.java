package nl.pepijnschildkamp.packer.packing;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Wave extends Item {

    private int stackHeight = 0;

    public List<Level> getLevels() {
        return levels;
    }

    private List<Level> levels = new ArrayList<>();

    public Wave(Dimension dimension) {
        super(dimension.getName(), dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
    }

    public boolean add(Level element) {
        if (!levels.isEmpty()) {
            stackHeight += currentLevelStackHeight();
        }

        return levels.add(element);
    }

    public int getStackHeight() {
        return stackHeight + currentLevelStackHeight();
    }

    public int currentLevelStackHeight() {
        if (levels.isEmpty()) {
            return 0;
        }
        return levels.get(levels.size() - 1)
                .getHeight();
    }

    public void add(Placement placement) {
        levels.get(levels.size() - 1)
                .add(placement);
    }

    public void addLevel() {
        add(new Level());
    }

    public Dimension getFreeSpace() {
        int spaceHeight = height - getStackHeight();
        if (spaceHeight < 0) {
            throw new IllegalArgumentException("Remaining free space is negative at " + spaceHeight);
        }
        return new Dimension(width, depth, spaceHeight);
    }

    public void clear() {
        levels.clear();
        stackHeight = 0;
    }
}
