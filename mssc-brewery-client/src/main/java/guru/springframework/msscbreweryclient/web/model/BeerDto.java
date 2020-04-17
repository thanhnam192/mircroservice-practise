package guru.springframework.msscbreweryclient.web.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Created by jt on 2019-04-20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto implements Serializable {

    static final long serialVersionUID = -5815566940065181210L;


    private UUID id;


    private Integer version;


    private OffsetDateTime createdDate;


    private OffsetDateTime lastModifiedDate;


    private String beerName;


    private BeerStyleEnum beerStyle;

    private String upc;


    private BigDecimal price;

    private Integer quantityOnHand;

}
