package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.HttpRequest;
import java.lang.reflect.Type;

public interface CallEngine {

    Call transform(Type returnType, HttpRequest request);
}
