package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.services.locationServices.CityService;
import giuliasilvestrini.geekhub.services.locationServices.ProvinceService;
import giuliasilvestrini.geekhub.services.locationServices.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final RegionService regionService;
    private final ProvinceService provinceService;
    private final CityService cityService;

    @Autowired
    public LocationController(RegionService regionService, ProvinceService provinceService, CityService cityService) {
        this.regionService = regionService;
        this.provinceService = provinceService;
        this.cityService = cityService;
    }

    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = regionService.findAll();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/regions/{regionId}/provinces")
    public ResponseEntity<List<Province>> getProvincesByRegionId(@PathVariable("regionId") long regionId) {
        List<Province> provinces = provinceService.getProvincesByRegionId(regionId);
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }

    @GetMapping("/provinces/{provinceId}/cities")
    public ResponseEntity<List<City>> getCitiesByProvinceId(@PathVariable("provinceId") long provinceId) {
        List<City> cities = cityService.findCitiesByProvinceId(provinceId);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
