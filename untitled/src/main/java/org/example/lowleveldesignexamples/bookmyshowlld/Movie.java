package org.example.lowleveldesignexamples.bookmyshowlld;

public class Movie {
    int movieId;
    String movieName;
    int movieDurationInMin;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieDuration() {
        return movieDurationInMin;
    }

    public void setMovieDuration(int movieDurationInMin) {
        this.movieDurationInMin = movieDurationInMin;
    }
}
