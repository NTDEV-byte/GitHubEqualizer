import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Scanner;

public class Equalizer {

    public static final int VIEW_FOLLOWERS = 1;
    public static final int VIEW_FOLLOWING = 2;
    public static final int VIEW_UNFOLLOWERS = 3;
    public static final int TOTAL_FOLLOWERS = 4;
    public static final int TOTAL_FOLLOWINGS = 5;
    public static final int EQUALIZE = 6;
    public static final int EXIT = 7;

    public static final int YES = 1;
    public static final int NO = 2;

    public static void viewFollowers(GHPersonSet<GHUser> followers){
        int index = 1;
        for(GHUser user : followers){
             System.out.println("Follower " + index +" "+ user.getLogin());
             index++;
        }
    }

    public static void viewFollowings(GHPersonSet<GHUser> followings){
        int index = 1;
        for(GHUser user : followings){
            System.out.println("Following " + index + " " + user.getLogin());
            index++;
        }
    }

    public static void getUnfollowersInfo(GHPersonSet<GHUser> followers,GHPersonSet<GHUser> following){
        int totalUnfollowers = 0;
           for(GHUser user : following){
                 if(!followers.contains(user)){
                    System.out.println((totalUnfollowers + 1) +" : " + user.getLogin());
                    totalUnfollowers++;
                 }
           }
           System.out.println("Total Unfollowers: "+totalUnfollowers);
    }

    public static GHPersonSet<GHUser> getUnfollowersList(GHPersonSet<GHUser> followers,GHPersonSet<GHUser> following){
        GHPersonSet<GHUser> unfollowers = new GHPersonSet<>();
        for(GHUser user : following){
            if(!followers.contains(user)){
                    unfollowers.add(user);
            }
        }
        return unfollowers;
    }

    // create balance
    public static void equalizer(GHPersonSet<GHUser> unfollowers){
            for(GHUser user : unfollowers){
                try {
                    user.unfollow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public static void alertEqualize(){
        System.out.println("Are you sure ! you want to equalize followings & followers ");
        System.out.println("1.yes");
        System.out.println("2.no");
    }

    private static void showMenu(){
         System.out.println("**********************GIT_EQUALIZER V.0.1**********************");
         System.out.println("1.View Followers");
         System.out.println("2.View Following");
         System.out.println("3.View Unfollowers");
         System.out.println("4.Total Followers");
         System.out.println("5.Total Followings");
         System.out.println("6.Equalize");
         System.out.println("7.Exit");
         System.out.println("***************************************************************");
    }

    public static void main(String[] args){
        boolean running = true;
        String token = "putHereYourToken";
        Scanner sc = new Scanner(System.in);
        int choice = VIEW_FOLLOWERS;


        try {
            //connection
            GitHub github = new GitHubBuilder().withOAuthToken(token).build();
            GHUser me = github.getMyself();

            GHPersonSet<GHUser> followers = me.getFollowers();
            GHPersonSet<GHUser> followings = me.getFollows();

            while(running){

                    showMenu();

                    choice = sc.nextInt();
                    switch(choice){

                        case VIEW_FOLLOWERS:
                            viewFollowers(followers);
                            break;

                        case VIEW_FOLLOWING:
                            viewFollowings(followings);
                            break;

                        case VIEW_UNFOLLOWERS:
                            getUnfollowersInfo(followers , followings);
                            break;

                        case TOTAL_FOLLOWERS:
                            System.out.println("total followers: " + followers.size());
                            break;

                        case TOTAL_FOLLOWINGS:
                            System.out.println("total followings: " + followings.size());
                            break;

                        case EQUALIZE:
                            alertEqualize();
                            int confirm = sc.nextInt();
                            if(confirm == YES){
                                equalizer(getUnfollowersList(followers , followings));
                                System.out.println("*********************Terminated ! ************************");
                            }

                            break;

                        case EXIT:
                            running = false;
                            break;
                    }
            }

            System.out.println("GoodBye !");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
