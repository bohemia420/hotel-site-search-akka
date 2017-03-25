package com.agoda.hotels;

import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sumit on 22/03/17.
 */
public class DisplayHotelActor extends UntypedActor {
    public void onReceive(Object message) {
        if(message instanceof String){
            String msg = (String) message;
            Repository repo = new Repository();
            ArrayList<HotelWrapper> hotelRepoList = repo.getHotelRepoList();

            boolean hasLiveHotelInstances = false;
            for(HotelWrapper hotelWrapper: hotelRepoList) {
                if(!hotelWrapper.getDeletedStatus()) {
                    hasLiveHotelInstances = true;
                    Hotel hotelInstance = hotelWrapper.getHotelInstance();
                    System.out.println("************************************************");
                    System.out.println("[Application Logs] Hotel Id No. : "+(1+hotelWrapper.getId()));
                    hotelInstance.displayHotelInstance();
                    System.out.println("************************************************");
                }
            }

            if(!hasLiveHotelInstances) {
                HotelsRepository.hotelsListener.tell(new String("[Application Logs] No Hotels to show"),getSelf());
            }
            getContext().stop(getSelf());
        }
        else unhandled(message);
    }
}
