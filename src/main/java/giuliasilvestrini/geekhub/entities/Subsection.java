package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(columnDefinition = "TEXT")
    private String subsectionDescription;
    private String subsectionTime;

    @JsonIgnore
    @ManyToOne
    private Section section;

    @ManyToOne
    private User creator;
}
