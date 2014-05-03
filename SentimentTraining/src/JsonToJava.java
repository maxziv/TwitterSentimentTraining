

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JsonToJava {

	public Tweet[] jsonToJava(int i){
		Tweet[] tweet = null;
		try {
			//json_tws?page=1&size=100&kwidx=7&city=NYC&dl=0
			//[page] start from 1 
			//[size] page size, positive int, suggest 10000 
			//[kwidx] start from 0 to 7 in ['i feel', 'i am feeling', "i'm feeling", 'i dont feel', "i'm", 'Im', 'I am', 'makes me'] or use all
			//[city] in ['LA', 'NYC', 'CHI', 'ATL'] or use all 
			//[dl] 0 or 1 whether to download

			//i is kwidx
			URL url = null;
			String loc[] = {"LA", "NYC", "CHI", "ATL"};
			for(int page_num = 20; page_num < 21; page_num++)
			{
				url = new URL("http://tweet2dowjones.appspot.com/json_tws?page="+page_num + "&size=10000&kwidx=all&city="+loc[i] + "&dl=0");
				URLConnection con = url.openConnection();
				InputStream in = con.getInputStream();
				String encoding = con.getContentEncoding();
				//			StringWriter writer = new StringWriter();
				//			IOUtils.copy(in, writer, encoding);
				//			String theString = writer.toString();
				//			System.out.println(url);
				//			System.out.println(theString);
				tweet = ArrayUtils.addAll(tweet, new Gson().fromJson(new InputStreamReader(in, "UTF-8"), Tweet[].class));
				System.out.println("Page num: "+ page_num);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tweet;
	}
	public static void main(String args[])
	{
		new JsonToJava().jsonToJava(1);
	}

}
