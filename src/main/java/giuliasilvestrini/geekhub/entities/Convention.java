package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Convention {
    @Id
    @GeneratedValue
    private UUID conventionId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String logo;
    private String coverImage;

    @ManyToOne
    private Region region;
    @ManyToOne
    private Province province;
    @ManyToOne
    private City city;

//    private String region;
//    private String province;
//    private String city;

    private String street;
    private String houseNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "convention", cascade = CascadeType.ALL)
    private List<Section> sectionList;
}
