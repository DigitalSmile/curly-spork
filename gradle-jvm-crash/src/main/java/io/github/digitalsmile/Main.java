package io.github.digitalsmile;

public class Main {
    static void main() {
        /*
         Works fine
         */
        var indexNative = new IndexNative();
        var indexMemorySegment = indexNative.createIndex(0, 0);
        indexNative.disposeIndex(indexMemorySegment);
    }
}
