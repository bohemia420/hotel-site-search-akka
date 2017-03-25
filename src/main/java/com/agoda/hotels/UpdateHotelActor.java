package com.agoda.hotels;

import akka.actor.UntypedActor;

import java.util.ArrayList;

/**
 * Created by sumit on 22/03/17.
 */
public class UpdateHotelActor extends UntypedActor {
    public void onReceive( Object message ) {
        if(message instanceof HotelWrapper){
            HotelWrapper hotelWrapper = (HotelWrapper) message;
            Hotel hotelInstance = hotelWrapper.getHotelInstance();

            Repository repo = new Repository();
            ArrayList<HotelWrapper> hotelRepoList = repo.getHotelRepoList();

            HotelWrapper hotelWrapperInRepository = hotelRepoList.get(hotelWrapper.getId());
            Hotel hotelInstanceInRepository = hotelWrapperInRepository.getHotelInstance();

            /*
                Getters and Setters - Applied based on the Update was a Valid Data Type / Input
             */
            if(isValidString(hotelInstance.getHotelName())){
                hotelInstanceInRepository.setHotelName(hotelInstance.getHotelName());
            }

            if(isValidString(hotelInstance.getHotelStreetAddress())){
                hotelInstanceInRepository.setHotelStreetAddress(hotelInstance.getHotelStreetAddress());
            }

            if(isValidString(hotelInstance.getHotelCityName())){
                hotelInstanceInRepository.setHotelCityName(hotelInstance.getHotelCityName());
            }

            if(isValidString(hotelInstance.getHotelCountryName())){
                hotelInstanceInRepository.setHotelCountryName(hotelInstance.getHotelCountryName());
            }

            if(isValidString(hotelInstance.getHotelContactDetails())){
                hotelInstanceInRepository.setHotelContactDetails(hotelInstance.getHotelContactDetails());
            }

            if(isValidInteger(hotelInstance.getNumOfStars())){
                hotelInstanceInRepository.setNumOfStars(hotelInstance.getNumOfStars());
            }

            if(isValidArray(hotelInstance.getListOfFeatures())){
                hotelInstanceInRepository.setListOfFeatures(hotelInstance.getListOfFeatures());
            }

            HotelsRepository.hotelsListener.tell(new String("[Applications Logs] Hotel Instance Updated, id : "+(1+hotelWrapper.getId())),getSelf());
            getContext().stop(getSelf());

        }
    }

    private boolean isValidString(String str) {
        /*
         Just a few string length checks
         */
        if(str.length() == 0) return false;
        return true;
    }

    private boolean isValidInteger(int x) {
        /*
            looks more like isValidStarRanking
         */
        if(x >= 1 && x <= 5) return true;
        return false;
    }

    private boolean isValidArray(ArrayList<String> arr) {
        /*
        Mostly works, for array type is taken care already, before
         */
        return true;
    }
}
