package giuliasilvestrini.geekhub.configuration;

import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import giuliasilvestrini.geekhub.services.locationServices.CityService;
import giuliasilvestrini.geekhub.services.locationServices.ProvinceService;
import giuliasilvestrini.geekhub.services.locationServices.RegionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component
public class ImportCSV {


    private final RegionService regionService;
    private final ProvinceService provinceService;
    private final CityService cityService;

    @Autowired
    public ImportCSV(RegionService regionService, ProvinceService provinceService, CityService cityService) {
        this.regionService = regionService;
        this.provinceService = provinceService;
        this.cityService = cityService;


    }

    public void importDataFromCSV(String regionFilename, String provinceFilename, String cityFilename) {
//        importRegionsFromCSV(regionFilename);
//        importProvincesFromCSV(provinceFilename);
//        importCitiesFromCSV(cityFilename);
    }


    private void importRegionsFromCSV(String filename) {
        try (FileReader reader = new FileReader(filename);
             CSVParser csvParser = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                String regionName = record.get("denominazione_regione");
                long regionCode = Long.parseLong(record.get("codice_regione"));

                Region region = new Region();
                region.setRegionName(regionName);
                region.setRegionCode(regionCode);

                System.out.println(region);
                regionService.saveRegion(region);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importProvincesFromCSV(String filename) {
        try (FileReader reader = new FileReader(filename);
             CSVParser csvParser = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                String sigla = record.get("sigla_provincia");
                String name = record.get("denominazione_provincia");
                long regionCode = Long.parseLong(record.get("codice_regione"));

                // Trova la regione corrispondente dal codice
                Region region = regionService.findByCode(regionCode);

                Province province = new Province();
                province.setProvinceName(name);
                province.setSigla(sigla);
                province.setRegion(region);

                System.out.println(province);
                provinceService.saveProvince(province);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void importCitiesFromCSV(String filename) {
        try (FileReader reader = new FileReader(filename);
             CSVParser csvParser = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : csvParser) {
                String sigla = record.get("sigla_provincia");
                int cityCode = Integer.parseInt(record.get("codice_istat"));
                String cityName = record.get("denominazione_ita");

                Province province = provinceService.findBySigla(sigla);

                if (province != null) {
                    City city = new City();
                    city.setProvince(province);
                    city.setCityCode(cityCode);
                    city.setCityName(cityName);
//                    System.out.println(city);
                    cityService.saveCity(city);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
