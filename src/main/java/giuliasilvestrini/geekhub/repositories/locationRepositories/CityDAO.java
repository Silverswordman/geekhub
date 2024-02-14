package giuliasilvestrini.geekhub.repositories.locationRepositories;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityDAO extends JpaRepository<City, Long> {
    Optional<City> findByCityName(String cityName);


    List<City> findByProvinceId(long provinceId);
}


