package org.acme.movie_rec.helper;

public class FileHelper {

	private static final String CREDITS_CSV = "credits.csv";
	private static final String MOVIES_CSV = "movies_metadata.csv";
	private static final String KEYWORDS_CSV = "keywords.csv";
	private static final String RATINGS_CSV = "ratings.csv";
	private static final String RATINGS_SMALL_CSV = "ratings_small.csv";
	private static final String LINKS_SMALL_CSV = "links_small.csv";
	private static final String LINKS_CSV = "links.csv";
	private static final String BASE_PATH = "src/main/resources/dataset/";

	public static String creditsPath() {
		return BASE_PATH + CREDITS_CSV;
	}

	public static String moviesPath() {
		return BASE_PATH + MOVIES_CSV;
	}

	public static String keywordsPath() {
		return BASE_PATH + KEYWORDS_CSV;
	}

	public static String ratingsPath(boolean useSmall) {
		if (useSmall) {
			return BASE_PATH + RATINGS_SMALL_CSV;
		}
		return BASE_PATH + RATINGS_CSV;
	}
	
	public static String linksPath(boolean useSmall) {
		if (useSmall) {
			return BASE_PATH + LINKS_SMALL_CSV;
		}
		return BASE_PATH + LINKS_CSV;
	}

}
