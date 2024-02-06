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
    public Page<Region> findAll(int size, int page, String order) {
        Pageable pageable = PageRequest.of(size, page, Sort.by(order));
        return regionDAO.findAll(pageable);
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
        // Implementa la logica per trovare una regione dal nome
        return regionDAO.findByRegionName(regionName)
                .orElseThrow(() -> new NotFoundException("Region with name " + regionName + " not found"));
    }

    @Transactional(readOnly = true)
    public Region findByCode(long code) {
        return regionDAO.findByRegionCode(code).orElseThrow(() -> new NotFoundException("Region with code " + code + " not found"));
    }


}
