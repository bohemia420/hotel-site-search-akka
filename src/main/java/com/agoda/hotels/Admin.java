package com.agoda.hotels;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by sumit on 21/03/17.
 */

/*
    Session Factory Instance - Admin implements Role Interface
    Overrides methods to honour the session as an Admin Session
 */
public class Admin implements Role {
    /*
        While Session as Admin is Live
        Get admin inputs - to log out, create, update, delete a hotel instance, or display the repo
     */
    public void callDisplay() {
        System.out.println("\nCongratulations! You could log in successfully as an Admin.\n");
        while (!HotelsRepository.isYetToLogin) {
            /*
                General display for Admin Choice Instructions - hard coded to only serve 5 ops
            */
            displayInfoHeader();
            /*
                get admin input for C, U, D ops / log out
             */
            int adminChoice = getRoleAction();
            switch (adminChoice) {
                case 0:
                    logout();
                    break;
                case 1:
                    createHotelInstance();
                    break;
                case 2:
                    updateHotelInstance();
                    break;
                case 3:
                    deleteHotelInstance();
                    break;
                case 4:
                    displayListOfHotels();
                    break;
            }
        }
    }

    /*
        Helper method to get admin input for C, U, D ops on the Hotel Repository / log out
     */
    public int getRoleAction() {
        Scanner adminRoll = new Scanner(System.in);
        int adminChoice = HotelsRepository.SENTINEL;
        boolean isValidAdminChoice = false;
        while (!isValidAdminChoice) {
            String adminChoiceStr = adminRoll.nextLine();
            try {
                adminChoice = Integer.parseInt(adminChoiceStr);
                if (adminChoice >= 0 && adminChoice <= 4) isValidAdminChoice = true;
                else System.out.println("\nPlease input numeric value among 0,1,2,3 or 4\n");
            } catch (NumberFormatException nfe) {
                System.out.println("\nThe Input is not numeric, please enter a Valid Number i.e 0,1,2,3 or 4.\n");
            }
        }
        //adminRoll.close();
        return adminChoice;
    }

    /*
        General display for Admin Choice Instructions - hard coded to only serve 5 ops
    */
    public void displayInfoHeader() {
        System.out.println("\nDear Admin, You may choose from amongst the following Operations...\n");
        System.out.println("\nIn Order to, Log out as Admin, Input 0");
        System.out.println("In Order to, Create a Hotel Instance, Input \"1\"");
        System.out.println("In Order to, Update a Hotel Instance, Input \"2\"");
        System.out.println("In Order to, Delete a Hotel Instance, Input \"3\"");
        System.out.println("In Order to, Show list of all Hotels, Input \"4\"\n");
    }

    /*
        Logging off the session - Role Interface Methos
     */
    public void logout(){
        HotelsRepository.isYetToLogin = true;
        System.out.println("\nYou have logged out successfully as an Admin\n");
    }

    /*
        Helper functions to get Features List for Hotel Instance Creation
     */
    private static ArrayList<String> getExtraFeaturesList() {
        boolean isValidFeatureString = false;
        String featureString = Integer.toString(HotelsRepository.SENTINEL);
        ArrayList<String> listOfFeatures = new ArrayList<String>();
        Scanner hotelRoll = new Scanner(System.in);
        while (!isValidFeatureString) {
            featureString = hotelRoll.nextLine();
            if (featureString.length() == 0) isValidFeatureString = true;
            else if (featureString.matches("^(\\d+(,\\d+){0,3})?$")) {
                String[] features = featureString.split(",");
                try {
                    boolean invalidFeaturesFlag = false;
                    for (String f : features) {
                        int fval = Integer.parseInt(f);
                        if (isInvalidFeature(fval)) {
                            invalidFeaturesFlag = true;
                            System.out.println("\nOne of the input feature : " + fval + " isn\'t valid");
                            System.out.println("Please input a valid, comma separated, numeric string Only\n");
                            break;
                        } else {
                            listOfFeatures.add(getFeatureName(fval));
                        }
                    }
                    if (!invalidFeaturesFlag) {
                        isValidFeatureString = true;
                    } else listOfFeatures = new ArrayList<String>();
                } catch (Exception e) {
                    System.out.println("\nOne of the input feature : is not a numeric");
                    System.out.println("Please input a valid, comma separated, numeric string Only\n");
                    break;
                }
            } else {
                System.out.println("\nPlease Input a Valid, Comma Separated Numeric Strings e.g 1,2\n");
            }
        }
        //hotelRoll.close();
        Set<String> featuresSet = new HashSet<String>(listOfFeatures);
        return new ArrayList<String>(featuresSet);
    }

