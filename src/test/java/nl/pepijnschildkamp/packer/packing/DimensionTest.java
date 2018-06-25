package nl.pepijnschildkamp.packer.packing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DimensionTest {
    @Test
    public void canHold2DHappyFlow() {
        Dimension dimension = new Dimension("happyFlow", 5, 5, 5);
        assertThat(dimension.canHold2D(5, 5, 3)).isEqualTo(true);
    }

    @Test
    public void canHold2DRotate() {
        Dimension dimension = new Dimension("Rotate", 5, 10, 5);
        assertThat(dimension.canHold2D(10, 5, 5)).isEqualTo(true);
    }

    @Test
    public void canHold2DHeightTooLarge() {
        Dimension dimension = new Dimension("tooLarge", 5, 5, 5);
        assertThat(dimension.canHold2D(4, 3, 6)).isEqualTo(false);
    }

    @Test
    public void fitsInside3DHappyFlow() {
        Dimension dimension = new Dimension("happyFlow", 5, 5, 5);
        assertThat(dimension.fitsInside3D(10, 10, 5)).isEqualTo(true);
    }

    @Test
    public void fitsInside3DTooLarge() {
        Dimension dimension = new Dimension("tooLarge", 5, 5, 5);
        assertThat(dimension.fitsInside3D(5, 4, 3)).isEqualTo(false);
    }

    // isEmpty
    @Test
    public void isEmptyHappyFlow() {
        Dimension dimension = new Dimension("happyFlow", 0,0,0);
        assertThat(dimension.isEmpty()).isEqualTo(true);
    }
}
