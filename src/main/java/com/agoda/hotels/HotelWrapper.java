package com.agoda.hotels;

/**
 * Created by sumit on 21/03/17.
 */
public class HotelWrapper {
    private int id;
    private boolean isSoftDeleted;
    private Hotel hotelInstance;

    public HotelWrapper(int id, boolean isSoftDeleted, Hotel hotelInstance) {
        this.id = id;
        this.isSoftDeleted = isSoftDeleted;
        this.hotelInstance = hotelInstance;
    }

    public int getId() {
        return id;
    }

    public boolean getDeletedStatus() {
        return isSoftDeleted;
    }

    public Hotel getHotelInstance() {
        return hotelInstance;
    }

    public void setHotelInstance(Hotel hotelInstance) {
        this.hotelInstance = hotelInstance;
    }

    public void setSoftDeleted(boolean isSoftDeleted) {
        this.isSoftDeleted = isSoftDeleted;
    }

    public void setId(int id) {
        this.id = id;
    }
}
