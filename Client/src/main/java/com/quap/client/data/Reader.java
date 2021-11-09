package com.quap.client.data;

public abstract class Reader {

    public abstract void insert(Object... args);

    public abstract boolean update(Object... args);

    public abstract Object getSpecific(Object regex);

    public abstract Object getAll();

    public abstract void create();

}
