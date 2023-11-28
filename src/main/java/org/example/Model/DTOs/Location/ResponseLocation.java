package org.example.Model.DTOs.Location;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseLocation {
    @NotBlank
    private String text;
    private String address;
    @NotBlank
    private String fullPlaceName;
    @NotEmpty
    private List<Double> coordinates;

}
