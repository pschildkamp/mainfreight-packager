package nl.pepijnschildkamp.packer.packing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    public void testClone() {
        Item item = new Item("toBeCloned", 5, 5, 5);
        Item actualClone = item.clone();
        Item expectedClone = new Item("toBeCloned", 5, 5, 5);

        assertThat(item).isEqualToComparingFieldByField(expectedClone);
        assertThat(item).isNotEqualTo(actualClone);
    }
}
