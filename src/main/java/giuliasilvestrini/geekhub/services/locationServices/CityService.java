package giuliasilvestrini.geekhub.services.locationServices;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.locationRepositories.CityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    private CityDAO cityDAO;


    public Page<City> findAll(int size, int page, String order) {
        Pageable pageable = PageRequest.of(size, page, Sort.by(order));
        return cityDAO.findAll(pageable);
    }


    public City findByid(long id) {
        return cityDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    public City saveCity(City city) {
        return cityDAO.save(city);
    }

    public City findCityByName(String cityName) {
        return cityDAO.findByCityName(cityName)
                .orElseThrow(() -> new NotFoundException("Province with name " + cityName + " not found"));
    }
}

