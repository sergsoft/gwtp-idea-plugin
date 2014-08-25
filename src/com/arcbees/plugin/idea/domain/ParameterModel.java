package com.arcbees.plugin.idea.domain;

import com.arcbees.plugin.idea.utils.IdUtils;

/**
 * Created by serg on 21.08.2014.
 */
public class ParameterModel {

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = IdUtils.capitalizeFirst(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
