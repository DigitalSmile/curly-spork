package io.github.digitalsmile;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

public record IndexNative() {

    private static final SymbolLookup LIBCLANG = SymbolLookup.libraryLookup(Path.of("/usr/lib/x86_64-linux-gnu/libclang-20.so.20"), Arena.global());

    private static final MethodHandle CREATE_INDEX = Linker.nativeLinker().downcallHandle(
            LIBCLANG.find("clang_createIndex").orElseThrow(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
    private static final MethodHandle DISPOSE_INDEX = Linker.nativeLinker().downcallHandle(
            LIBCLANG.find("clang_disposeIndex").orElseThrow(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    public MemorySegment createIndex(int pcm, int showDiagnostics) {
        try {
            return (MemorySegment) CREATE_INDEX.invokeExact(pcm, showDiagnostics);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void disposeIndex(MemorySegment index) {
        try {
            DISPOSE_INDEX.invokeExact(index);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public MemorySegment createIndexWithArena(Arena arena, int pcm, int showDiagnostics) {
        var libclang = SymbolLookup.libraryLookup(Path.of("/usr/lib/x86_64-linux-gnu/libclang-20.so.20"), arena);
        var CREATE_INDEX = Linker.nativeLinker().downcallHandle(
                libclang.find("clang_createIndex").orElseThrow(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
        try {
            return (MemorySegment) CREATE_INDEX.invokeExact(pcm, showDiagnostics);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void disposeIndexWithArena(Arena arena, MemorySegment index) {
        var libclang = SymbolLookup.libraryLookup(Path.of("/usr/lib/x86_64-linux-gnu/libclang-20.so.20"), arena);
        var DISPOSE_INDEX = Linker.nativeLinker().downcallHandle(
                libclang.find("clang_disposeIndex").orElseThrow(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
        try {
            DISPOSE_INDEX.invokeExact(index);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int strlenGlibc(MemorySegment string) {
        var glibc = Linker.nativeLinker().defaultLookup();
        var strlen = Linker.nativeLinker().downcallHandle(
                glibc.find("strlen").orElseThrow(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        try {
            return (int) strlen.invokeExact(string);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