    /*
        A display of the features supported, and input instructions
     */
    private static void displayExtraFeatures() {
        System.out.println("\nPlease Enter, Comma Separated, any other features for the Hotel, as follows :\n");
        System.out.println("For Private Beach, enter \"1\"");
        System.out.println("For Restaurant, enter \"2\"");
        System.out.println("For Swimming Pool, enter \"3\"");
        System.out.println("For Golf Course, enter \"4\"");
        System.out.println("E.g, For Swimming Pool and Restaurant, enter : 2,3\n");
    }

    /*
        Helper Methods to fetch Feature Names identified as ids
     */
    private static String getFeatureName(int fval) {
        return HotelsRepository.FEATURES_MAP.get(fval);
    }

    /*
        Only 5 features supported to begin with - hardcoded as a Final Map in HotelRepository Singleton Class
     */
    private static boolean isInvalidFeature(int fval) {
        if (fval >= 1 && fval <= 4) return false;
        return true;
    }

    /*
        Overloaded method
        - getting star ratings for a hotel instance, while Creation
        - Hard coded to only support ratings upto 5 stars
     */
    private static int getStarRatings() {
        boolean isValidStarRating = false;
        Scanner hotelRoll = new Scanner(System.in);
        int numOfStars = HotelsRepository.SENTINEL;
        while (!isValidStarRating) {
            String starRating = hotelRoll.nextLine();
            try {
                numOfStars = Integer.parseInt(starRating);
                if (numOfStars >= 1 && numOfStars <= 5) isValidStarRating = true;
                else System.out.println("Please input numeric value among 1,2,3,4 or 5");
            } catch (NumberFormatException nfe) {
                System.out.println("The Input is not numeric, please enter a Valid Number i.e 1,2,3,4 or 5.");
            }
        }
        //hotelRoll.close();
        return numOfStars;
    }

