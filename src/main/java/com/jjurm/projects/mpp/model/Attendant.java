package com.jjurm.projects.mpp.model;

/**
 * A class representing a person going to participate in the meeting.
 * 
 * @author JJurM
 */
public class Attendant {

  private Place origin;

  public Attendant(Place origin) {
    this.origin = origin;
  }

  public Place getOrigin() {
    return origin;
  }

}
