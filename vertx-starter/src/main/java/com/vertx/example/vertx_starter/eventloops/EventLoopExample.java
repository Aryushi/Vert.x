package com.vertx.example.vertx_starter.eventloops;

import io.vertx.core.*;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500)
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
    );
    vertx.deployVerticle(EventLoopExample.class.getName(),new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("start" +getClass().getName());
    startPromise.complete();
    Thread.sleep(5000);
  }
}
