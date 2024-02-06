package giuliasilvestrini.geekhub.repositories.locationRepositories;


import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionDAO extends JpaRepository<Region, Long> {


    Optional<Region> findByRegionCode(long regionCode);

    Optional<Region> findByRegionName(String regionName);

    @Query("SELECT p.provinceList FROM Region p WHERE p.regionCode = :codice_regione")
    List<Province> getProvinceList(@Param("codice_regione") long codice_regione);


}