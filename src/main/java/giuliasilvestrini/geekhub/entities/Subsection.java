package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subsection {
    @Id
    @GeneratedValue
    private long subsectionId;
    private String subsectionTitle;
    private String subsectionDescription;
    private String subsectionTime;

    @JsonIgnore
    @ManyToOne
    private Section section;
}
