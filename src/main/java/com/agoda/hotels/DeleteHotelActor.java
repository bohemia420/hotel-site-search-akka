package com.agoda.hotels;

import akka.actor.UntypedActor;

/**
 * Created by sumit on 22/03/17.
 */

/*
    soft deletes only.
 */
public class DeleteHotelActor extends UntypedActor {
    public void onReceive( Object message ) {
        if(message instanceof Integer) {
            int lookUpId = (Integer) message;

            Repository repo = new Repository();

            try{
                HotelWrapper hotelWrapper = repo.getHotelRepoList().get(lookUpId);
                if(hotelWrapper.getDeletedStatus()) System.out.println("[Application Logs] the hotel id: "+(1+lookUpId)+" was already marked deleted.");
                hotelWrapper.setSoftDeleted(true);
                HotelsRepository.hotelsListener.tell(new String("[Application Logs] The id : "+(1+lookUpId)+" has been deleted"), getSelf());
            } catch (ArrayIndexOutOfBoundsException aoobe ) {
                HotelsRepository.hotelsListener.tell(new String("[Application Logs] The id : "+(1+lookUpId)+" perhaps, doesn't exist. Couldn\'t delete any"), getSelf());
            }
            getContext().stop(getSelf());
        }
        else unhandled(message);
    }
}
