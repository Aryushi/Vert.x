package com.vertxweb.broker.assets;

import io.vertx.ext.web.Router;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {

//  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);
  public static final List<String> ASSETS = Arrays.asList("AAPL","AMZN","FB","GOOG","MSFT","NFLX","TSLA"); // creating an in memory asset list

  public static void attach(Router parent){
    parent.get("/assets").handler(new GetAssetsHandler());
  }
}
