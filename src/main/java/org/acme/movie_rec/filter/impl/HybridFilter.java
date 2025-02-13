package org.acme.movie_rec.filter.impl;

import org.acme.movie_rec.config.SparkConfig;
import org.acme.movie_rec.filter.RecomendationFilter;
import org.acme.movie_rec.loaders.CreditsLoader;
import org.acme.movie_rec.loaders.KeywordsLoader;
import org.acme.movie_rec.loaders.MovieLoader;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class HybridFilter implements RecomendationFilter {

	public Rating[]  doFilter(Dataset<Row> dataSet, int userId, int pageSize) {
		SparkSession spark = SparkConfig.getSparkSession();
		Dataset<Row> moviesData = MovieLoader.loadMovies(spark);
		Dataset<Row> credits = CreditsLoader.loadCredits(spark);
		Dataset<Row> keywords = KeywordsLoader.loadKeywords(spark);

		Dataset<Row> fullMoviesDataset = moviesData.join(credits, "id").join(keywords, "id");
		fullMoviesDataset.show(5);
		
		return null;
	}

}
