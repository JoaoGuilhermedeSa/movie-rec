package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class RatingsLoader {

	private static final String RATINGS_CSV = "src/main/resources/ratings.csv";
	private static final String RATINGS_SMALL_CSV = "src/main/resources/ratings_small.csv";

		 public static Dataset<Row> loadRatings(SparkSession spark, boolean useSmall) {
		        return spark.read()
		                .option("header", "true")       
		                .option("inferSchema", "true") 
		                .csv(useSmall? RATINGS_SMALL_CSV : RATINGS_CSV)
		                .select(
		                    col("userId").cast("int"), 
		                    col("movieId").cast("int"), 
		                    col("rating").cast("double"), 
		                    col("timestamp").cast("long")
		                )
		                .filter("userId IS NOT NULL AND movieId IS NOT NULL");
		    }
		}
