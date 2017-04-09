package com.jjurm.projects.mpp.map;

import java.util.Date;

import com.jjurm.projects.mpp.model.Attendant;

/**
 * A productivity map based on one attendant's attributes.
 * 
 * @author JJurM
 */
public abstract class AttendantProductivityMap implements ProductivityMap {

  protected Date date;
  protected Attendant attendant;

  public AttendantProductivityMap(Date date, Attendant attendant) {
    this.date = date;
    this.attendant = attendant;
  }

}
