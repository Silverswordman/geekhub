package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.exceptions.DuplicateEntryException;
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

    public Page<Convention> findAll(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return conventionDAO.findAll(pageable);
    }

    public Convention findById(UUID conventionId) {
        return conventionDAO.findById(conventionId).orElseThrow(() -> new NotFoundException(conventionId));
    }

    public Convention findByTitle(String title) {
        return conventionDAO.findByTitle(title).orElseThrow(() -> new NotFoundException("Convention not found with title: " + title));
    }

    public Convention saveConvention(ConventionDTO conventionDTO) {
        Region region = regionService.findRegionByName(conventionDTO.region());
        if (region == null) {
            throw new NotFoundException("Not found:" + conventionDTO.region());
        }
        Province province = provinceService.findProvinceByName(conventionDTO.province());
        if (province == null) {
            throw new NotFoundException("Not found:" + conventionDTO.province());
        }

        if (!province.getRegion().equals(region)) {
            throw new IllegalArgumentException("Province " + province.getProvinceName() + " does not belong to region " + region.getRegionName());
        }

        City city = cityService.findCityByName(conventionDTO.city());
        if (city == null) {
            throw new NotFoundException("Not found: " + conventionDTO.city());
        }

        if (!city.getProvince().equals(province)) {
            throw new IllegalArgumentException("City " + city.getCityName() + " does not belong to province " + province.getProvinceName());
        }

        Optional<Convention> existingConventionOpt = conventionDAO.findByTitle(conventionDTO.title());
        if (existingConventionOpt.isPresent()) {
            throw new DuplicateEntryException("Convention with title " + conventionDTO.title() + " already exists.");
        }

        Convention convention = new Convention();
        convention.setTitle(conventionDTO.title());
        convention.setStartDate(conventionDTO.startDate());
        convention.setEndDate(conventionDTO.endDate());
        convention.setSite(conventionDTO.site());
        convention.setAddress(conventionDTO.address());
        convention.setRegion(region);
        convention.setProvince(province);
        convention.setCity(city);

        return conventionDAO.save(convention);
    }



    public void conventionDelete(UUID conventionId) {
        Convention delete = this.findById(conventionId);
        conventionDAO.delete(delete);}

    public Convention update(Convention convention) {
        if (conventionDAO.existsById(convention.getConventionId())) {
            return conventionDAO.save(convention);
        } else {

            throw new IllegalArgumentException("Convention with ID " + convention.getConventionId() + " does not exist.");
        }
    }
}
