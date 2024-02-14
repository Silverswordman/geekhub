package giuliasilvestrini.geekhub.services.locationServices;

import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.locationRepositories.ProvinceDAO;
import giuliasilvestrini.geekhub.repositories.locationRepositories.RegionDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {

    private final ProvinceDAO provinceDAO;
    private final RegionService regionService;
    private final RegionDAO regionDAO;

    public ProvinceService(ProvinceDAO provinceDAO, RegionService regionService, RegionDAO regionDAO) {
        this.provinceDAO = provinceDAO;
        this.regionService = regionService;
        this.regionDAO = regionDAO;
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

    public List<Province> getProvincesByRegionId(long regionId) {
        Region region = regionDAO.findById(regionId).orElseThrow(() -> new NotFoundException(regionId));
        return region.getProvinceList();
    }

}




