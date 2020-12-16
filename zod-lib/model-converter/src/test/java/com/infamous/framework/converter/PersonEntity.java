package com.infamous.framework.converter;

class PersonEntity {

    private String m_name;
    private String m_address;

    public PersonEntity(String name, String address) {
        m_name = name;
        m_address = address;
    }

    public String getName() {
        return m_name;
    }

    public PersonEntity setName(String name) {
        m_name = name;
        return this;
    }

    public String getAddress() {
        return m_address;
    }

    public PersonEntity setAddress(String address) {
        m_address = address;
        return this;
    }
}
