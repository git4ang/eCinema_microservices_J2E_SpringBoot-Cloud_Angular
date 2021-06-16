package ang.neggaw.cities.services;

import ang.neggaw.cities.entities.City;

import java.util.Collection;

public interface CityService {
    Object createCity(City c);
    City getCity(long idCity);
    Collection<City> allCities();
    Object updateCity(City c);
    Object deleteCity(long idCity);
}
