package com.infamous.zod.base.rest;

import java.util.Arrays;
import javax.ws.rs.core.Response;

public class PersonEndPoint implements IPersonEndPoint {

    @RestEndPoint
    @Override
    public Response fetchAll() {
        return Response.ok(Arrays.asList(new Person("Luke"),
            new Person("Sam"))).build();
    }

    @RestEndPoint
    @Override
    public Response exception() {
        throw new RuntimeException("Exception");
    }

    @Override
    public String ok() {
        return "Hello";
    }
}

class Person {

    private final String m_name;

    public Person(String name) {
        this.m_name = name;
    }
}

interface IPersonEndPoint extends BaseEndPoint {

    @RestEndPoint
    Response fetchAll();

    @RestEndPoint
    Response exception();

    String ok();
}
