package com.agoda.hotels;

import akka.actor.UntypedActor;

/**
 * Created by sumit on 21/03/17.
 */
public class HotelsListener extends UntypedActor {
    @Override
    public void onReceive( Object message ) throws Exception{
        if(message instanceof String) {
            String msg = (String) message;
            System.out.println(msg);
        }
        else unhandled(message);
    }
}
