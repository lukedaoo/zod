package com.infamous.framework.converter;

public class PersonDto {

    private final String m_name;
    private final String m_address;

    public PersonDto(String name, String address) {
        m_name = name;
        m_address = address;
    }

    public String getName() {
        return m_name;
    }

    public String getAddress() {
        return m_address;
    }
}
