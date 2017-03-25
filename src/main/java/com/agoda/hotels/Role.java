package com.agoda.hotels;

/**
 * Created by sumit on 21/03/17.
 */
public interface Role {
    /*
        method to start the session and walk through with an interactive console session inputs
     */
    void callDisplay();
    /*
        to log out and terminate the session
     */
    void logout();
    /*
        displays info for session (welcome info)
     */
    void displayInfoHeader();
    /*
        helps choose the session action e.g C, R, U, D / logout ops
     */
    int getRoleAction();
}
