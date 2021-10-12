package com.vertx.example.vertx_starter.eventbus.customcodec;

public class Pong {
private Integer id;

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }

  public Pong() {
   //default constructor
  }
  public Pong(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
