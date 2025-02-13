package org.acme.movie_rec.config;

import org.apache.spark.sql.SparkSession;

public class SparkConfig {
    public static SparkSession getSparkSession() {
        return SparkSession.builder()
                .appName("MovieRecommender")
                .master("local[*]")
                .getOrCreate();
    }
}