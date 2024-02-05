package giuliasilvestrini.geekhub.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

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
    private UUID sectionId;
    private String sectionTitle;
    private String sectionSubtitle;
}
