package giuliasilvestrini.geekhub.entities.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;



    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public class Region {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String regionName;
        private long regionCode;

        @JsonIgnore
        @OneToMany(mappedBy = "region", fetch = FetchType.EAGER)
        private List<Province> provinceList;

    }
