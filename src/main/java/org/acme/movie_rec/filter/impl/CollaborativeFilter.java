package org.acme.movie_rec.filter.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.acme.movie_rec.config.SparkConfig;
import org.acme.movie_rec.filter.RecomendationFilter;
import org.acme.movie_rec.loaders.MovieLoader;
import org.acme.movie_rec.loaders.RatingsLoader;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CollaborativeFilter implements RecomendationFilter {

	public void doFilter() {
		SparkSession spark = SparkConfig.getSparkSession();
		Dataset<Row> dataset = RatingsLoader.loadRatings(spark, false);

		JavaRDD<Rating> ratings = dataset.javaRDD().map(row -> new Rating(row.getAs("userId"),
				row.getAs("movieId"), row.getAs("rating")));

		MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), 10, 10, 0.01);

		Dataset<Row> movies = MovieLoader.loadMovies(spark);
		Rating[] recommendations = model.recommendProducts(1, 5);
		List<Integer> ids = Arrays.asList(recommendations).stream().map(Rating::product).toList();
		List<Row> filtered = movies.filter(movies.col("id").isInCollection(ids)).collectAsList();

		for (Rating r : recommendations) {
			Optional<Row> row = filtered.stream().filter(movie -> movie.getAs("id").equals(r.product())).findFirst();
			if (row.isPresent()) {
				System.out.println("Usuário " + r.user() + " deve assistir " + row.get().getAs("title"));
			} else {
				System.out.println("Usuário " + r.user() + " deve assistir " + r.product());

			}
		}

	}

}
