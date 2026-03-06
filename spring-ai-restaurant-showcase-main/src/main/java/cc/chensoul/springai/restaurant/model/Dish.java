package cc.chensoul.springai.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    
    private Long id;
    private String name;
    private String description;
    private String cuisine;
    private Double price;
    private String category;
    private List<String> ingredients;
    private String dietaryInfo;
    private Integer calories;
    private String preparationTime;
    private String difficulty;
}
