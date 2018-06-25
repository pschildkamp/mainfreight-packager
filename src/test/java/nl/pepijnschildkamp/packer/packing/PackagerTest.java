package nl.pepijnschildkamp.packer.packing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PackagerTest {

    private final static Item item1;
    private final static Item item2;
    private final static Item item3;
    private final static Item item4;

    static {
        item1 = new Item("Item1", 5, 5, 5);
        item2 = new Item("Item2", 6, 6, 6);
        item3 = new Item("Item3", 7, 7, 7);
        item4 = new Item("Item4", 8, 8, 8);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPack() {
        Wave wave = new Wave(new Dimension(25, 25, 25));
        Packager packager = new Packager(wave);
        Wave packedWave = packager.pack(Arrays.asList(item1, item2, item3, item4));

        assertThat(packedWave).isNotNull();
        assertThat(packedWave.getLevels()).hasSize(3);
    }

    @Test
    public void testFilterWaveThrowsException() {
        Wave wave = new Wave(new Dimension(5, 5, 5));
        Packager packager = new Packager(wave);
        exception.expect(WaveTooSmallException.class);
        Wave packedWave = packager.pack(Collections.singletonList(item2));
        assertThat(packedWave).isNull();
    }
}
