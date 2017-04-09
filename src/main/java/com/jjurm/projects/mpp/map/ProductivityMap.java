package com.jjurm.projects.mpp.map;

import com.jjurm.projects.mpp.model.Place;

/**
 * This interfaces represents a function that basically takes coordinates as an argument and returns
 * the adjusted productivity of human after travelling to the specified location, considering the
 * factor mapped by a particular implementation.
 * 
 * @author JJurM
 */
public interface ProductivityMap {

  /**
   * Returns productivity given a specified destination and a day. The 0-th day is the day of
   * arrival and the meeting days are from 1 to 3 (inclusive).
   */
  public double calculateProductivity(Place destination, int day);

}
