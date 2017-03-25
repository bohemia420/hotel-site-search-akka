package com.agoda.hotels;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sumit on 21/03/17.
 */

/*
    Session Factory Instance - User implements Role Interface
    Overrides methods to honour the session as a User Session
 */
public class User implements Role {
    /*
        While Session as User is Live
        Get user inputs - to log out, create, update, delete a hotel instance, or display the repo
     */
    public void callDisplay(){
        System.out.println("Congratulations! You could log in successfully as a User.");
        while (!HotelsRepository.isYetToLogin) {
            /*
                General display for User Choice Instructions - hard coded to only serve 2 ops
            */
            displayInfoHeader();

            /*
                get admin input for Search ops / log out
             */
            int userChoice = getRoleAction();
            switch (userChoice) {
                case 0 : logout();
                    break;
                case 1 : searchBuilder();
                    break;
            }
        }
    }

    /*
        Helper method to get user input for Search ops on the Hotel Repository / log out
     */
    public int getRoleAction() {
        Scanner userRoll = new Scanner(System.in);
        int userChoice = HotelsRepository.SENTINEL;
        boolean isValidUserChoice = false;
        while(!isValidUserChoice) {
            String userChoiceStr = userRoll.nextLine();
            try {
                userChoice = Integer.parseInt(userChoiceStr);
                if (userChoice >= 0 && userChoice <= 1) isValidUserChoice = true;
                else System.out.println("\nPlease input numeric value, 0 or 1\n");
            } catch (NumberFormatException nfe) {
                System.out.println("The Input is not numeric, please enter a Valid Number i.e 0 or 1");
            }
        }
        return userChoice;
    }

    /*
        General display for User Choice Instructions - hard coded to only serve 5 ops
    */
    public void displayInfoHeader() {
        System.out.println("\nDear User, You may choose from amongst the following Operations...\n");
        System.out.println("In Order to, Log out as User, Input \"0\"");
        System.out.println("In Order to, Search, Input \"1\" : ");
    }

    /*
        Logging off the session - Role Interface Methods
     */
    public void logout(){
        HotelsRepository.isYetToLogin = true;
        System.out.println("\nYou have logged out successfully as a user\n");
    }

    /*
        Helper functions to get Features List for Hotel Instance Creation
     */
    private static ArrayList<String> getExtraFeaturesList() {
        Scanner searchRoll = new Scanner(System.in);
        String featuresQuery = Integer.toString(HotelsRepository.SENTINEL);

        boolean isValidFeatureString = false;
        ArrayList<String> listOfFeatures = new ArrayList<String>();
        while(!isValidFeatureString) {
            featuresQuery = searchRoll.nextLine();
            if(featuresQuery.length() == 0)isValidFeatureString = true;
            else if(featuresQuery.matches("^(\\d+(,\\d+){0,3})?$")) {
                String[] features = featuresQuery.split(",");
                try {
                    boolean invalidFeaturesFlag = false;
                    for (String f : features) {
                        int fval = Integer.parseInt(f);
                        if (isInvalidFeature(fval)) {
                            invalidFeaturesFlag = true;
                            System.out.println("\nOne of the input feature : " + fval + " isn\'t valid");
                            System.out.println("Please input a valid, comma separated, numeric string Only\n");
                            break;
                        }
                        else {
                            listOfFeatures.add(getFeatureName(fval));
                        }
                    }
                    if(!invalidFeaturesFlag){
                        isValidFeatureString = true;
                    }
                    else listOfFeatures = new ArrayList<String>();
                }
                catch (Exception e) {
                    System.out.println("\nOne of the input feature : is not a numeric");
                    System.out.println("Please input a valid, comma separated, numeric string Only\n");
                    break;
                }
            }
            else{
                System.out.println("\nPlease Input a Valid, Comma Separated Numeric Strings e.g 1,2\n");
            }
        }
        //searchRoll.close();
        return listOfFeatures;
    }

    /*
        A display of the features supported, and input instructions
     */
    public static void displayExtraFeatures() {
        System.out.println("\nPlease Enter, Comma Separated, any other features for the Hotel, as follows :\n");
        System.out.println("For Private Beach, enter \"1\"");
        System.out.println("For Restaurant, enter \"2\"");
        System.out.println("For Swimming Pool, enter \"3\"");
        System.out.println("For Golf Course, enter \"4\"");
        System.out.println("E.g, For Swimming Pool and Restaurant, enter : 2,3\n");
    }

    /*
        Helper Methods to fetch Feature Names identified as ids
     */
    public static boolean isInvalidFeature(int fval) {
        if(fval >= 1 && fval <= 4) return false;
        return true;
    }

    /*
        Only 5 features supported to begin with - hardcoded as a Final Map in HotelRepository Singleton Class
     */
    public static String getFeatureName(int fval) {
        return HotelsRepository.FEATURES_MAP.get(fval);
    }

