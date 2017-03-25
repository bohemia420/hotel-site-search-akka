package com.agoda.hotels;

import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sumit on 21/03/17.
 */

    /*
        The Various Data Structures for Our Hotel Repository
        - One Fat ArrayList of Hotels : Actual Hotel Instances, Random Access
        - One Hash Table on Ratings i.e 1 - 5 Stars : ArrayList of Hotel Instances
        - One Hash Table on Geo i.e Country : ArrayList of Hotel Instances
        - One Hash Map for City <-to-> Country Mapping : 1 - 1 Mapping
        - One Inverted Index on Terms (Hotel Names)
        - One Hash Table on Hotel Features
     */

public class Repository {
    private static ArrayList<HotelWrapper> hotelRepoList = new ArrayList<HotelWrapper>();

    private static HashMap<Integer, ArrayList<Integer>> ratingWiseMap = new HashMap<Integer, ArrayList<Integer>>();
    private static HashMap<String, ArrayList<Integer>> geoWiseMap = new HashMap<String, ArrayList<Integer>>();

    private static HashMap<String, String> geoMappingHash = new HashMap<String, String>();

    private static HashMap<String, ArrayList<Integer>> invertedIndexMap = new HashMap<String, ArrayList<Integer>>();
    private static HashMap<String, ArrayList<Integer>> featuresHashMap = new HashMap<String, ArrayList<Integer>>();

    public ArrayList<HotelWrapper> getHotelRepoList() {
        return hotelRepoList;
    }

    public HashMap<Integer, ArrayList<Integer>> getRatingWiseMap() {
        return ratingWiseMap;
    }

    public HashMap<String, ArrayList<Integer>> getGeoWiseMap() {
        return geoWiseMap;
    }

    public HashMap<String, String> getGeoMappingHash() {
        return geoMappingHash;
    }

    public HashMap<String, ArrayList<Integer>> getInvertedIndexMap() {
        return invertedIndexMap;
    }

    public HashMap<String, ArrayList<Integer>> getFeaturesHashMap() {
        return featuresHashMap;
    }

    public void setHotelRepoList(ArrayList<HotelWrapper> hotelRepoList) {
        this.hotelRepoList = hotelRepoList;
    }

    public void setRatingWiseMap(HashMap<Integer, ArrayList<Integer>> ratingWiseMap) {
        this.ratingWiseMap = ratingWiseMap;
    }

    public void setGeoWiseMap(HashMap<String, ArrayList<Integer>> geoWiseMap) {
        this.geoWiseMap = geoWiseMap;
    }

    public void setGeoMappingHash(HashMap<String, String> geoMappingHash) {
        this.geoMappingHash = geoMappingHash;
    }

    public void setInvertedIndexMap(HashMap<String, ArrayList<Integer>> invertedIndexMap) {
        this.invertedIndexMap = invertedIndexMap;
    }

    public void setFeaturesHashMap(HashMap<String, ArrayList<Integer>> featuresHashMap) {
        this.featuresHashMap = featuresHashMap;
    }
    /*
        Repository static var display - needed for debugging
     */
    public void printHotelRepoList() {
        for(HotelWrapper hotelWrapper : hotelRepoList) {
            System.out.println("************ Printing a Hotel Instance **********");
            System.out.println("Id : "+hotelWrapper.getId());
            System.out.println("Is deleted : "+hotelWrapper.getDeletedStatus());
            hotelWrapper.getHotelInstance().displayHotelInstance();
        }
    }

    public void printRatingWiseMap() {
        Iterator mit = ratingWiseMap.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<Integer,ArrayList<Integer>> pair = (Map.Entry)mit.next();
            ArrayList<Integer> val = pair.getValue();
            System.out.println(pair.getKey());
            for(int id : val) {
                System.out.println(id);
            }
        }
    }

    public void printGeoWiseMap() {
        Iterator mit = geoWiseMap.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<String,ArrayList<Integer>> pair = (Map.Entry)mit.next();
            ArrayList<Integer> val = pair.getValue();
            System.out.println(pair.getKey());
            for(int id : val) {
                System.out.println(id);
            }
        }
    }

    public void printInvertedIndexMap() {
        Iterator mit = invertedIndexMap.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<String,ArrayList<Integer>> pair = (Map.Entry)mit.next();
            ArrayList<Integer> val = pair.getValue();
            System.out.println(pair.getKey());
            for(int id : val) {
                System.out.println(id);
            }
        }
    }

    public void printFeaturesHashMap() {
        Iterator mit = featuresHashMap.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<String,ArrayList<Integer>> pair = (Map.Entry)mit.next();
            ArrayList<Integer> val = pair.getValue();
            System.out.println(pair.getKey());
            for(int id : val) {
                System.out.println(id);
            }
        }
    }

    public void printGeoMappingHash() {
        Iterator mit = geoMappingHash.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<String,String> pair = (Map.Entry)mit.next();
            String val = pair.getValue();
            System.out.println(pair.getKey());
            System.out.println(val);
        }
    }

}
