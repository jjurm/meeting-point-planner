package com.jjurm.projects.mpp.model;

import java.util.TimeZone;

import com.peertopark.java.geocalc.Point;

public class Place {

  private Point point;
  private TimeZone timeZone;

  public Place(Point point, TimeZone timeZone) {
    this.point = point;
    this.timeZone = timeZone;
  }

  public Point getPoint() {
    return this.point;
  }

  public TimeZone getTimeZone() {
    return this.timeZone;
  }

}
