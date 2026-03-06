package cc.chensoul.springai.restaurant.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequest {

    private String location;
    private String cuisine;
    private String priceRange;
    private List<String> dietaryRestrictions;
    private String occasion;
    private Integer groupSize;
    private String timeOfDay;
    private List<String> preferences;

}
