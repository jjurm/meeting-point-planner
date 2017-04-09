package com.jjurm.projects.mpp.model;

import com.peertopark.java.geocalc.Point;

/**
 * A class representing a person going to participate in the meeting.
 * 
 * @author JJurM
 */
public class Attendant {

  private Point origin;

  public Attendant(Point origin) {
    this.origin = origin;
  }

  public Point getOrigin() {
    return origin;
  }

}
