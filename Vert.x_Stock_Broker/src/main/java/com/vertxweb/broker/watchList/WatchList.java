package com.vertxweb.broker.watchList;

import com.vertxweb.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  List<Asset> assets;

  public  JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  } // maps watchlist to vertx json object
}
