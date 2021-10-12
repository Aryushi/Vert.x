package com.vertxweb.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAssetsHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);

  @Override
  public void handle(final  RoutingContext context) {

      final JsonArray response = new JsonArray();
      AssetsRestApi.ASSETS.stream().map(a -> new Asset(a)).forEach(response::add); //mapping all strings to a new asset object and adding each one of them to response => each time they get the end point of assets,it's retrieved and will generate a json array response from the static list of assets
      LOG.info("Path {} responds with {}",context.normalizedPath() , response.encode());
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .putHeader("my-header","my-value")
        .end(response.toBuffer());

  }
}
