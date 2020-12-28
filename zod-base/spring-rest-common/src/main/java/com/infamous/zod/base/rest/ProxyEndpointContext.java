package com.infamous.zod.base.rest;

import com.infamous.framework.logging.ZodLogger;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Getter;

public class ProxyEndpointContext<T> {

    private @Getter final T m_target;
    private @Getter final Class<?> m_interface;
    private @Getter final ZodLogger m_logger;

    private @Getter Consumer<T> m_postInit;

    private ProxyEndpointContext(ProxyEndpointContextBuilder<T> builder) {
        m_target = builder.m_target;
        m_interface = builder.m_interface;
        m_logger = builder.m_logger;
        m_postInit = builder.m_postInit;
    }

    public static class ProxyEndpointContextBuilder<T> {

        private T m_target;
        private Class<?> m_interface;
        private ZodLogger m_logger;
        private Consumer<T> m_postInit;

        public ProxyEndpointContextBuilder() {

        }

        public ProxyEndpointContextBuilder<T> anInterface(Class<?> anInterface) {
            Objects.requireNonNull(anInterface);
            m_interface = anInterface;
            return this;
        }

        public ProxyEndpointContextBuilder<T> target(T target) {
            Objects.requireNonNull(target);
            m_target = target;
            return this;
        }

        public ProxyEndpointContextBuilder<T> logger(ZodLogger logger) {
            Objects.requireNonNull(logger);
            m_logger = logger;
            return this;
        }

        public ProxyEndpointContextBuilder<T> postInit(Consumer<T> postInit) {
            m_postInit = postInit;
            return this;
        }

        public ProxyEndpointContext<T> build() {
            return new ProxyEndpointContext<T>(this);
        }
    }
}
