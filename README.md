Necessária a instalação do spark: https://spark.apache.org/downloads.html


Necessário o build da aplicação.

Após o build, executar na raiz do projeto.
spark-submit --class org.acme.movie_rec.MovieRecApplication --master local[*] --driver-memory 8g --executor-memory 8g --executor-cores 8 target/movie-rec-0.0.1-SNAPSHOT.jar
