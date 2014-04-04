

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Tweet {
	
	@Getter @Setter private long id;
	@Getter @Setter private String text;
	@Getter @Setter private String created_at;
	@Getter @Setter private Boolean processed;
	@Getter @Setter private Boolean searchGeoPlace;
	@Getter @Setter private String timeStamp;
	@Getter @Setter private String searchTerm;
	@Getter @Setter private int retweet_count;
	@Getter @Setter private String geo;
	@Getter @Setter private String searchGeo;
	@Getter @Setter private int favorite_count;
	
	
}
