package cc.chensoul.springai.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    
    private Long id;
    private String name;
    private String cuisine;
    private String location;
    private Double rating;
    private String description;
    private String priceRange;
    private String[] features;
}
