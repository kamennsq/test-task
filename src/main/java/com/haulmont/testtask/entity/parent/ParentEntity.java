package com.haulmont.testtask.entity.parent;

import com.sun.javafx.beans.IDProperty;

import java.io.Serializable;

public class ParentEntity implements Serializable {
    private static final long VERSION = 1L;

    public Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
