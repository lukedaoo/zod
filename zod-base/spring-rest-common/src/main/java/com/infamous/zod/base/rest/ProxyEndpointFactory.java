package com.infamous.zod.base.rest;

import java.lang.reflect.Proxy;

public class ProxyEndpointFactory {

    private static final ProxyEndpointFactory INSTANCE = new ProxyEndpointFactory();

    private ProxyEndpointFactory() {

    }

    public static ProxyEndpointFactory getInstance() {
        return INSTANCE;
    }

    public <T> T create(ProxyEndpointContext<T> context) {
        return (T) Proxy.newProxyInstance(context.getInterface().getClassLoader(),
            new Class[]{context.getInterface()},
            new DynamicEndPointHandler<>(context)
        );
    }
}