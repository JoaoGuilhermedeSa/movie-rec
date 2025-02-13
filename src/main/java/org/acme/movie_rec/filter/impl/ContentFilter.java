package org.acme.movie_rec.filter.impl;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.concat_ws;
import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.udf;

import java.util.List;

import org.acme.movie_rec.filter.RecomendationFilter;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors; // Import Vectors
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;


public class ContentFilter implements RecomendationFilter {

    public Rating[] doFilter(Dataset<Row> dataSet, int userId, int pageSize) {
        dataSet = prepareFeatures(dataSet);
        dataSet = computeTFIDF(dataSet);

        List<Row> result = recommendMovies(dataSet, "The Lord of the Rings", 10);
        for (Row row : result) {
            System.out.println(
                    "###################################################################################################################################");
            System.out.println(row.toString());
        }

        return null;
    }

    public static Dataset<Row> prepareFeatures(Dataset<Row> moviesData) {

        moviesData = moviesData
                .withColumn("genres_str", expr("array_join(transform(genres, x -> x.name), ' ')"))
                .withColumn("keywords_str", expr("array_join(transform(keywords, x -> x.name), ' ')"))
                .withColumn("cast_str", expr("array_join(transform(cast, x -> x.name), ' ')"))
                .withColumn("crew_str", expr("array_join(transform(crew, x -> x.name), ' ')"))
                .withColumn("combined_features",
                        concat_ws(" ", col("title"), col("genres_str"), col("keywords_str"), col("cast_str"), col("crew_str")));


        return moviesData;
    }

    private static Dataset<Row> computeTFIDF(Dataset<Row> moviesData) {
        Tokenizer tokenizer = new Tokenizer().setInputCol("combined_features").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(moviesData);

        StopWordsRemover remover = new StopWordsRemover().setInputCol("words").setOutputCol("filtered_words");
        Dataset<Row> filteredData = remover.transform(wordsData);

        HashingTF hashingTF = new HashingTF().setInputCol("filtered_words").setOutputCol("raw_features").setNumFeatures(5000);
        Dataset<Row> featurizedData = hashingTF.transform(filteredData);

        IDF idf = new IDF().setInputCol("raw_features").setOutputCol("features");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset<Row> rescaledData = idfModel.transform(featurizedData);

        return rescaledData;
    }


    public static List<Row> recommendMovies(Dataset<Row> rescaledData, String movieTitle, int numRecommendations) {
        // Filter the movie by name
        Dataset<Row> targetMovie = rescaledData.filter(col("title").equalTo(movieTitle));

        if (targetMovie.isEmpty()) {
            System.out.println("Movie not found!");
            return List.of();
        }

        // Get the target movie's feature vector.  No need for Kryo here.
        Vector targetVector = targetMovie.select("features").first().getAs(0);

        // Define the cosine similarity UDF.  Correctly type the input.
        UserDefinedFunction cosineSimilarity = udf(
                (Vector v) -> {
                    if (v == null || targetVector == null) {
                        return 0.0;
                    }
                    return Vectors.sqdist(v, targetVector) == 0 ? 1.0 : 1 / (1 + Vectors.sqdist(v, targetVector));
                }, DataTypes.DoubleType
        );


        // Apply the UDF and get the recommendations.
        Dataset<Row> similarityData = rescaledData
                .withColumn("similarity", cosineSimilarity.apply(col("features")))
                .select("title", "similarity")
                .orderBy(col("similarity").desc()) // Order by similarity
                .limit(numRecommendations);
        
        return similarityData.filter(col("title").notEqual(movieTitle)).collectAsList(); // Exclude the input movie itself
    }
}