    /*
        Search - Builder Method : Comprises of sub queries as filters (by name, star ranking, city, country, features)
     */
    private static void searchBuilder(){
        System.out.println("\nGreat! Let\'s get started with building and refining our search ...\n");
        /*
            Build Search Query based on Input
            - build terms query (match in Hotel Names)
            - build geo queries (match with country / city)
            - build star ranking queries
            - build features list queries
         */
        String termsQuery = buildTermsQuery();
        String countryQuery = buildCountryQuery();
        String cityQuery = buildCityQuery();
        int numOfStarsQuery = buildStarRankingQuery();
        ArrayList<String> listOfFeaturesQuery = buildFeaturesQuery();

        /*
            instantiating a search query object and performing search
         */
        Search searchQueryObj = new Search(termsQuery, countryQuery, cityQuery, numOfStarsQuery, listOfFeaturesQuery);

        /*
            instantiating an actor for search
         */
        ActorRef searchHotelsActorRef = HotelsRepository.hotelsActorSystem.actorOf(
                new Props(SearchHotelsActor.class), "search-hotel-actor");
        /*
            performing search - shoutouts to the listener
         */
        searchHotelsActorRef.tell(searchQueryObj, null);
    }

    /*
        build terms query - matches in Hotel Names
     */
    private static String buildTermsQuery() {
        Scanner searchRoll = new Scanner(System.in);
        System.out.println("\n Please input the terms, comma separated to search for : \n");
        System.out.println("For Instance, to search for Hilton, or Hyatt, input : Hilton, Hyatt\n");
        System.out.println("Leave blank for no term.\n");

        String termsQuery = searchRoll.nextLine();
        //searchRoll.close();
        return termsQuery;
    }

    /*
        build geo queries
        - search query with country filter
        - search query with city filter (overrides the above filter)
     */
    private static String buildCountryQuery() {
        Scanner searchRoll = new Scanner(System.in);
        System.out.println("\nGreat! Please input the Country you want to search by ... \n");
        System.out.println("Please only choose from amongst the following Countries ...");
        System.out.println("Leave blank for no country filters\n");

        for(int i = 0;i < HotelsRepository.NUM_LINES; i++) System.out.println();

        Repository repo = new Repository();
        ArrayList<String> countriesInRepo = new ArrayList<String>();
        for(String city: repo.getGeoMappingHash().keySet()){
            countriesInRepo.add(repo.getGeoMappingHash().get(city));
        }
        HashSet<String> countriesInRepoSet = new HashSet<String>(countriesInRepo);
        for(String country : countriesInRepoSet){
            System.out.println(country);
        }

        for(int i = 0;i < HotelsRepository.NUM_LINES; i++) System.out.println();

        String countryQuery = Integer.toString(HotelsRepository.SENTINEL);

        boolean isValidCountry = false;
        while(!isValidCountry) {
            countryQuery = searchRoll.nextLine();
            if((countryQuery.length() == 0) || (countriesInRepoSet.contains(countryQuery))) isValidCountry = true;
            else System.out.println("\nPlease input a valid country name\n");
        }
        //searchRoll.close();
        return countryQuery;
    }

    private static String buildCityQuery() {
        Scanner searchRoll = new Scanner(System.in);
        System.out.println("\nGreat! Please input the City you want to search by ...\n");
        System.out.println("Please only choose from amongst the following list of Cities ...");
        System.out.println("Leave blank for no city filters\n");

        for(int i = 0;i < HotelsRepository.NUM_LINES; i++) System.out.println();

        Repository repo = new Repository();
        Set<String> citiesInRepo = repo.getGeoMappingHash().keySet();

        for(String city : citiesInRepo) {
            System.out.println(city);
        }

        for(int i = 0;i < HotelsRepository.NUM_LINES; i++) System.out.println();

        String cityQuery = Integer.toString(HotelsRepository.SENTINEL);

        boolean isValidCity = false;
        while(!isValidCity) {
            cityQuery = searchRoll.nextLine();
            if((cityQuery.length() == 0) || (citiesInRepo.contains(cityQuery))) isValidCity = true;
            else System.out.println("\nPlease input a valid city name\n");
        }
        //searchRoll.close();
        return cityQuery;
    }
    /*
        building the star ranking query
     */
    private static int buildStarRankingQuery() {
        Scanner searchRoll = new Scanner(System.in);
        System.out.println("\nGreat! Please input the Star Ranking you want to filter by ...\n");
        System.out.println("Please only choose from a valid star rating, i.e 1-5 ");
        System.out.println("Leave blank for no star ranking filters\n");

        boolean isValidStarRating = false;
        int numOfStarsQuery = HotelsRepository.SENTINEL;
        while(!isValidStarRating) {
            String starRating = searchRoll.nextLine();
            try {
                if(starRating.length() == 0){
                    isValidStarRating = true;
                    numOfStarsQuery = -1;
                }
                else{
                    numOfStarsQuery = Integer.parseInt(starRating);
                    if (numOfStarsQuery >= 1 && numOfStarsQuery <= 5) isValidStarRating = true;
                    else System.out.println("Please input numeric value among 1,2,3,4 or 5");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("\nThe Input is not numeric, please enter a Valid Number i.e 1,2,3,4 or 5.\n");
            }
        }
        //searchRoll.close();
        return numOfStarsQuery;
    }

    /*
        Optional -
        build query supported with list of features as filters
     */
    private static ArrayList<String> buildFeaturesQuery() {
        displayExtraFeatures();
        return getExtraFeaturesList();
    }
}

