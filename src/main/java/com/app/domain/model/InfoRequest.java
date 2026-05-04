package com.app.domain.model;

import com.app.infrastrucuture.entity.Info;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class InfoRequest {

    @NotNull
    @JsonProperty("title")
    private String title;

    @NotNull
    @JsonProperty("description")
    private String description;


    public Info convert(String id) {
        Info info = new Info();
        info.setId(id);
        info.setTitle(this.title);
        info.setDescription(this.description);
        return info;
    }
}
