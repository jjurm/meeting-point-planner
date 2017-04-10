package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;

public class ProductivityMapsFactory {

  // @formatter:off
  private static Factory[] factories = {
      AltitudeMap::new,
      DistanceMap::new,
      //IsHomeMap::new,
      JetLagMap::new,
  };
  // @formatter:on

  public static ProductivityMap[] produce(Date date, Attendant attendant) {
    ProductivityMap[] maps = new ProductivityMap[factories.length];
    for (int i = 0; i < factories.length; i++) {
      maps[i] = factories[i].construct(date, attendant);
    }
    return maps;
  }

  static interface Factory {

    public ProductivityMap construct(Date date, Attendant attendant);

  }

}
