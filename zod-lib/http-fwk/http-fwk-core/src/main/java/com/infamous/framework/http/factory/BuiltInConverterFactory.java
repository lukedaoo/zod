package com.infamous.framework.http.factory;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;

class BuiltInConverterFactory extends ConverterFactory {

    public Converter<Object, String> getStringConverter() {
        return ToStringConverter.getInstance();
    }

    static class ToStringConverter extends Converter<Object, String> {

        private static ToStringConverter INSTANCE;

        private ToStringConverter() {
            super(new ConvertProcessor<>(String::valueOf));
        }

        public static ToStringConverter getInstance() {
            if (INSTANCE == null) {
                synchronized (ToStringConverter.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ToStringConverter();
                    }
                }
            }
            return INSTANCE;
        }

    }
}
