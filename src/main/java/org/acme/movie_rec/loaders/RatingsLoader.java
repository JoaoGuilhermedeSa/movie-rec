package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;

import org.acme.movie_rec.helper.FileHelper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class RatingsLoader {
	
		 public static Dataset<Row> loadRatings(SparkSession spark, boolean useSmall) {
			 
			 System.out.println("Reading ratings. Use small = " + useSmall);
			 
			 Dataset<Row> response = spark.read()
		                .option("header", "true")       
		                .option("inferSchema", "true") 
		                .csv(FileHelper.ratingsPath(useSmall))
		                .select(
		                    col("userId").cast("int"), 
		                    col("movieId").cast("int"), 
		                    col("rating").cast("double"), 
		                    col("timestamp").cast("long")
		                )
		                .filter("userId IS NOT NULL AND movieId IS NOT NULL");
			 
			 System.out.println("Loaded "+ response.count() +" ratings lines");
			 
			 return response;
		    }
		}
