package org.acme.movie_rec;

import org.acme.movie_rec.config.SparkConfig;
import org.acme.movie_rec.filter.RecomendationFilter;
import org.acme.movie_rec.filter.impl.CollaborativeFilter;
import org.acme.movie_rec.filter.impl.ContentFilter;
import org.acme.movie_rec.loaders.CreditsLoader;
import org.acme.movie_rec.loaders.KeywordsLoader;
import org.acme.movie_rec.loaders.LinksLoader;
import org.acme.movie_rec.loaders.MovieLoader;
import org.acme.movie_rec.loaders.RatingsLoader;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MovieRecApplication {

	public static void main(String[] args) {
		SparkSession spark = SparkConfig.getSparkSession();
		int userId = 2;
		Dataset<Row> ratingsDataset = RatingsLoader.loadRatings(spark, false);
		Dataset<Row> movieDataset = MovieLoader.loadMovies(spark);
		Dataset<Row> credits = CreditsLoader.loadCredits(spark);
		Dataset<Row> keywords = KeywordsLoader.loadKeywords(spark);
		Dataset<Row> links = LinksLoader.loadLinks(spark, false);

		Dataset<Row> fullMoviesDataset = movieDataset.join(credits, "id").join(keywords, "id").join(links,
				links.col("movieId").equalTo(movieDataset.col("id")));

		ContentFilter contentfilter = new ContentFilter();
		contentfilter.doFilter(fullMoviesDataset, userId, 20);

		RecomendationFilter filter = new CollaborativeFilter();
		Rating[] recs = filter.doFilter(ratingsDataset, userId, 20);
		for (Rating r : recs) {
			System.out.println("O usuário " + userId + " pode assistir o filme " + r.product());
		}
		
		//TODO ADD PARAMS AND HYBRID FILTER

		spark.stop();

	}
}
