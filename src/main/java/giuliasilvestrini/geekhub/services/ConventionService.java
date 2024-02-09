package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.AccessDeniedException;
import giuliasilvestrini.geekhub.exceptions.DuplicateEntryException;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.ConventionDTO;
import giuliasilvestrini.geekhub.repositories.ConventionDAO;
import giuliasilvestrini.geekhub.services.locationServices.CityService;
import giuliasilvestrini.geekhub.services.locationServices.ProvinceService;
import giuliasilvestrini.geekhub.services.locationServices.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Convention saveConvention(ConventionDTO conventionDTO, User user) {
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
        convention.setLogo("https://placekitten.com/100/100");
        convention.setCoverImage("https://placekitten.com/200/200");
        convention.setSite(conventionDTO.site());
        convention.setAddress(conventionDTO.address());
        convention.setCreator(user);
        convention.setRegion(region);
        convention.setProvince(province);
        convention.setCity(city);

        return conventionDAO.save(convention);
    }

    public Convention updateConvention(UUID conventionId, ConventionDTO conventionDTO, User user) {
        Convention existingConvention = findById(conventionId);

        if (user.getRole() == Role.ADMIN || (user.getRole() == Role.EVENTPLANNER && existingConvention.getCreator().getUserId().equals(user.getUserId()))) {
            Region region = regionService.findRegionByName(conventionDTO.region());
            if (region == null) {
                throw new NotFoundException("Not found:" + conventionDTO.region());
            }
            Province province = provinceService.findProvinceByName(conventionDTO.province());
            if (province == null) {
                throw new NotFoundException("Not found:" + conventionDTO.province());
            }
            City city = cityService.findCityByName(conventionDTO.city());
            if (city == null) {
                throw new NotFoundException("Not found: " + conventionDTO.city());
            }
            existingConvention.setTitle(conventionDTO.title());
            existingConvention.setStartDate(conventionDTO.startDate());
            existingConvention.setEndDate(conventionDTO.endDate());
            existingConvention.setSite(conventionDTO.site());
            existingConvention.setAddress(conventionDTO.address());
            existingConvention.setRegion(region);
            existingConvention.setProvince(province);
            existingConvention.setCity(city);
            return conventionDAO.save(existingConvention);
        } else {
            throw new AccessDeniedException("Only ADMIN or the creator EVENTPLANNER are allowed to update conventions.");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ConventionService.class);

    public String uploadLogo(MultipartFile file, UUID conventionId, User user) throws IOException {
        Convention convention = findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }

        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Only ADMIN or the creator of the convention are allowed to upload logo.");
        }

        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        convention.setLogo(url);
        conventionDAO.save(convention);
        return url;
    }

    public String uploadCover(MultipartFile file, UUID conventionId, User user) throws IOException {
        Convention convention = findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }

        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Only ADMIN or the creator of the convention are allowed to upload cover image.");
        }

        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        convention.setCoverImage(url);
        conventionDAO.save(convention);
        return url;
    }



    public Convention addSectionToConvention(UUID conventionId, Section section) {
        Convention convention = findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }

        convention.getSectionList().add(section);

        return conventionDAO.save(convention);
    }


    public void deleteConvention(UUID conventionId) {
        Convention conventionToDelete = findById(conventionId);
        if (conventionToDelete == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }

        conventionDAO.delete(conventionToDelete);
    }
}
