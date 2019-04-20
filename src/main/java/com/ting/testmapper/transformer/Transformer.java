package com.ting.testmapper.transformer;

public interface Transformer<S, D>{
    D transform(S source);
}
