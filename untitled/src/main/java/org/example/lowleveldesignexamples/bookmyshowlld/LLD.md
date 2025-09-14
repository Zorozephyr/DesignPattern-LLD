BookMyShow

User -> location(city) -> movies -> theatre -> showtime -> availableSeats-> booking -> Payment


Objects:
1.User
2.Location
3.Movies
4.Theatre
5.Shows -> Movie, Theatre
6.Seats
7.Screens
8.Booking
9.Payment

User -> Name
Location -> List<Theatres>, getMoviesInTheCity()
Theatres -> List<Movies>, List<Shows>, getShowTimesForMovieX(Movie X),
Shows -> time, Movie, Screen, HashMap<Seats> BookedSeats
Screen -> Seats[]
Seats -> Type, Price, seatNo


