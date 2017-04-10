package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class JetLagMap extends ProductivityMap {

  public static final double E_EAST = 0.65;
  public static final double E_WEST = 0.75;

  public JetLagMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    int offset1 = attendant.getOrigin().getTimeZone().getOffset(date.getTime());
    int offset2 = destination.getTimeZone().getOffset(date.getTime());
    double hourDiff = (offset2 - offset1) / 3600000;

    boolean toEast = hourDiff >= 0;
    hourDiff = Math.abs(hourDiff);

    double dayCoefficient1 = toEast ? 2 : 2.5;
    double productivity1 = 1 - Math.pow(Math.max(0, hourDiff - dayCoefficient1 * (day - 1)) / 12, 2)
        * (1 - (toEast ? E_EAST : E_WEST));

    double dayCoefficient2 = !toEast ? 2 : 2.5;
    double productivity2 = 1 - Math.pow(Math.max(0, hourDiff - dayCoefficient2 * (day - 1)) / 12, 2)
        * (1 - (!toEast ? E_EAST : E_WEST));

    return Math.max(productivity1, productivity2);
  }

}
