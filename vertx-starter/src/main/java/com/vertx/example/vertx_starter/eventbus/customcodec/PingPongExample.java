package com.vertx.example.vertx_starter.eventbus.customcodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {

private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);
  public static void main(String[] args) {

    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), LogOnHandler());
    vertx.deployVerticle(new PongVerticle(), LogOnHandler());
  }

  private static Handler<AsyncResult<String>> LogOnHandler() {
    return ar -> {
      if (ar.failed()) {
        LOG.error("err", ar.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle{
    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);
    static final String ADDRESS = PingVerticle.class.getName();
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();

      startPromise.complete();
      final Ping message = new Ping("Hello",true);
      LOG.debug("sending: {}",message);
      //Register only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(ADDRESS,message,reply ->{
        if(reply.failed()){
          LOG.debug("Failed",reply.cause());
          return;
        }
         LOG.debug("Response: {}", reply.result().body());
      });
    }
  }

  static class PongVerticle extends AbstractVerticle{
    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      startPromise.complete();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message-> {
        LOG.debug("Received Message: {}",message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error ->{
        LOG.error("Error",error);
      });
    }
  }
}
