package com.infamous.zod.base.common.service;

public interface HttpRestClientManagement {

    <T> T newRestClient(String serviceName, Class<T> restClientInterface);
}
