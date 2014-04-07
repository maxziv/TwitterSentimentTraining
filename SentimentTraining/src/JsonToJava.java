

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JsonToJava {
	
	public Tweet[] jsonToJava(){
		Tweet[] tweet = null;
		try {
			URL url = new URL("http://tweet2dowjones.appspot.com/json_dump?q=Im");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			
			tweet = new Gson().fromJson(new InputStreamReader(in, "UTF-8"), Tweet[].class);
			//System.out.println(tweet[0].getText());
//			tweet[0].setClassification("pos");
//			System.out.println(new Gson().toJson(tweet[0]));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tweet;
	}
	public static void main(String args[])
	{
		new JsonToJava().jsonToJava();
	}
	
}
