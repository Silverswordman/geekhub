package giuliasilvestrini.geekhub.services.locationServices;

import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.repositories.locationRepositories.RegionDAO;
import org.springframework.beans.factory.annotation.Autowired;


import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionService {

    private final RegionDAO regionDAO;

    @Autowired
    public RegionService(RegionDAO regionDAO) {
        this.regionDAO = regionDAO;
    }

    @Transactional(readOnly = true)
    public List<Region> findAll() {
        return regionDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Region findRegionById(long id) {
        return regionDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public Region saveRegion(Region region) {
        return regionDAO.save(region);
    }

    @Transactional
    public void deleteRegionById(long id) {
        regionDAO.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Province> getprovinceList(long codice_regione) {
        return regionDAO.getProvinceList(codice_regione);
    }

    public Region findRegionByName(String regionName) {
        return regionDAO.findByRegionName(regionName)
                .orElseThrow(() -> new NotFoundException("Regione " + regionName + " non trovata"));
    }

    @Transactional(readOnly = true)
    public Region findByCode(long code) {
        return regionDAO.findByRegionCode(code).orElseThrow(() -> new NotFoundException("Regione " + code + " non trovata"));
    }



}

