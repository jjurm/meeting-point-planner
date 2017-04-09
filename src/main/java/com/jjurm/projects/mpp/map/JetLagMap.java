package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class JetLagMap extends AttendantProductivityMap {

  static double E = 0.7;

  public JetLagMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    int offset1 = attendant.getOrigin().getTimeZone().getOffset(date.getTime());
    int offset2 = destination.getTimeZone().getOffset(date.getTime());
    double hourDiff = (offset2 - offset1) / 3600000;

    if (hourDiff == 0)
      return 0;

    boolean toEast = hourDiff > 0;
    double dayCoefficient = toEast ? 2 : 2.5;
    return 1
        - Math.pow(Math.max(0, Math.abs(hourDiff) - dayCoefficient * (day - 1)) / 12, 2) * (1 - E);
  }

}
