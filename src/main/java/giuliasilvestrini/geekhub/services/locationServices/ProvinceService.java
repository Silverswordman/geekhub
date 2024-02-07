package giuliasilvestrini.geekhub.services.locationServices;

import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.locationRepositories.ProvinceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {

    private final ProvinceDAO provinceDAO;
    private final RegionService regionService;

    @Autowired
    public ProvinceService(ProvinceDAO provinceDAO, RegionService regionService) {
        this.provinceDAO = provinceDAO;
        this.regionService = regionService;
    }

    public List<Province> findAll() {
        return provinceDAO.findAll();
    }

    public Province findById(long id) {
        return provinceDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Province saveProvince(Province province) {
        return provinceDAO.save(province);
    }

    public void deleteById(long id) {
        provinceDAO.deleteById(id);
    }


    public Province findBySigla(String sigla) {
        return provinceDAO.findBySigla(sigla)
                .orElseThrow(() -> new NotFoundException("Provincia " + sigla + " non trovata"));
    }


    public Province findProvinceByName(String provinceName) {
        return provinceDAO.findByProvinceName(provinceName)
                .orElseThrow(() -> new NotFoundException("Provincia " + provinceName + " non trovata"));
    }
}



