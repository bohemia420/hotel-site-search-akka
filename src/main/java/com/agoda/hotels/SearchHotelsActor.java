package com.agoda.hotels;

import akka.actor.UntypedActor;

/**
 * Created by sumit on 22/03/17.
 */
public class SearchHotelsActor extends UntypedActor {
    public void onReceive( Object message ) {
        if(message instanceof Search) {
            Search searchQueryObj = (Search) message;
            searchQueryObj.doSearch();
            getContext().stop(getSelf());
        }
        else unhandled(message);
    }
}
