package org.example.lowleveldesignexamples.bookmyshowlld;

import org.example.lowleveldesignexamples.bookmyshowlld.enums.City;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TheatreController {
    Map<City,List<Theatre>> cityVsTheatre;
    List<Theatre> allTheatre;

    TheatreController(){
        cityVsTheatre = new HashMap<>();
        allTheatre = new ArrayList<>();
    }

    void addTheatre(Theatre theatre, City city){
        allTheatre.add(theatre);

        List<Theatre> theatres = cityVsTheatre.getOrDefault(city, new ArrayList<>());
        theatres.add(theatre);
        cityVsTheatre.put(city, theatres);
    }

    Map<Theatre, List<Show>> getAllShow(Movie movie, City city){
        Map<Theatre, List<Show>> theatreVsShows = new HashMap<>();
        List<Theatre> theatres = cityVsTheatre.get(city);
        String movieName = movie.getMovieName();
        for(Theatre theatre: theatres){
            List<Show> shows = theatre.getShows().stream().filter(t -> t.movie.getMovieName().equals(movieName)).toList();
            theatreVsShows.put(theatre,shows);
        }
        return theatreVsShows;
    }


}
