package ang.neggaw.cities.services.implementations;

import ang.neggaw.cities.entities.City;
import ang.neggaw.cities.proxies.CinemaRestProxy;
import ang.neggaw.cities.repositories.CityRepository;
import ang.neggaw.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CinemaRestProxy cinemaRestProxy;


    @Override
    public Object createCity(City c) {

        City cityDB = cityRepository.findByName(c.getName());
        if (cityDB != null) return String.format("City already exists with name: '%s'", cityDB.getName());

        c.setEntityState(City.EntityState.CREATED);
        return cityRepository.save(c);
    }

    @Override
    public City getCity(long idCity, boolean isFullCity) {
        City cityDB = cityRepository.findByIdCity(idCity);
        if( isFullCity && cityDB != null && ! cityDB.getIdsCinemasCity().isEmpty()) {
            cityDB.getIdsCinemasCity().forEach(id -> cityDB.getCinemas().add(cinemaRestProxy.getCinema(id).getBody()));
        }

        return cityDB;
    }

    @Override
    public Collection<City> allCities() { return cityRepository.findAll(); }

    @Override
    public Object updateCity(City c) {

        City cityDB = cityRepository.findByIdCity(c.getIdCity());
        if (cityDB == null) return String.format("City with id: '%s' Not Found.", c.getIdCity());

        if(c.getEntityState() != null && c.getEntityState().equals(City.EntityState.PROCESSING)){
            c.setEntityState(City.EntityState.UPDATED);
            return cityRepository.saveAndFlush(c);
        }

        c.setEntityState(City.EntityState.UPDATED);
        c.setIdsCinemasCity(cityDB.getIdsCinemasCity());
        return cityRepository.saveAndFlush(c);
    }

    @Override
    public Object deleteCity(long idCity) {

        City cityDB = getCity(idCity, true);
        if (cityDB == null) return String.format("City with id: '%s' Not Found.", idCity);

        if( ! cityDB.getCinemas().isEmpty()) {
            cityDB.getCinemas().forEach(cinema -> {
                cinema.getIdsCitiesCinema().removeIf(id -> id == idCity);
                cinema.setEntityState(City.EntityState.PROCESSING);
                cinemaRestProxy.updateCinema(cinema.getIdCinema(), cinema);
                cinemaRestProxy.deleteCinema(cinema.getIdCinema());
            });
        }

        cityRepository.delete(cityDB);
        cityDB.setEntityState(City.EntityState.DELETED);

        return cityDB;
    }
}
