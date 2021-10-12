package com.vertxweb.broker.quotes;

import com.vertxweb.broker.assets.Asset;
import com.vertxweb.broker.assets.AssetsRestApi;
import io.vertx.ext.web.Router;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {



  public static void attach(Router parent){
    //created in memory store(mp)-> why? -> quoterestapi endpoint always return a successsful response when an asset was provided as a parameter and hence to limit the response to olny a valid asset ,an in memory store of different quotes is created
    final Map<String, Quote> cachedQuotes = new HashMap<>(); // map with key string and value quote object
    AssetsRestApi.ASSETS.forEach(symbol -> //iterating all over the ASSETS
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );
    parent.get("/quotes/:asset").handler(new GetQuoteHandler(cachedQuotes));

  }

  private static Quote initRandomQuote(final String assetParam) {
//    return Quote.builder()
//      .asset(new Asset(assetParam))
//      .volume(randomValue())
//      .ask(randomValue())
//      .bid(randomValue())
//      .lastPrice(randomValue())
//      .build();
Quote quote = new Quote(new Asset(assetParam),randomValue(),randomValue(),randomValue(),randomValue());
return quote;
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1,100));
  }
}
