package nl.pepijnschildkamp.packer.packing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WaveTest {
    private final static Wave testWave;

    static {
        Placement placement = new Placement(new Space());
        placement.setItem(new Item("item 2", 5, 5, 5));
        Level level = new Level();
        level.add(placement);

        testWave = new Wave(new Dimension("wave", 10, 10, 10));
        testWave.add(level);
    }

    @Test
    public void currentLevelStackHeight() {
        assertThat(testWave.getStackHeight()).isEqualTo(5);
    }

    @Test
    public void testGetFreeSpace() {
        Dimension expectedDimension = new Dimension(10, 10, 5);
        assertThat(testWave.getFreeSpace()).isEqualToComparingFieldByField(expectedDimension);
    }
}
