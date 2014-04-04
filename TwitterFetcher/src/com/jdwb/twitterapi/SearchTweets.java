package com.jdwb.twitterapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SearchTweets {

	Twitter twitter;

	public SearchTweets()
	{
		twitter = new TwitterFactory().getInstance();
	}
	/**
	 * Usage: java twitter4j.examples.search.SearchTweets [query]
	 *
	 * @param args
	 */
	public void query(String [] args)
	{
		if (args.length < 1) {
			System.out.println("java twitter4j.examples.search.SearchTweets [query]");
			System.exit(-1);
		}
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			Query query = new Query(args[0]);
			QueryResult result;
			do {
				result = twitter.search(query);
				System.out.println(result.getMaxId());
				List<Status> tweets = result.getTweets();
				

				for (Status tweet : tweets) {
					System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());

				}

			} while ((query = result.nextQuery()) != null);
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
	}

	public String getTweetById(String id)
	{
		String tweet = null;
		try {
			Status status;
			status = twitter.showStatus(Long.parseLong(id));
			//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			tweet = status.getText();
		} catch (TwitterException te) {

			System.out.println("Failed to search tweets: " + te.getMessage());
			return tweet;
		}
		return tweet;
	}
	
	public static void main(String[] args) throws InterruptedException {

		SearchTweets searchObj = new SearchTweets();
		String csvFile = "corpus.csv";
		BufferedReader br = null;
		String line = "", cvsSplitBy = ",", searchTerm = null, attitude = null, 
				statusId = null, tweetText = null, folder = null, tts = "tweet_training_set/";
		int count = 0, resumeCount = 0;
		if(args.length!=0)
			resumeCount = Integer.parseInt(args[0]);
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				if(resumeCount-- > 0)
				{
					count++;
					continue;
				}
				// use comma as separator
				String[] result = line.split(cvsSplitBy);
				searchTerm = result[0].replaceAll("\"", "");
				attitude = result[1].replaceAll("\"", "");
				statusId = result[2].replaceAll("\"", "");
				System.out.println("Tweet [searchTerm= " + searchTerm
						+ " , attitude=" + attitude 
						+ " , statusId=" + statusId + "]");
				tweetText = searchObj.getTweetById(statusId);
				if(attitude.equals("irrelevant"))
					folder = tts + "irr";
				else if(attitude.equals("positive"))
					folder = tts + "pos";
				else if(attitude.equals("negative"))
					folder = tts + "neg";
				else if(attitude.equals("neutral"))
					folder = tts + "neu";
				if(tweetText!=null){
					File file = new File(folder + "/" + statusId + ".txt");
					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(tweetText);
					bw.close();
				}
				System.out.println("--> downloading tweet #"+statusId + " ("+ count + " of 5513) (" + (5513.0-count)* 28.0/60.0/60.0 + " hrs left)," +
						" pausing 28 sec to obey Twitter API rate limits");
				Thread.sleep(28000);
				count++;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}
}