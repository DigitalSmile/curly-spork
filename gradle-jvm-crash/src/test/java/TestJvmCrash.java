import io.github.digitalsmile.IndexNative;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

public class TestJvmCrash {

    @Test
    public void simpleTest() {
        /*
         Always crashes, but test is marked green
         */
        var indexNative = new IndexNative();
        var indexMemorySegment = indexNative.createIndex(0, 0);
        indexNative.disposeIndex(indexMemorySegment);

        Assertions.assertTrue(true);
    }

    @Test
    public void simpleTestWithConfinedArena() {
        /*
         Always crashes, but test is marked green
         */
        var indexNative = new IndexNative();
        try (var offheap = Arena.ofConfined()) {
            var indexMemorySegment = indexNative.createIndexWithArena(offheap, 0, 0);
            indexNative.disposeIndexWithArena(offheap, indexMemorySegment);
        } catch (Throwable e) {
            Assertions.fail(e);
        }
        Assertions.assertTrue(true);
    }

    @Test
    public void simpleTestGlibc() {
        /*
         Works fine
         */
        var indexNative = new IndexNative();
        try (var offheap = Arena.ofConfined()) {
            var pointer = offheap.allocateFrom("Hello");
            var size = indexNative.strlenGlibc(pointer);
            Assertions.assertEquals(5, size);
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }
}
