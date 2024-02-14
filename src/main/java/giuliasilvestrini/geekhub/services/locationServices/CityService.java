package giuliasilvestrini.geekhub.services.locationServices;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.locationRepositories.CityDAO;
import giuliasilvestrini.geekhub.repositories.locationRepositories.ProvinceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityDAO cityDAO;


    public List<City> findAll() {
        return cityDAO.findAll();
    }

    public City findById(long id) {
        return cityDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public City saveCity(City city) {
        return cityDAO.save(city);
    }

    public City findCityByName(String cityName) {
        return cityDAO.findByCityName(cityName)
                .orElseThrow(() -> new NotFoundException("Città with name " + cityName + " not found"));
    }

    public List<City> findCitiesByProvinceId(long provinceId) {
        return cityDAO.findByProvinceId(provinceId);
    }


}
