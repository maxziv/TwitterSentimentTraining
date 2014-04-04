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
        
		//	My Applications Consumer and Auth Access Token
        twitter.setOAuthConsumer("sOErI5F8MWwWtLXzJcZMEXvSY", "f47RlqjTPOEXZMJEEIq9talRsC9YApj12ZbezQbAcc5seb2bta");
        twitter.setOAuthAccessToken(new AccessToken("1143685122-DLI0xqwVIwhu8AxaWuqiMELggfheJo1paxwnlJz", "L8Yg1UTslxivsRfDvam7awrZDLM4PxX7WkwLulX6S0FzM"));
        
        try {
        	
        	ResponseList<Status> a = twitter.getUserTimeline(new Paging(1,5));
        	
        	for(Status b: a) {
        		System.out.println(b.getText());
        	}

        }catch(Exception e ){
        	
        }
		
	}
	
	
	
	
}
