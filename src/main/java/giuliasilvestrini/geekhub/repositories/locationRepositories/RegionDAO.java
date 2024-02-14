package giuliasilvestrini.geekhub.repositories.locationRepositories;

import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.entities.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionDAO extends JpaRepository<Region, Long> {

    Optional<Region> findByRegionCode(long regionCode);

    Optional<Region> findByRegionName(String regionName);

    @Query("SELECT p FROM Province p WHERE p.region.id = :regionId")
    List<Province> findByRegionId(@Param("regionId") long regionId);

    @Query("SELECT p.provinceList FROM Region p WHERE p.regionCode = :regionCode")
    List<Province> getProvinceList(@Param("regionCode") long regionCode);
}
