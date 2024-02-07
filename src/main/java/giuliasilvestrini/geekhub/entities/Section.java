package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @GeneratedValue
    private long sectionId;
    private String sectionTitle;
    private String sectionSubtitle;
    private String sectionImage;

    @ManyToOne
    private Convention convention;

    @JsonIgnore
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Subsection> subsectionList;
}
