package com.jjurm.projects.mpp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.maps.GeoApiContext;

public class ApiManager {

  static GeoApiContext context = null;

  public static GeoApiContext getContext() {
    if (context == null) {
      try (BufferedReader br = new BufferedReader(new FileReader("ApiKey.txt"))) {
        String line = br.readLine();
        System.out.println("key = " + line);
        context = new GeoApiContext().setApiKey(line);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return context;
  }

}
