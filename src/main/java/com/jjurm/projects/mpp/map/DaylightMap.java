package com.jjurm.projects.mpp.map;

import java.util.Calendar;
import java.util.Date;

import com.jjurm.projects.mpp.db.QueryCache;
import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class DaylightMap extends ProductivityMap {

  public static final double D = 100.0 / 121.0;
  public static final double WORKING_HOURS = 16;

  public static QueryCache<Integer, Double> cache = new QueryCache<Integer, Double>(
      day -> "SELECT declination FROM sundeclination WHERE `day` = " + day, rs -> rs.getDouble(1));

  public DaylightMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    double phi = destination.getPoint().getLatitude() * Math.PI / 180;

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR) + day;
    double declination = cache.getValue(dayOfYear) * Math.PI / 180;

    double h = Math.acos(-Math.tan(phi) * Math.tan(declination)) / (2 * Math.PI);
    double sunrise = 24 * -h + 12;
    double sunset = 24 * h + 12;

    double RL = Math.max(0, -sunset + sunrise + WORKING_HOURS) / WORKING_HOURS;

    double P = RL * D + (1 - RL);
    return P;
  }

}
