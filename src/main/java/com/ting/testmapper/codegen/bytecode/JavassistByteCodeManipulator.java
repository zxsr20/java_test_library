package com.ting.testmapper.codegen.bytecode;

import com.ting.testmapper.codegen.SourceCodeContext;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ting.testmapper.util.SystemPropertyUtil.*;


public class JavassistByteCodeManipulator implements ByteCodeMainipulator{

    private static Logger LOGGER = LoggerFactory.getLogger(JavassistByteCodeManipulator.class);

    private ClassPool classPool;

    protected final boolean enableWriteSourceFile;

    protected final boolean enableWriteClassFile;

    protected final String pathToWriteSourceFile;

    protected final String pathToWriteClassFile;

    /**
     * 默认的输出路径
     */
    protected static final String CLASS_PATH = "classpath:/";

    public JavassistByteCodeManipulator() {
        this.enableWriteSourceFile = Boolean.valueOf(getSystemProperty(ENABLE_WRITE_SOURCE_FILE, "false"));
        this.enableWriteClassFile = Boolean.valueOf(getSystemProperty(ENABLE_WRITE_CLASS_FILE, "false"));
        this.pathToWriteSourceFile = getSystemProperty(WRITE_SOURCE_FILE_ABSOLUTE_PATH, CLASS_PATH);
        this.pathToWriteClassFile = getSystemProperty(WRITE_CLASS_FILE_ABSOLUTE_PATH, CLASS_PATH);
        this.classPool = new ClassPool();
        this.classPool.appendSystemPath();
        this.classPool.insertClassPath(new ClassClassPath(this.getClass()));
        LOGGER.debug(
                "Initialize {} done, enableWriteSourceFile={}, enableWriteClassFile={}, pathToWriteSourceFile={}, "
                        + "pathToWriteClassFile={}", this.getClass().getSimpleName(), enableWriteSourceFile,
                enableWriteClassFile, pathToWriteSourceFile, pathToWriteClassFile);
    }

    @Override
    public Class<?> compileClass(SourceCodeContext sourceCode) {
        StringBuilder className = new StringBuilder(sourceCode.getClassName());
        CtClass byteCodeClass = classPool.makeClass(className.toString());

        Class<?> compileClass;

        try {
            writeSourceFile(sourceCode);
            CtClass abstractMapperClass = classPool.get(sourceCode.getSuperClass().getCanonicalName());
            byteCodeClass.setSuperclass(abstractMapperClass);

            for(String fieldDef : sourceCode.getFields()){
                byteCodeClass.addField(CtField.make(fieldDef, byteCodeClass));


            }
            for(String methodDef : sourceCode.getMethods()){
                byteCodeClass.addMethod(CtNewMethod.make(methodDef, byteCodeClass));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeSourceFile(SourceCodeContext sourceCode) {

    }
}
