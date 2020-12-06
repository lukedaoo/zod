package com.infamous.framework.persistence;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Person")
@Table(name = "Person")
public class Person {

    private String m_id;

    private String m_name;

    public Person() {

    }

    public Person(String id) {
        this.m_id = id;
    }

    public Person(String id, String name) {
        m_id = id;
        m_name = name;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return m_id;
    }

    public void setId(String id) {
        m_id = id;
    }

    @Column(name = "name")
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(m_id, person.m_id) &&
            Objects.equals(m_name, person.m_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id='").append(m_id).append('\'');
        sb.append(", name='").append(m_name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
