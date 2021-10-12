package com.vertxweb.broker.quotes;

import com.vertxweb.broker.assets.Asset;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;

public class Quote {

  Asset asset;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
  BigDecimal volume;

    public Quote() {

    }


  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public BigDecimal getBid() {
    return bid;
  }

  public void setBid(BigDecimal bid) {
    this.bid = bid;
  }

  public BigDecimal getAsk() {
    return ask;
  }

  public void setAsk(BigDecimal ask) {
    this.ask = ask;
  }

  public BigDecimal getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(BigDecimal lastPrice) {
    this.lastPrice = lastPrice;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  public Quote(Asset asset, BigDecimal bid, BigDecimal ask, BigDecimal lastPrice, BigDecimal volume) {
    this.asset = asset;
    this.bid = bid;
    this.ask = ask;
    this.lastPrice = lastPrice;
    this.volume = volume;
  }
  public JsonObject toJsonObject(){
      return JsonObject.mapFrom(this);
  } // converts quote object to json object
}
