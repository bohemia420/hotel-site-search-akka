package com.agoda.hotels;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by sumit on 21/03/17.
 */

/*
    This Class maintains the Console Application, and is implemented as a Singleton Class
    The Thread Safety is ensured via Akka Concurrency (ActorSystem)
 */
public class HotelsRepository {

    /*
        Variables to help Sessions and Session Roles
     */

    /*
        No exit from the application thus far
     */
    static boolean isLiveConsole = true;
    /*
        We have either an admin, or a user session running currently
     */
    static boolean isYetToLogin = true;

    /*
        Hotels Feature identifiers mapped to Feature Names
        Need to rely on a Config File - Keeping in-memory for simplicity

        Hard-coded features - identified by keys 1,2,3 and 4 resp.
     */
    static final HashMap<Integer, String> FEATURES_MAP = new HashMap<Integer, String>() {
        {
            put(1, "PRIVATE_BEACH");
            put(2, "FINE_DINING_RESTAURANT");
            put(3, "SWIMMING_POOL");
            put(4, "GOLF_COURSE");
        }
    };

    /*
        Identifiers or various session roles - admin, user
     */
    static final String ADMIN_ROLE_STR = "ADMIN";
    static final String USER_ROLE_STR = "USER";

    /*
        auxiliary conventional vars

        NUM_LINES : No. of Lines left blank - convention for CLI display / presentation
        SENTINEL : The default value to begin with
     */
    static final int NUM_LINES = 3;
    static final int SENTINEL = -1;

    /*
        Our Actor System - please be aware that we are using Akka Concurrency Framework for all requests - C, R, U, D
     */
    static final ActorSystem hotelsActorSystem = ActorSystem.create("hotels-repository-actors-system");
    /*
        Our One Final Listener - Various Worker Actors tell/publish messages to the Listener
     */
    static final ActorRef hotelsListener = hotelsActorSystem.actorOf( new Props( HotelsListener.class ), "hotels-listener" );

    /*
        Singleton One Time Instantiation
     */
    private static HotelsRepository repository = new HotelsRepository( );
    /*
        Private Constructor
     */
    private HotelsRepository() { }

    /*
        Retrieving the Singleton Instance
     */
    public static HotelsRepository getInstance( ) {
        return repository;
    }

    /*
        The method invoked on Hand-over from Driver Class main method
     */
    protected static void startApplication() {
        /*
            Checks how long a session, and an app is Live for - stays interactive to serve C, R, U, D on Hotels Repo
         */
        while(isLiveConsole && isYetToLogin) {
            /*
                General Display Method - Welcoming Sessions
             */
            welcomeUsers();
            /*
                Log in - as Admin (C, U, D ops) / Users (R ops)
             */
            createSession();
        }
    }

    protected static void welcomeUsers(){
        /*
            Displays General Welcome Message / Instructions - hardcoded identifiers(for actions)
         */
        System.out.println("*************************************************************");
        System.out.println("**** Welcome to the Agoda.com - Online Hotels Repository ****");
        System.out.println("*************************************************************");

        for (int i = 0; i < NUM_LINES; i++) System.out.println();

        System.out.println("Hey There, Let\'s get started ...");
        System.out.println();

        System.out.println("To exit the application, Input \"0\"");
        System.out.println("To log in as an Admin, Input \"1\"");
        System.out.println("To log in as a User, Input \"2\"");

        for (int i = 0; i < NUM_LINES; i++) System.out.println();
    }

    protected static void createSession() {
        /*
            Logs in as an admin / user, based on the input keyed in
         */

        /*
            Get choice for session / exit
         */
        int sessionChoice = getSessionChoice();

        /*
            Admin / User sessions, supported as a Factory Instance - SessionFactory
         */
        SessionFactory sessionFactory = new SessionFactory();

        /*
            Session Logic :
            case 0 : Exit Application - isYetToLogin up, isLiveConsole down
            case 1 : Logs in As Admin - isYetToLogin up, hand-over to an Admin Class Object (via Factory)
            case 2 : Logs in As User - isYetToLogin up, hand-over to a User Class Object (via Factory
         */
        switch (sessionChoice) {
            case 0 : isYetToLogin = true; isLiveConsole = false;
                System.out.println("\nYou have successfully exited from the Application.\n");
                break;
            case 1 : isYetToLogin = false;
                Role adminRights = sessionFactory.getRole(ADMIN_ROLE_STR);
                adminRights.callDisplay();
                break;
            case 2 : isYetToLogin = false;
                Role userRights = sessionFactory.getRole(USER_ROLE_STR);
                userRights.callDisplay();
                break;
        }
    }

    /*
        Getting Session Input Choice
        Logic :
            Session Choice must be a numeric (Integerizable) String
            Session Choice must be between 0 - 2 inclusive, hardcoded
     */
    protected static int getSessionChoice() {
        Scanner sessionRoll = new Scanner(System.in);
        int sessionChoice = SENTINEL;
        boolean isValidUserChoice = false;
        while(!isValidUserChoice){
            String sessionChoiceStr = sessionRoll.nextLine();
            try{
                sessionChoice = Integer.parseInt(sessionChoiceStr);
                if(sessionChoice >= 0 && sessionChoice <= 2) isValidUserChoice = true;
                else System.out.println("Please input numeric value among 0,1 or 2.");
            } catch(NumberFormatException nfe) {
                System.out.println("The Input is not numeric, please enter a Valid Number i.e 0,1 or 2.");
            }
        }
        //sessionRoll.close();
        return sessionChoice;
    }
}
