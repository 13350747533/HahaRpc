package com.xmj.rpc.sample.api;

import lombok.Data;

@Data
public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
