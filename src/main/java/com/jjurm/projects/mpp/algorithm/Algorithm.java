package com.jjurm.projects.mpp.algorithm;

import java.util.Date;
import java.util.TreeSet;

import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public abstract class Algorithm {

  protected int resultCount;

  public Algorithm(int resultCount) {
    this.resultCount = resultCount;
  }

  public abstract TreeSet<Result> find(Date date, Attendant[] attendants);

  public static class Result {

    private double productivitySum;
    private Place destination;

    public Result(double productivitySum, Place destination) {
      super();
      this.productivitySum = productivitySum;
      this.destination = destination;
    }

    public double getProductivitySum() {
      return productivitySum;
    }

    public Place getDestination() {
      return destination;
    }

    @Override
    public String toString() {
      return destination + " (" + productivitySum + ")";
    }

  }

}
