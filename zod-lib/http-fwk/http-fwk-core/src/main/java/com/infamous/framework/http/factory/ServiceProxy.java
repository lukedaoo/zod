package com.infamous.framework.http.factory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

class ServiceProxy {

    @SuppressWarnings("unchecked")
    public static <ServiceType> ServiceType create(ZodHttpClientFactory zodHttpClientFactory,
                                                   Class<ServiceType> service) {
        return (ServiceType) Proxy.newProxyInstance(service.getClassLoader(),
            new Class[]{service}, new ServiceProxyHandler<>(zodHttpClientFactory, service));
    }

    static class ServiceProxyHandler<ServiceType> implements InvocationHandler {

        private final ZodHttpClientFactory m_zodHttpClientFactory;
        private final Class<ServiceType> m_service;

        public ServiceProxyHandler(ZodHttpClientFactory zodHttpClientFactory, Class<ServiceType> service) {
            m_zodHttpClientFactory = zodHttpClientFactory;
            m_service = service;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) {
                return invokeDefaultMethod(method, m_service, proxy, args);
            }
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            return loadServiceMethod(m_zodHttpClientFactory, method).invoke(args);
        }

        private Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, Object... args)
            throws Throwable {
            Lookup lookup = MethodHandles.lookup();
            return lookup.unreflectSpecial(method, declaringClass).bindTo(object).invokeWithArguments(args);
        }

        @SuppressWarnings("unchecked")
        private <T> ServiceMethod<T> loadServiceMethod(ZodHttpClientFactory zodHttpClientFactory, Method method) {
            ServiceMethod<T> serviceMethod = ServiceMethod.parseAnnotation(zodHttpClientFactory, method);

            return Optional.ofNullable(serviceMethod)
                .orElseThrow(() -> new IllegalStateException("Can't load Service Method"));
        }
    }
}
