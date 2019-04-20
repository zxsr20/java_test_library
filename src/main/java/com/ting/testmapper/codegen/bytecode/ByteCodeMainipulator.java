package com.ting.testmapper.codegen.bytecode;

import com.ting.testmapper.codegen.SourceCodeContext;

public interface ByteCodeMainipulator {

    Class<?> compileClass(SourceCodeContext sourceCode);
}
