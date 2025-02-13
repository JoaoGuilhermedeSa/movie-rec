package org.acme.movie_rec.loaders;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;

public class CreditsLoader {
	private static final String CREDITS_CSV = "src/main/resources/credits.csv";

    public static Dataset<Row> loadCredits(SparkSession spark) {
    	DataType castSchema = DataTypes.createArrayType(
    	        DataTypes.createStructType(new StructField[]{
    	            DataTypes.createStructField("name", DataTypes.StringType, true)
    	        })
    	    );

    	    DataType crewSchema = DataTypes.createArrayType(
    	        DataTypes.createStructType(new StructField[]{
    	            DataTypes.createStructField("name", DataTypes.StringType, true),
    	            DataTypes.createStructField("job", DataTypes.StringType, true)
    	        })
    	    );
    	
    	
    	   return spark.read()
                   .option("header", "true")
                   .option("inferSchema", "true")
                   .csv(CREDITS_CSV)
                   .withColumn("cast", from_json(col("cast"), castSchema)) 
                   .withColumn("crew", from_json(col("crew"), crewSchema))
                   .select("id", "cast", "crew");
    }
}
