package com.ting.testmapper.codegen;

import com.ting.testmapper.transformer.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceCodeContext {

    private static final AtomicInteger UNIQUE_CLASS_INDEX = new AtomicInteger();
    public static final char LF = '\n';
    private StringBuilder sourceBuilder;
    private String classSimpleName;
    private String packageName;
    private String className;
    private Class<?> superClass;
    private List<String> methods;
    private List<String> fields;
    private List<Transformer<Object, Object>> transformers;
    private int transformerIndices = 0;

    public SourceCodeContext(String baseClassName, Class<?> superClass) {
        this.sourceBuilder = new StringBuilder();
        String safeBaseClassName = baseClassName.replace("[]", "$Array");
        this.superClass = superClass;
        this.packageName = "com.ting.testmapper.generated";
        this.classSimpleName = safeBaseClassName;
        this.methods = new ArrayList<String>();
        this.fields = new ArrayList<String>();

        this.classSimpleName = getUniqueClassName(this.classSimpleName);
        this.className = this.packageName + "." + this.classSimpleName;
        sourceBuilder.append("package ");
        sourceBuilder.append(packageName);
        sourceBuilder.append(LF);
        sourceBuilder.append(LF);
        sourceBuilder.append("public class ");
        sourceBuilder.append(this.classSimpleName);
        sourceBuilder.append(" extends GeneratedMapperBase {");
    }

    private String getUniqueClassName(String name) {
        return name + System.nanoTime() + "$" + UNIQUE_CLASS_INDEX.getAndIncrement();
    }

    public String toSourceFile() {
        return sourceBuilder.toString() + LF + "}";
    }

    public void addMethod(String methodSource) {
        sourceBuilder.append(LF);
        sourceBuilder.append(methodSource);
        sourceBuilder.append(LF);
        this.methods.add(methodSource);
    }

    public String getMapper() {
        return "mapper";
    }

    public Transformer<Object, Object>[] getTransformers() {
        if (transformers == null) {
            return null;
        }
        return transformers.toArray(new Transformer[] {});
    }

    public String getTransformer(Transformer<Object, Object> transformer) {
        if (transformers == null) {
            transformers = new ArrayList<Transformer<Object, Object>>(8);
        }
        transformers.add(transformer);
        return "(transformers[" + transformerIndices++ + "])";
    }

    public Class<?> getSuperClass(){
        return superClass;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }



}