    /*
        Overloaded method
        - getting star ratings for a hotel instance, while Updation
        - A use case wherein the admin just might leave the field empty, i.e unchanged no. of star ratings, as before
        - Hard coded to only support ratings upto 5 stars
     */
    private static int getStarRatings(int id) {
        boolean isValidStarRating = false;
        Scanner hotelRoll = new Scanner(System.in);
        int numOfStars = HotelsRepository.SENTINEL;
        while (!isValidStarRating) {
            String starRating = hotelRoll.nextLine();
            try {
                if (starRating.length() == 0) {
                    isValidStarRating = true;
                    numOfStars = -1;
                } else {
                    numOfStars = Integer.parseInt(starRating);
                    if (numOfStars >= 1 && numOfStars <= 5) isValidStarRating = true;
                    else System.out.println("Please input numeric value among 1,2,3,4 or 5");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("The Input is not numeric, please enter a Valid Number i.e 1,2,3,4 or 5.");
            }
        }
        //hotelRoll.close();
        return numOfStars;
    }

    /*
        get hotel details from admin
        persist into the datastructures via an actor
     */
    private static void createHotelInstance() {
        System.out.println("\nPlease input the Hotel details, as follows : \n");

        /*
            Make a hotel instance with user inputs. Overloaded Methods :
            - for a new instance hotel creation - makeHotelInstance()
         */
        Hotel hotelInstance = makeHotelInstance();

        /*
            An Actor instantiated to persist the Hotel Instance above, in the Hotels Repository
         */
        ActorRef createHotelActorRef = HotelsRepository.hotelsActorSystem.actorOf(
                new Props(CreateHotelActor.class), "create-hotel-actor");

        /*
            Start the creation / persistence of the Hotel Instance
         */
        createHotelActorRef.tell(hotelInstance, null);
    }

    /*
        delete the hotel, identified by an id
     */
    private static void deleteHotelInstance() {
        Repository repo = new Repository();
        if(repo.getHotelRepoList().size() == 0){
            System.out.println("\nDelete Action Prohibited. There aren\'t any hotel instances in the repository.\n");
            return;
        }
        System.out.println("\nPlease input the id of the Hotel to be deleted : \n");
        /*
            general interactive input method helper for getting an integer id
         */
        boolean doesHotelInstanceExists = false;
        HotelWrapper hotelWrapper = null;
        int id = HotelsRepository.SENTINEL;
        int lookUpId = HotelsRepository.SENTINEL;

        ArrayList<HotelWrapper> hotelRepoList = repo.getHotelRepoList();
        while ((!doesHotelInstanceExists)) {
            id = getIdForUDops();
            lookUpId = id - 1;

            try {
                hotelWrapper = hotelRepoList.get(lookUpId);
                if (!hotelWrapper.getDeletedStatus());
                else System.out.println("\nThe hotel instance whose id you input was deleted before.\n");
                doesHotelInstanceExists = true;
            } catch (IndexOutOfBoundsException oobe) {
                System.out.println("\nThe id input for deletion : " + id + " doesn\'t exist\n");
                System.out.println("Please input a valid hotel id.\n");
            }
        }

        /*
            An Actor instantiated to (soft) delete the Hotel Instance identified above, from the Hotels Repository
         */
        ActorRef deleteHotelActorRef = HotelsRepository.hotelsActorSystem.actorOf(
                new Props(DeleteHotelActor.class), "delete-hotel-actor");

        /*
            Start the deletion of the Hotel Instance, by hotel id (lookup id is always -1 the id displayed
         */
        deleteHotelActorRef.tell(new Integer(lookUpId), null);
    }

    /*
        get hotel details from admin
        update into the datastructures via an actor
     */
    private static void updateHotelInstance() {
        Repository repo = new Repository();
        if(repo.getHotelRepoList().size() == 0){
            System.out.println("\nUpdate Action Prohibited. There aren\'t any hotel instances in the repository.\n");
            return;
        }
        System.out.println("\nPlease input the id of the Hotel to be updated : \n");

        /*
            Make a hotel instance with user inputs. Overloaded Methods :
            - for an instance hotel updation - makeHotelInstance(String updateMsg)
         */
        HotelWrapper hotelWrapper = makeHotelInstance("update");

        /*
            An Actor instantiated to update the Hotel Instance identified above, with Id
         */
        ActorRef updateHotelActorRef = HotelsRepository.hotelsActorSystem.actorOf(
                new Props(UpdateHotelActor.class), "update-hotel-actor");

        /*
            Start the updation of the Hotel Instance, by hotel id
         */
        updateHotelActorRef.tell(hotelWrapper, null);
    }

    /*
        Just a display of all the Existing(and not soft deleted hotels!) in the Hotels Repository
     */
    private static void displayListOfHotels() {
        /*
            An Actor instantiated to display the Hotel Instances, which aren't soft deleted
         */
        ActorRef displayHotelActorRef = HotelsRepository.hotelsActorSystem.actorOf(
                new Props(DisplayHotelActor.class), "display-hotels-actor");

        /*
            Start the display of the Hotel Repository
         */
        displayHotelActorRef.tell(new String("display"), null);
    }

    /*
        Get the hotel instance id for update / deletion
     */
    public static int getIdForUDops() {
        Scanner hotelRoll = new Scanner(System.in);

        int idChoice = HotelsRepository.SENTINEL;
        boolean isValidIdChoice = false;
        while (!isValidIdChoice) {
            String idChoiceStr = hotelRoll.nextLine();
            try {
                idChoice = Integer.parseInt(idChoiceStr);
                isValidIdChoice = true;
            } catch (NumberFormatException nfe) {
                System.out.println("\nThe id, of the hotel to be deleted / updated, has to be a numeric value\n");
            }
        }
        //hotelRoll.close();
        return idChoice;
    }

    /*
        Overloaded method
        - input instructions
        - hotel instance creation
        - Creation of Hotel Instance for Persistence via An Akka Actor
     */
    public static Hotel makeHotelInstance() {
        Scanner hotelRoll = new Scanner(System.in);
        /*
            get hotel name
         */
        System.out.println("Enter the Hotel Name : ");
        String hotelName = hotelRoll.nextLine();
        /*
            get hotel street address
         */
        System.out.println("Enter the Hotel Street : ");
        String hotelStreetAddress = hotelRoll.nextLine();
        /*
            get hotel city name
         */
        System.out.println("Enter the Hotel City : ");
        String hotelCityName = hotelRoll.nextLine();
        /*
            get hotel country name
         */
        System.out.println("Enter the Hotel Country : ");
        String hotelCountryName = hotelRoll.nextLine();
        /*
            get hotel contact details
         */
        System.out.println("Please Enter the Contact Details : ");
        String hotelContactDetails = hotelRoll.nextLine();
        /*
            get hotel star ranking
         */
        System.out.println("Please Enter the Star Ratings with the Hotel (1-5)");
        int numOfStars = getStarRatings();
        /*
            get hotel features
            - display features for a hotel
            - input features for the hotel instances
         */
        displayExtraFeatures();
        ArrayList<String> listOfFeatures = getExtraFeaturesList();

        Hotel hotelInstance = new Hotel(hotelName, hotelStreetAddress,
                hotelCityName, hotelCountryName, hotelContactDetails, numOfStars, listOfFeatures);

        //hotelRoll.close();
        return hotelInstance;
    }

    /*
        Overloaded method
        - check the hotel instance id being updated, is valid, and exists
        - display current hotel field state, along
        - remember we aren't doing the Setters, to update on the fly.
        - Reasons, so we take the Updation threadSafe and Concurrently using the Akka ActorSystem
     */
    public static HotelWrapper makeHotelInstance(String updateMsg) {
        Repository repo = new Repository();
        ArrayList<HotelWrapper> hotelRepoList = repo.getHotelRepoList();
        /*
            get a valid id for the hotel instance to be updated
            - id is something visible to the session
            - lookUpId is the actual index of the hotel instance
         */
        boolean doesHotelInstanceExists = false;
        HotelWrapper hotelWrapper = null;
        int id = HotelsRepository.SENTINEL;
        int lookUpId = HotelsRepository.SENTINEL;

        while ((!doesHotelInstanceExists)) {
            id = getIdForUDops();
            lookUpId = id - 1;

            System.out.println("\n[Leave Empty (Press Return) for Unchanged.] " +
                    "Please input the Hotel details for Updations, as follows : \n");
            try {
                hotelWrapper = hotelRepoList.get(lookUpId);
                if (!hotelWrapper.getDeletedStatus());
                else System.out.println("\nThe hotel instance whose id you input was deleted before.\n");
                doesHotelInstanceExists = true;
            } catch (IndexOutOfBoundsException oobe) {
                System.out.println("\nThe id input for updation : " + id + " doesn\'t exist\n");
                System.out.println("Please input a valid hotel id.\n");
            }
        }

        HotelWrapper hotelWrapperInRepository = hotelRepoList.get(lookUpId);
        Hotel hotelInstanceInRepository = hotelWrapperInRepository.getHotelInstance();
        /*
            get the updated hotel name
         */
        Scanner hotelRoll = new Scanner(System.in);
        System.out.println("\nCurrent Hotel Name : " + hotelInstanceInRepository.getHotelName());
        System.out.println("\nEnter the new Hotel Name : ");
        String hotelName = hotelRoll.nextLine();
        /*
            get the updated hotel street address
         */
        hotelRoll = new Scanner(System.in);
        System.out.println("\nCurrent Street Address : " + hotelInstanceInRepository.getHotelStreetAddress());
        System.out.println("\nEnter the new Hotel Street : ");
        String hotelStreetAddress = hotelRoll.nextLine();
        /*
            get the updated hotel city name
         */
        hotelRoll = new Scanner(System.in);
        System.out.println("\nCurrent Hotel City : " + hotelInstanceInRepository.getHotelCityName());
        System.out.println("\nEnter the new Hotel City : ");
        String hotelCityName = hotelRoll.nextLine();
        /*
            get the updated hotel country name
         */
        hotelRoll = new Scanner(System.in);
        System.out.println("\nCurrent Hotel Country : " + hotelInstanceInRepository.getHotelCountryName());
        System.out.println("\nEnter the new Hotel Country : ");
        String hotelCountryName = hotelRoll.nextLine();
        /*
            get the updated hotel contact details
         */
        hotelRoll = new Scanner(System.in);
        System.out.println("\nCurrent Hotel Contact Details : " + hotelInstanceInRepository.getHotelContactDetails());
        System.out.println("\nPlease Enter the Contact Details : ");
        String hotelContactDetails = hotelRoll.nextLine();
        /*
            get the updated hotel star ratings
         */
        System.out.println("\nCurrent Hotel Star Rating : " + hotelInstanceInRepository.getNumOfStars());
        System.out.println("\nPlease Enter the new Star Ratings with the Hotel (1-5).Leave empty for unchanged.");
        int numOfStars = getStarRatings(lookUpId);
        /*
            get the updated list of features for the hotel
            - display the list of features available for the hotels
            - input the set of features for the hotel instance being updated
         */
        System.out.println("\nCurrent Features in the Hotel : " +
                Arrays.toString(hotelInstanceInRepository.getListOfFeatures().toArray()));

        displayExtraFeatures();
        ArrayList<String> listOfFeatures = getExtraFeaturesList();

        Hotel hotelInstance = new Hotel(hotelName, hotelStreetAddress,
                hotelCityName, hotelCountryName, hotelContactDetails, numOfStars, listOfFeatures);

        //hotelRoll.close();
        return new HotelWrapper(lookUpId, false, hotelInstance);
    }
}
