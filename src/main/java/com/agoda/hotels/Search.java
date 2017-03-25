package com.agoda.hotels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by sumit on 22/03/17.
 */
public class Search {
    private String termsQuery;
    private String countryQuery;
    private String cityQuery;
    private int numOfStarsQuery;
    private ArrayList<String> listOfFeaturesQuery;

    /*
        indices are hashed here, thereby indices which specify all valid filters are printed
     */
    private HashMap<Integer, Integer> retrievalsMap;
    /*
        the counter on number of valid search filters
     */
    private int validSearchFilters;

    public Search(String termsQuery, String countryQuery, String cityQuery,
                  int numOfStarsQuery, ArrayList<String> listOfFeaturesQuery) {

        this.termsQuery = termsQuery;
        this.countryQuery = countryQuery;
        this.cityQuery = cityQuery;
        this.numOfStarsQuery = numOfStarsQuery;
        this.listOfFeaturesQuery = listOfFeaturesQuery;

        this.retrievalsMap = new HashMap<Integer, Integer>();
        this.validSearchFilters = 0;
    }

    public void doSearch() {
        validSearchFilters = 0;
        if(termsQuery.length() > 0){
            validSearchFilters += 1;
            populateTermQueryRetrievals();
        }

        if(countryQuery.length() > 0){
            validSearchFilters += 1;
            if(cityQuery.length() > 0) populateCityTermQueryRetrievals();
            else populateCountryTermQueryRetrievals();
        }
        else{
            validSearchFilters += 1;
            if(cityQuery.length() > 0) populateCityTermQueryRetrievals();
        }

        if(numOfStarsQuery >= 1 && numOfStarsQuery <= 5) {
            validSearchFilters += 1;
            populateStarRankingRetrievals();
        }
        if(listOfFeaturesQuery.size() > 0){
            validSearchFilters += 1;
            populateFeaturesQueryRetrievals();
        }

        ArrayList<Integer> indicesOfRetrievals = retrieveIndices();
        printSearchResults(indicesOfRetrievals);

    }

    private void printSearchResults(ArrayList<Integer> indicesOfRetrievals) {
        Repository repo = new Repository();
        for(int id : indicesOfRetrievals) {
            System.out.println("\nPrinting Search Result : id == "+id);
            HotelWrapper hotelWrapper = repo.getHotelRepoList().get(id);
            hotelWrapper.getHotelInstance().displayHotelInstance();
            System.out.println();
        }
        System.out.println();
    }

    private ArrayList<Integer> retrieveIndices() {
        ArrayList<Integer> listOfIndices = new ArrayList<Integer>();

        Iterator mit = retrievalsMap.entrySet().iterator();
        while(mit.hasNext()){
            Map.Entry<Integer,Integer> pair = (Map.Entry)mit.next();
            int val = pair.getValue();
            if(val == validSearchFilters){
                listOfIndices.add(pair.getKey());
            }
        }
        return listOfIndices;
    }

    void populateTermQueryRetrievals() {
        ArrayList<Integer> retrievals = new ArrayList<Integer>();

        Repository repo = new Repository();
        String[] terms = termsQuery.split(",");
        for(String term : terms) {
            term = term.toLowerCase().trim();
            if(repo.getInvertedIndexMap().containsKey(term)) retrievals.addAll(repo.getInvertedIndexMap().get(term));
        }
        Set<Integer> retrievalsSet = new HashSet<Integer>(retrievals);
        for(Integer id: retrievalsSet) {
            if (repo.getHotelRepoList().get(id).getDeletedStatus()) {
                retrievalsSet.remove(id);
            }
        }

        for(int id : retrievals) {
            if(retrievalsMap.containsKey(id)) retrievalsMap.put(id, 1 + retrievalsMap.get(id));
            else retrievalsMap.put(id, 1);
        }
    }

    void populateCityTermQueryRetrievals() {
        ArrayList<Integer> retrievals = new ArrayList<Integer>();
        Repository repo = new Repository();

        String countryName = repo.getGeoMappingHash().get(cityQuery);
        for(Integer id : repo.getGeoWiseMap().get(countryName)) {
            if((repo.getHotelRepoList().get(id).getDeletedStatus() == false) &&
                    (repo.getHotelRepoList().get(id).getHotelInstance().getHotelCityName().equalsIgnoreCase(cityQuery))){
                retrievals.add(id);
            }
        }

        for(int id : retrievals) {
            if(retrievalsMap.containsKey(id)) retrievalsMap.put(id, 1 + retrievalsMap.get(id));
            else retrievalsMap.put(id, 1);
        }
    }

    void populateCountryTermQueryRetrievals() {
        ArrayList<Integer> retrievals = new ArrayList<Integer>();
        Repository repo = new Repository();

        for(int id : repo.getGeoWiseMap().get(countryQuery)){
            if(repo.getHotelRepoList().get(id).getDeletedStatus() == false){
                retrievals.add(id);
            }
        }

        for(int id : retrievals) {
            if(retrievalsMap.containsKey(id)) retrievalsMap.put(id, 1 + retrievalsMap.get(id));
            else retrievalsMap.put(id, 1);
        }
    }

    void populateStarRankingRetrievals() {
        ArrayList<Integer> retrievals = new ArrayList<Integer>();
        Repository repo = new Repository();

        for(int id : repo.getRatingWiseMap().get(numOfStarsQuery)){
            if(repo.getHotelRepoList().get(id).getDeletedStatus() == false){
                retrievals.add(id);
            }
        }

        for(int id : retrievals) {
            if(retrievalsMap.containsKey(id)) retrievalsMap.put(id, 1 + retrievalsMap.get(id));
            else retrievalsMap.put(id, 1);
        }
    }

    void populateFeaturesQueryRetrievals() {
        ArrayList<Integer> retrievals = new ArrayList<Integer>();
        Repository repo = new Repository();

        for(String feature : listOfFeaturesQuery) {
            if(repo.getFeaturesHashMap().containsKey(feature)) retrievals.addAll(repo.getFeaturesHashMap().get(feature));
        }
        Set<Integer> retrievalsSet = new HashSet<Integer>(retrievals);

        for(Integer id: retrievalsSet) {
            if (repo.getHotelRepoList().get(id).getDeletedStatus()) {
                retrievalsSet.remove(id);
            }
        }

        for(int id : retrievals) {
            if(retrievalsMap.containsKey(id)) retrievalsMap.put(id, 1 + retrievalsMap.get(id));
            else retrievalsMap.put(id, 1);
        }
    }
}
