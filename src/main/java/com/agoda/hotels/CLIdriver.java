package com.agoda.hotels;

/**
 * Created by sumit on 21/03/17.
 */

/*
    The Driver Class, this instantiates a Singleton "HotelRepository and hands over the execution
 */
public class CLIdriver {
    public CLIdriver() { super(); }

    public static void main(String args[]){
        /*
            Referencing the singleton instance of HotelsRepository
         */
        HotelsRepository hotelRepository = HotelsRepository.getInstance();
        /*
            Start the application - as an Interactive Console App
         */
        hotelRepository.startApplication();
    }
}
