package nl.pepijnschildkamp.packer.packing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaceTest {

    @Test
    public void copyFromViaParent() {
        Space parentSpace = new Space();
        parentSpace.x = 5;
        parentSpace.y = 5;
        parentSpace.z = 0;

        parentSpace.width = 25;
        parentSpace.depth = 50;
        parentSpace.height = 25;

        Space space = new Space();
        space.x = 10;
        space.y = 10;
        space.z = 10;

        space.width = 25;
        space.depth = 50;
        space.height = 25;
        space.setParent(parentSpace);

        Space fromSpace = new Space();
        fromSpace.copyFrom(space);

        assertThat(fromSpace).hasFieldOrPropertyWithValue("x", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("y", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("z", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("width", 25);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("depth", 50);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("height", 25);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("parent", parentSpace);
    }

    @Test
    public void copyFromViaParameters() {
        Space fromSpace = new Space();
        fromSpace.copyFrom(25, 50, 25, 10, 10, 10);

        assertThat(fromSpace).hasFieldOrPropertyWithValue("x", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("y", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("z", 10);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("width", 25);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("depth", 50);
        assertThat(fromSpace).hasFieldOrPropertyWithValue("height", 25);
        assertThat(fromSpace.getParent()).isNull();
    }
}
