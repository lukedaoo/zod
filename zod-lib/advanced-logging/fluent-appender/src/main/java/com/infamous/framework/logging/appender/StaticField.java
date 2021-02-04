package com.infamous.framework.logging.appender;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;

@Plugin(name = "StaticField", category = Node.CATEGORY, printObject = true)
public final class StaticField {

    private final String name;
    private final String value;
    private final boolean valueNeedsLookup;

    private StaticField(final String name, final String value) {
        this.name = name;
        this.value = value;
        this.valueNeedsLookup = value != null && value.contains("${");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value == null ? "" : value;
    }

    public boolean isValueNeedsLookup() {
        return valueNeedsLookup;
    }

    @PluginFactory
    public static StaticField createStaticField(
        @PluginAttribute("name") final String name,
        @PluginValue("value") final String value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is required");
        }
        return new StaticField(name, value);
    }

    @Override
    public String toString() {
        return name + '=' + getValue();
    }
}

