package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import giuliasilvestrini.geekhub.entities.Location.City;
import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.Location.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import jakarta.persistence.*;


import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private String site;
    private String logo;
    private String coverImage;
    @ManyToOne
    private Region region;
    @ManyToOne
    private Province province;
    @ManyToOne
    private City city;

    private String address;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @JsonIgnore
    @OneToMany(mappedBy = "convention", cascade = CascadeType.ALL)
    private List<Section> sectionList;
}
