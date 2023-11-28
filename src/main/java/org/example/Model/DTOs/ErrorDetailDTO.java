package org.example.Model.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetailDTO {
    private String title;
    private int status;
    private String detail;
    private String timeStamp;

    private Map<String, List<ValidationErrorDTO>> errors = new HashMap<>();
}
