package ang.neggaw.cities.services.implementations;

import ang.neggaw.cities.entities.City;
import ang.neggaw.cities.repositories.CityRepository;
import ang.neggaw.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;


    @Override
    public Object createCity(City c) {

        City cityDB = cityRepository.findByName(c.getName());
        if (cityDB != null) return String.format("City already exists with name: '%s'", cityDB.getName());

        c.setEntityState(City.EntityState.CREATED);
        return cityRepository.save(c);
    }

    @Override
    public City getCity(long idCity) { return cityRepository.findByIdCity(idCity); }

    @Override
    public Collection<City> allCities() { return cityRepository.findAll(); }

    @Override
    public Object updateCity(City c) {

        City cityDB = cityRepository.findByIdCity(c.getIdCity());
        if (cityDB == null) return String.format("City with id: '%s' Not Found.", c.getIdCity());

        c.setEntityState(City.EntityState.UPDATED);
        return cityRepository.saveAndFlush(c);
    }

    @Override
    public Object deleteCity(long idCity) {

        City city = getCity(idCity);
        if (city == null) return String.format("City with id: '%s' Not Found.", idCity);

        cityRepository.delete(city);
        city.setEntityState(City.EntityState.DELETED);

        return city;
    }
}
