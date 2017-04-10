package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class AltitudeMap extends ProductivityMap {

  public static final double K = 13;

  public static final double T0 = 288.15;
  public static final double L = 0.0065;
  public static final double p0 = 101325;
  public static final double g = 9.80665;
  public static final double M = 0.0289644;
  public static final double R = 8.31447;

  public AltitudeMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    int h = destination.getAltitude();
    return K * M * p0 * Math.pow(1 - (L * h) / T0, (g * M) / (R * L)) / (R * (T0 - L * h));
  }

}
