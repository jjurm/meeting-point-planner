package com.jjurm.projects.mpp.map;

import com.jjurm.projects.mpp.model.Attendant;

/**
 * A productivity map based on one attendant's attributes.
 * 
 * @author JJurM
 */
public abstract class AttendantProductivityMap implements ProductivityMap {

  protected Attendant attendant;

  public AttendantProductivityMap(Attendant attendant) {
    this.attendant = attendant;
  }

}
