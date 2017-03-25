package com.agoda.hotels;

import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumit on 21/03/17.
 */

/*
    persists the hotel instance in the array list of hotels in the Repository
    updates the hashes and references that point to the Single Source of Truth Above.
 */
public class CreateHotelActor extends UntypedActor {
    public void onReceive( Object message ) {
        if(message instanceof Hotel){
            Hotel hotelInstance = (Hotel) message;
            Repository repo = new Repository();

            /*
                get the S S T arraylist of hotels
             */
            ArrayList<HotelWrapper> hotelRepoList = repo.getHotelRepoList();
            /*
                formulate the hotel id
             */
            int id = hotelRepoList.size();
            boolean isSoftDeleted = false;
            /*
                instantiated as a hotel wrapper
             */
            HotelWrapper hotelWrapper = new HotelWrapper(id, isSoftDeleted, hotelInstance);
            hotelRepoList.add(hotelWrapper);

            repo.setHotelRepoList(hotelRepoList);
            /*
                update the hashes & references
                 - rating wise hash
                 - country wise hash
                 - city to country mapping
                 - inverted index of terms in the hotel name
                 - inverted index of features / facilites in the hotels
             */
            int rating = hotelInstance.getNumOfStars();

            HashMap<Integer, ArrayList<Integer>> ratingWiseMap = repo.getRatingWiseMap();
            if(!ratingWiseMap.containsKey(rating)){
                ratingWiseMap.put(rating,new ArrayList<Integer>());
            }
            ArrayList<Integer> ratingArr = ratingWiseMap.get(rating);
            ratingArr.add(id);
            ratingWiseMap.put(rating,ratingArr);

            repo.setRatingWiseMap(ratingWiseMap);

            String countryName = hotelInstance.getHotelCountryName();

            HashMap<String, ArrayList<Integer>> geoWiseMap = repo.getGeoWiseMap();
            if(!geoWiseMap.containsKey(countryName)){
                geoWiseMap.put(countryName,new ArrayList<Integer>());
            }
            ArrayList<Integer> geoArr = geoWiseMap.get(countryName);
            geoArr.add(id);
            geoWiseMap.put(countryName,geoArr);

            repo.setGeoWiseMap(geoWiseMap);

            String cityName = hotelInstance.getHotelCityName();

            HashMap<String, String> geoHash = repo.getGeoMappingHash();
            if(!geoHash.containsKey(cityName)){
                geoHash.put(cityName, countryName);
            }

            repo.setGeoMappingHash(geoHash);

            String hotelName = hotelInstance.getHotelName();
            String[] terms = hotelName.split(" ");

            HashMap<String, ArrayList<Integer>> invertedIndexMap = repo.getInvertedIndexMap();

            for(String term : terms) {
                term = term.toLowerCase();
                if(!invertedIndexMap.containsKey(term)){
                    invertedIndexMap.put(term, new ArrayList<Integer>());
                }
                ArrayList<Integer> termArr = invertedIndexMap.get(term);
                termArr.add(id);
                invertedIndexMap.put(term, termArr);
            }

            repo.setInvertedIndexMap(invertedIndexMap);

            ArrayList<String> hotelFeatures = hotelInstance.getListOfFeatures();

            HashMap<String, ArrayList<Integer>> featuresHashMap = repo.getFeaturesHashMap();

            for(String feature : hotelFeatures) {
                if(!featuresHashMap.containsKey(feature)) {
                    featuresHashMap.put(feature, new ArrayList<Integer>());
                }
                ArrayList<Integer> featureArr = featuresHashMap.get(feature);
                featureArr.add(id);
                featuresHashMap.put(feature,featureArr);
            }

            repo.setFeaturesHashMap(featuresHashMap);

            HotelsRepository.hotelsListener.tell(new String("[Application Logs] Hotel Instance Created, id : "+(1+id)),getSelf());
            getContext().stop(getSelf());
        }
        else unhandled(message);
    }
}
