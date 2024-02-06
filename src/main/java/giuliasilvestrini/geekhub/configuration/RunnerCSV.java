package giuliasilvestrini.geekhub.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class RunnerCSV implements CommandLineRunner {

    private final ImportCSV importCSV;

    @Autowired
    public RunnerCSV(ImportCSV importCSV) {
        this.importCSV = importCSV;
    }

    @Override
    public void run(String... args) throws Exception {
        String regionFilename = "./CSV/gi_regioni.csv";
        String provinceFilename = "./CSV/gi_province.csv";
        String cityFilename = "./CSV/gi_comuni.csv";

        importCSV.importDataFromCSV(regionFilename, provinceFilename, cityFilename);


    }
}

