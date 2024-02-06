package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.ConventionDTO;
import giuliasilvestrini.geekhub.repositories.ConventionDAO;
import giuliasilvestrini.geekhub.services.locationServices.CityService;
import giuliasilvestrini.geekhub.services.locationServices.ProvinceService;
import giuliasilvestrini.geekhub.services.locationServices.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConventionService {

    @Autowired
    private ConventionDAO conventionDAO;

    @Autowired
    private RegionService regionService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    Cloudinary cloudinary;

    public Page<Convention> findAll(int size, int page, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return conventionDAO.findAll(pageable);
    }

    public Convention findById(UUID conventionId) {
        return conventionDAO.findById(conventionId).orElseThrow(() -> new NotFoundException(conventionId));
    }

    public Convention saveConvention(ConventionDTO conventionDTO) {
        Region region = regionService.findRegionByName(conventionDTO.region());
        Province province = provinceService.findProvinceByName(conventionDTO.province());
        City city = cityService.findCityByName(conventionDTO.city());

        Convention convention = new Convention();
        convention.setTitle(conventionDTO.title());
        convention.setStartDate(conventionDTO.startDate());
        convention.setEndDate(conventionDTO.endDate());
        convention.setStreet(conventionDTO.street());
        convention.setHouseNumber(conventionDTO.houseNumber());
        convention.setRegion(region);
        convention.setProvince(province);
        convention.setCity(city);

        return conventionDAO.save(convention);
    }


    public void deleteById(UUID conventionId) {
        conventionDAO.deleteById(conventionId);
    }

    public Convention update(Convention convention) {
        if (conventionDAO.existsById(convention.getConventionId())) {
            return conventionDAO.save(convention);
        } else {

            throw new IllegalArgumentException("Convention with ID " + convention.getConventionId() + " does not exist.");
        }
    }
}
