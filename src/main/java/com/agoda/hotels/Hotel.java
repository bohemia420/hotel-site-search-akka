package com.agoda.hotels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sumit on 21/03/17.
 */
public class Hotel implements Serializable {

    private String hotelName;
    private String hotelStreetAddress;
    private String hotelCityName;
    private String hotelCountryName;
    private String hotelContactDetails;
    private int numOfStars;
    private ArrayList<String> listOfFeatures;

    public Hotel(String hotelName, String hotelStreetAddress, String hotelCityName,
                 String hotelCountryName, String hotelContactDetails, int numOfStars, ArrayList<String> listOfFeatures) {

        this.hotelName = hotelName;
        this.hotelStreetAddress = hotelStreetAddress;
        this.hotelCityName = hotelCityName;
        this.hotelCountryName = hotelCountryName;
        this.hotelContactDetails = hotelContactDetails;
        this.numOfStars = numOfStars;
        this.listOfFeatures = listOfFeatures;
    }

    public String getHotelName(){
        return hotelName;
    };

    public String getHotelStreetAddress(){
        return hotelStreetAddress;
    };

    public String getHotelCityName(){
        return hotelCityName;
    };

    public String getHotelCountryName(){
        return hotelCountryName;
    };

    public String getHotelContactDetails(){
        return hotelContactDetails;
    };

    public int getNumOfStars(){
        return numOfStars;
    };

    public ArrayList<String> getListOfFeatures(){
        return listOfFeatures;
    }

    public void setHotelName(String hotelName){
        this.hotelName = hotelName;
    }

    public void setHotelStreetAddress(String hotelStreetAddress){
        this.hotelStreetAddress = hotelStreetAddress;
    }

    public void setHotelCityName(String hotelCityName){
        this.hotelCityName = hotelCityName;
    }

    public void setHotelCountryName(String hotelCountryName){
        this.hotelCountryName = hotelCountryName;
    }

    public void setHotelContactDetails(String hotelContactDetails){
        this.hotelContactDetails = hotelContactDetails;
    }

    public void setNumOfStars(int numOfStars){
        this.numOfStars = numOfStars;
    }

    public void setListOfFeatures(ArrayList<String> listOfFeatures){
        this.listOfFeatures = listOfFeatures;
    }

    public void displayHotelInstance() {
        System.out.println("Hotel Name : "+getHotelName());
        System.out.println("Street Address : "+getHotelStreetAddress());
        System.out.println("City Name : "+getHotelCityName());
        System.out.println("Country Name : "+getHotelCountryName());
        System.out.println("Contact Details : "+getHotelContactDetails());
        System.out.println("Star Ratings : "+getNumOfStars()+" stars.");
        System.out.println("Extra Features in the Hotel : "+Arrays.toString(getListOfFeatures().toArray()));
    }
}
