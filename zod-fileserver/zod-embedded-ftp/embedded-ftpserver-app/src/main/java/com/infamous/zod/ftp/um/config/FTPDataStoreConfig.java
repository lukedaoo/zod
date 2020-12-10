package com.infamous.zod.ftp.um.config;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.zod.base.jpa.JPACommonUtils;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserDAO;
import com.infamous.zod.ftp.um.FTPUserManager;
import com.infamous.zod.ftp.um.impl.EncryptStrategy;
import com.infamous.zod.ftp.um.impl.FTPUserDAOImpl;
import com.infamous.zod.ftp.um.impl.FTPUserManagerRepository;
import com.infamous.zod.ftp.um.impl.PasswordEncryptorWrapper;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class FTPDataStoreConfig {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(FTPDataStoreConfig.class, "ftp.server");

    @Bean(name = "ftpServerEMF")
    public LocalContainerEntityManagerFactoryBean createFTPEMF() {
        return JPACommonUtils.createEntityManagerFactory((emf) -> {
            emf.setPersistenceUnitName("ftp-ds");
            emf.setPersistenceXmlLocation("META-INF/persistence-ftp.xml");
        });
    }

    @Bean
    public FTPDataStore createFTPDataStore(DataStoreManager dataStoreManager) {
        FTPDataStore dataStore = new FTPDataStore();
        dataStoreManager.register(FTPDataStore.DS_NAME, dataStore);
        return dataStore;
    }

    @Bean
    public FTPUserDAO createFTPUserDAO(DataStoreManager dsm) {
        return new FTPUserDAOImpl(dsm);
    }

    @Bean
    public PasswordEncryptor passwordEncryptor(FTPServerConfigProperties config) {
        EncryptStrategy et = EncryptStrategy.find(config.getEncryptorStrategy());
        LOGGER.info("Using '" + et + "' strategy for password encryptor");
        return new PasswordEncryptorWrapper(et);
    }

    @Bean("ftpUserManager")
    public FTPUserManager createUserManger(FTPUserDAO dao, PasswordEncryptor pe,
                                           FTPServerConfigProperties serverConfig) {
        Path rootPath = Paths.get(serverConfig.getRootFolder());
        try {
            Files.createDirectory(rootPath);
        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                LOGGER.error("Error while creating direction warehouse [" + rootPath + "]", e);
            }
        }
        LOGGER.debug("Using root [" + rootPath + "] for FTP Service");
        return new FTPUserManagerRepository(dao, pe, rootPath, serverConfig);
    }
}
