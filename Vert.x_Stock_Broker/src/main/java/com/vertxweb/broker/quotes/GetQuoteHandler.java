package com.vertxweb.broker.quotes;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuoteHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(GetQuoteHandler.class);

  private final Map<String, Quote> cachedQuotes;

  public GetQuoteHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }
//   public GetQuoteHandler(final Map<String, Quote> cachedQuotes){
//     this.cachedQuotes = cachedQuotes;
//   }

  @Override
  public void handle(final RoutingContext context) {
//    context -> { //asset parameter would be dynamic || when we define a handler ,we get a routing context(here context) , this routing context now contains a different parameter
      final String assetParam = context.pathParam("asset"); //context.pathParam defines asset parameter and assign it to vble assetParam
      LOG.debug("Asset Parameter: {}", assetParam);

      var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam)); // if the specified value is null, then this method returns an empty instance of the Optional class
      if (maybeQuote.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "quote for asset" + assetParam + "not available!")
            .put("Path ", context.normalizedPath())
            .toBuffer()
          );
        return;
      }
//      final Quote quote = new Quote();
      final JsonObject response = maybeQuote.get().toJsonObject();
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());

  }
}
