package ang.neggaw.cinemas.services.implementations;

import ang.neggaw.cinemas.beans.CityProxy;
import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.proxies.CityRestProxy;
import ang.neggaw.cinemas.repositories.CinemaRepository;
import ang.neggaw.cinemas.services.CinemaService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CityRestProxy cityRestProxy;


    @Override
    public Object createCinema(Cinema c) {

        Cinema cinemaDB = cinemaRepository.findByName(c.getName());
        if (cinemaDB != null && cinemaDB.getIdsCitiesCinema().contains(c.getIdCity())) 
            return String.format("Cinema already exists with this name: '%s' and City, id: '%s'", cinemaDB.getName(), cinemaDB.getIdCity());

        try {
            c.setCity(cityRestProxy.getCity(c.getIdCity()).getBody());

            c.setEntityState(Cinema.EntityState.CREATED);
            c.setIdsCitiesCinema(List.of(c.getIdCity()));
            cinemaDB = cinemaRepository.save(c);

            // adding Cinema to the City entity
            if ( ! cinemaDB.getCity().getIdsCinemasCity().contains(cinemaDB.getIdCinema()) ) {
                cinemaDB.getCity().setEntityState(Cinema.EntityState.PROCESSING);
                cinemaDB.getCity().getIdsCinemasCity().add(cinemaDB.getIdCinema());
                cityRestProxy.updateCity(cinemaDB.getIdCity(), cinemaDB.getCity());
            }
        } catch (Exception e) {
            return String.format(e.getMessage() + "\n\nUnable to create Cinema. City with id: '%s' Not Found.", c.getIdCity());
        }
        return cinemaDB;
    }

    @Override
    public Cinema getCinema(long idCinema, boolean isFullCinema) {
        
        Cinema cinemaDB = cinemaRepository.findByIdCinema(idCinema);

        // with the option "isFullCinema" on can retrieve all associations of Cinema entity 
        if (isFullCinema && cinemaDB != null && !cinemaDB.getIdsCitiesCinema().isEmpty())
            cinemaDB.getIdsCitiesCinema().forEach(id -> cinemaDB.getCities().add(cityRestProxy.getCity(id).getBody()));
        return cinemaDB;
    }

    @Override
    public Collection<Cinema> allCinemas() { return cinemaRepository.findAll(); }

    @Override
    public Object updateCinema(Cinema c) {

        Cinema cinemaDB = cinemaRepository.findByIdCinema(c.getIdCinema());
        if (cinemaDB == null) return null;

        if (c.getEntityState() != null && c.getEntityState().equals(Cinema.EntityState.PROCESSING)) {
            c.setEntityState(Cinema.EntityState.UPDATED);
            return cinemaRepository.saveAndFlush(c);
        }

        c.setEntityState(Cinema.EntityState.UPDATED);
        c.setIdsCitiesCinema(cinemaDB.getIdsCitiesCinema());
        return cinemaRepository.saveAndFlush(c);
    }

    @Override
    public Object updateCinemaToOtherCity(long idCinema, long idCityNew, long idCityOld) {

        Cinema cinemaDB = getCinema(idCinema, false);
        if(cinemaDB == null) return String.format("Cinema with id: '%s' Not Found.", idCinema);

        try {
            CityProxy cityNew = cityRestProxy.getCity(idCityNew, true).getBody();
            CityProxy cityOld = cityRestProxy.getCity(idCityOld, true).getBody();

            // Removing old City entity from the Cinema entity
            cityOld.getIdsCinemasCity().removeIf(id -> id == idCinema);
            cityOld.setEntityState(Cinema.EntityState.PROCESSING);
            cityRestProxy.updateCity(idCityOld, cityOld);

            // Adding new City entity to Cinema entity
            if (! cityNew.getIdsCinemasCity().contains(idCinema)) {
                cityNew.getIdsCinemasCity().add(idCinema);
                cityNew.setEntityState(Cinema.EntityState.PROCESSING);
                cityRestProxy.updateCity(idCityNew, cityNew);
            }

            cinemaDB.getIdsCitiesCinema().removeIf(id -> id == idCityOld);
            cinemaDB.getIdsCitiesCinema().add(idCityNew);

        } catch (Exception e){
            return String.format(e.getMessage() + "\n\nUnable to Add Cinema with id: '%s' to City. City with id: '%s' Not Found.", idCinema, idCityNew);
        }
        return cinemaRepository.saveAndFlush(cinemaDB);
    }

    @Override
    public Object deleteCinema(long idCinema) {
        
        Cinema cinemaDB = getCinema(idCinema, true);
        if (cinemaDB == null ) return String.format("Unable to delete. Cinema with id: '%s' Not Found.", idCinema);

        if(! cinemaDB.getCities().isEmpty()) {
            cinemaDB.getCities().forEach(v -> {
                v.getIdsCinemasCity().removeIf(idC -> idC == idCinema);
                v.setEntityState(Cinema.EntityState.PROCESSING);
                cityRestProxy.updateCity(v.getIdCity(), v);
            });
        }

        cinemaRepository.delete(cinemaDB);
        cinemaDB.setEntityState(Cinema.EntityState.DELETED);

        return cinemaDB;
    }
}
