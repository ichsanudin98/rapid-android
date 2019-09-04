package com.ichsanudinstore.loka.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 5:53 PM
 */
public class CategoryOfficeModel extends RealmObject {
    @PrimaryKey
    private Long id;
    private String name;

    public CategoryOfficeModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
