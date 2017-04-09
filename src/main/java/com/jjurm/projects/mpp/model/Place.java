package com.jjurm.projects.mpp.model;

import java.util.TimeZone;

import com.peertopark.java.geocalc.Point;

public class Place {

  int id;
  private String name;
  private Point point;
  private TimeZone timeZone;

  Place(int id, String name, Point point, TimeZone timeZone) {
    this.id = id;
    this.name = name;
    this.point = point;
    this.timeZone = timeZone;
  }

  public int getId() {
    return id;
  }

  public Point getPoint() {
    return point;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Place))
      return false;
    Place p = (Place) obj;
    return id == p.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

}
