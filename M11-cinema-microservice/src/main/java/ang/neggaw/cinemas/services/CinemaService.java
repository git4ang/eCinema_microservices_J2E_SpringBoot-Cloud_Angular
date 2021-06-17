package ang.neggaw.cinemas.services;

import ang.neggaw.cinemas.entities.Cinema;

import java.util.Collection;

public interface CinemaService {
    Object createCinema(Cinema c);
    Cinema getCinema(long idCinema, boolean isFullCinema);
    Collection<Cinema> allCinemas();
    Object updateCinema(Cinema c);
    Object updateCinemaToOtherCity(long idCinema, long idCityNew, long idCityOld);
    Object deleteCinema(long idCinema);
}
