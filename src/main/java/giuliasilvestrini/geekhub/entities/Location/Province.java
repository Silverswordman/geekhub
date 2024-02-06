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

public class Province {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String provinceName;
    private String sigla;





    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    @JsonIgnore
    @OneToMany(mappedBy = "province")
    private List<City> cityList;


    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", provinceName='" + provinceName + '\'' +
                ", sigla='" + sigla + '\'' +
                '}';
    }
}