package com.infamous.zod.base.jpa;

import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

public class JtaTransactionManagementConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean(TransactionManager.class)
    public UserTransactionManager createAtomikosTransactionManager() throws Exception {
        UserTransactionManager manager = new UserTransactionManager();
        manager.setStartupTransactionService(false);
        manager.setForceShutdown(true);
        manager.setTransactionTimeout(300000);
        return manager;
    }

    @Bean
    public JtaTransactionManager createTransactionManager(UserTransactionManager atomikosTransactionManager) {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(atomikosTransactionManager);
        transactionManager.setUserTransaction(atomikosTransactionManager);
        return transactionManager;
    }
}
