package giuliasilvestrini.geekhub.repositories.locationRepositories;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvinceDAO extends JpaRepository<Province, Long> {

    Optional<Province> findBySigla(String code);

    Optional<Province> findByProvinceName(String provinceName);




    @Query("SELECT p.cityList FROM Province p WHERE p.sigla = :prov_sigla")
    List<City> getCityList(@Param("prov_sigla") String prov_sigla);

}
