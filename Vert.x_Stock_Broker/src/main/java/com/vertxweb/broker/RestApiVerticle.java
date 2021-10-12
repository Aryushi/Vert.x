package com.vertxweb.broker;

import com.vertxweb.broker.assets.AssetsRestApi;
import com.vertxweb.broker.quotes.QuotesRestApi;
import com.vertxweb.broker.watchList.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(RestApiVerticle.class);


  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    StartHttpServerAndAttachRoutes(startPromise);
  }
  private void StartHttpServerAndAttachRoutes(Promise<Void> startPromise) {
    final Router restApi = Router.router(vertx);
    restApi.route()
      .handler(BodyHandler.create()
        .setBodyLimit(1024) // bydeafult there is no limit
        .setHandleFileUploads(true)
      ) // why bodyhandler? to be able to pass rigorous bodies to server side ,bodyhandler needs to be registered , this handler is called before any other handler is called and hence in vertx multiple handler can be chained together => when http request reches our web server,the body handler will be called ,if the request body is available the body will be added to the request context and after that handler forward it to the next handler. If there is no next handler the requwest ends
      .failureHandler(handlefailure()); // prev line enables bodyhandle for all the routes available

    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);
    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP server error",error))
      .listen(MainVerticle.PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private Handler<RoutingContext> handlefailure() {
    return errorcontext ->{
      if(errorcontext.response().ended()){
        //ignore completd response
        return;
      }
      LOG.error("Route error:",errorcontext.failure());
      errorcontext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
    };
  }
}
