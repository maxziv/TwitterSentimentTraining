/**
 * This Class is used to get the list of status.
 */
package com.jdwb.twitterapi;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class GetUserStatus {
	
	public static void main(String[] args) {
		
		Twitter twitter = new TwitterFactory().getInstance();
      
        try {
        	
        	ResponseList<Status> a = twitter.getUserTimeline(new Paging(1,5));
        	
        	for(Status b: a) {
        		System.out.println(b.getText());
        	}

        }catch(Exception e ){
        	
        }
		
	}
	
	
	
	
}
