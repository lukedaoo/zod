package com.infamous.zod.songmgmt;

import com.infamous.framework.factory.JacksonConverterFactory;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.engine.JavaHttpEngine;
import com.infamous.framework.http.factory.ZodHttpClientFactory;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.zod.base.jpa.JPACommonUtils;
import com.infamous.zod.songmgmt.converter.SongConverter;
import com.infamous.zod.songmgmt.repository.SongManagementDataStore;
import com.infamous.zod.storage.StorageFileRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class SongManagementAutoConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean createStorageFileEmf() {
        return JPACommonUtils.createEntityManagerFactory((emf) -> {
            emf.setPersistenceUnitName("song-mgmt-ds");
            emf.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
        });
    }

    @Bean
    public SongManagementDataStore createSongMgmtDataStore(DataStoreManager dataStoreManager) {
        SongManagementDataStore dataStore = new SongManagementDataStore();
        dataStoreManager.register(SongManagementDataStore.DS_NAME, dataStore);
        return dataStore;
    }

    @Bean
    public SongConverter createConverter() {
        return new SongConverter();
    }

    // TODO:
    //  - Create ZodRestClient manager
    //  - Dynamic Url
    @Bean
    public StorageFileRestClient createStorageRestClient() {
        ZodHttpClientFactory clientFactory =
            ZodHttpClientFactory.builder()
                .baseUrl("http://localhost:5080/storage/v1")
                .converterFactory(JacksonConverterFactory.create())
                .callEngine(JavaHttpEngine.getInstance())
                .config(new HttpConfig())
                .build();

        return clientFactory.create(StorageFileRestClient.class);
    }
}
