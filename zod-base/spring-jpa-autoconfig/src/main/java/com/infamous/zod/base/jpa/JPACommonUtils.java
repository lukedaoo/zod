package com.infamous.zod.base.jpa;

import java.util.function.Consumer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class JPACommonUtils {

    public static LocalContainerEntityManagerFactoryBean createEntityManagerFactory(
        Consumer<LocalContainerEntityManagerFactoryBean> consumer) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        consumer.accept(emf);
        return emf;
    }

}
