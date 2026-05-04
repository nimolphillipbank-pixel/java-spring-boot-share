package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InfoResponse(@JsonProperty("id") String id, @JsonProperty("title") String title,
                           @JsonProperty("description") String description) {

  public InfoResponse(String id, String title, String description) {
    this.id = id;
    this.title = title;
    this.description = description;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String description() {
    return description;
  }
}
