package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;
import com.peertopark.java.geocalc.EarthCalc;

public class DistanceMap extends ProductivityMap {

  public static final double PY = 0.9;
  public static final double PE = 0.7;

  public static final double AGE_YOUNG = 18;
  public static final double AGE_ELDERLY = 80;
  public static final double MAX_DISTANCE = 20_000_000;

  private double PM;

  public DistanceMap(Date date, Attendant attendant) {
    super(date, attendant);
    PM = PY - (attendant.getAge() - AGE_YOUNG) / (AGE_ELDERLY - AGE_YOUNG) * (PY - PE);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    if (day > 1)
      return 1;

    double distance =
        EarthCalc.getHarvesineDistance(attendant.getOrigin().getPoint(), destination.getPoint());

    return Math.exp(distance * Math.log(PM) / MAX_DISTANCE);
  }
}
