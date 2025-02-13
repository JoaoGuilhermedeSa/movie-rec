package org.acme.movie_rec.filter.impl;

import org.acme.movie_rec.filter.RecomendationFilter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class CollaborativeFilter implements RecomendationFilter {

	public Rating[] doFilter(Dataset<Row> dataSet, int userId, int pageSize) {
		JavaRDD<Rating> ratings = dataSet.javaRDD()
				.map(row -> new Rating(row.getAs("userId"), row.getAs("movieId"), row.getAs("rating")));

		MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), 10, 10, 0.01);

		return model.recommendProducts(userId, pageSize);

	}

}
