package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;
import com.peertopark.java.geocalc.EarthCalc;

public class DistanceMap extends ProductivityMap {

  static double K = 6.1;

  public DistanceMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    double distance =
        EarthCalc.getHarvesineDistance(attendant.getOrigin().getPoint(), destination.getPoint());
    if (day == 1) {
      return Math.pow(10, -K * distance / 1000 / Math.pow(10, K));
    } else {
      return 1;
    }
  }
}
