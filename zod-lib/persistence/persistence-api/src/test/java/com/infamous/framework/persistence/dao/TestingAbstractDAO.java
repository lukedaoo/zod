package com.infamous.framework.persistence.dao;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.Person;
import java.util.List;
import java.util.stream.Collectors;

class TestingAbstractDAO extends AbstractDAO<Person, String> {

    public TestingAbstractDAO(DataStoreManager dataStoreManager) {
        super(dataStoreManager, Person.class, "TESTING_DS");
    }

    @Override
    public List findById(List<String> list) {
        return list.stream().map(this::findById)
            .collect(Collectors.toList());
    }
}
