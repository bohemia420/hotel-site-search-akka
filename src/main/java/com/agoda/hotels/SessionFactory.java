package com.agoda.hotels;

/**
 * Created by sumit on 21/03/17.
 */

/*
    a factory to instantiate a User, OR an Admin Session
 */
public class SessionFactory {
    public Role getRole(String roleType){
        if(roleType == null){
            return null;
        }
        if(roleType.equalsIgnoreCase(HotelsRepository.ADMIN_ROLE_STR)){
            return new Admin();
        }
        else if(roleType.equalsIgnoreCase(HotelsRepository.USER_ROLE_STR)){
            return new User();
        }
        return null;
    }
}
