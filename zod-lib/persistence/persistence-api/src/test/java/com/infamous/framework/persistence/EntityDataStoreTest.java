package com.infamous.framework.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityDataStoreTest {

    public static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(EntityDataStoreTest.class, "persistence.app");
    EMFactory m_emf = new JPAEntityManagerFactory("zod_db_test");
    TestingJPAEntityDataStoreManager m_eds = new TestingJPAEntityDataStoreManager(m_emf);

    @BeforeEach
    public void before() {
        m_eds.beginTransaction();
        m_eds.deleteAll(Person.class);

        List persons = m_eds.findAll(Person.class);
        assertTrue(persons.isEmpty());
        m_eds.commitTransaction();
    }

    @Test
    public void testPersist() {
        createPersons();

        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            List<Person> persons = m_eds.findAll(Person.class);

            assertNotNull(persons);
            assertEquals(persons.size(), 4);
            assertEquals(persons.get(0), new Person("p1", "A"));
            assertEquals(persons.get(1), new Person("p2", "B"));
            assertEquals(persons.get(2), new Person("p3", "C"));
            assertEquals(persons.get(3), new Person("p4", "D"));
        });
    }

    @Test
    public void testMerge() {
        createPersons();

        assertFalse(m_eds.isActive());

        mergePersons();

        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            List<Person> persons = (List<Person>) m_eds.findAll(Person.class);

            assertNotNull(persons);
            assertEquals(persons.size(), 4);
            assertEquals(persons.get(0), new Person("p1", "A_updated"));
            assertEquals(persons.get(1), new Person("p2", "B_updated"));
            assertEquals(persons.get(2), new Person("p3", "C_updated"));
            assertEquals(persons.get(3), new Person("p4", "D_updated"));
        });
    }

    @Test
    public void testDelete() {
        createPersons();

        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            m_eds.deleteById(Person.class, "p1");
            m_eds.deleteById(Person.class, "p2");
        });

        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            List<Person> persons = m_eds.findAll(Person.class);

            assertNotNull(persons);
            assertEquals(persons.size(), 2);
            assertEquals(persons.get(0), new Person("p3", "C"));
            assertEquals(persons.get(1), new Person("p4", "D"));
        });
    }

    @Test
    public void testFindByNativeQuery() {
        createPersons();
        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            List<Person> personList = m_eds.findByNativeQuery(Person.class, "SELECT * FROM person");
            assertNotNull(personList);

            assertNotNull(personList);
            assertEquals(personList.size(), 4);
            assertEquals(personList.get(0), new Person("p1", "A"));
            assertEquals(personList.get(1), new Person("p2", "B"));
            assertEquals(personList.get(2), new Person("p3", "C"));
            assertEquals(personList.get(3), new Person("p4", "D"));
        });
    }

    @Test
    public void testFindByNativeQuery_2() {
        createPersons();
        assertFalse(m_eds.isActive());

        executeInTx(() -> {
            List<Object[]> persons = (List<Object[]>) m_eds.findByNativeQuery("SELECT * FROM person");
            assertNotNull(persons);

            List<Person> personList = persons.stream()
                .map(o -> new Person(String.valueOf(o[0]), String.valueOf(o[1])))
                .collect(Collectors.toList());

            assertNotNull(personList);
            assertEquals(personList.size(), 4);
            assertEquals(personList.get(0), new Person("p1", "A"));
            assertEquals(personList.get(1), new Person("p2", "B"));
            assertEquals(personList.get(2), new Person("p3", "C"));
            assertEquals(personList.get(3), new Person("p4", "D"));
        });
    }


    private void mergePersons() {
        executeInTx(() -> {
            Person p1Updated = new Person("p1", "A_updated");
            Person p2Updated = new Person("p2", "B_updated");
            Person p3Updated = new Person("p3", "C_updated");
            Person p4Updated = new Person("p4", "D_updated");

            m_eds.merge(p1Updated);
            m_eds.merge(p2Updated);
            m_eds.merge(p3Updated);
            m_eds.merge(p4Updated);
        });
    }

    private void createPersons() {
        executeInTx(() -> {
            Person p1 = new Person("p1", "A");
            Person p2 = new Person("p2", "B");
            Person p3 = new Person("p3", "C");
            Person p4 = new Person("p4", "D");
            m_eds.persist(p1);
            m_eds.persist(p2);
            m_eds.persist(p3);
            m_eds.persist(p4);
        });
    }

    private void executeInTx(Template template) {
        m_eds.beginTransaction();
        template.execute();
        m_eds.commitTransaction();
    }

    interface Template {

        void execute();
    }
}