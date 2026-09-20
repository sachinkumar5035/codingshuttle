package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    @NotBlank(message = "Name of the department cannot be blank")
    @Size(min = 3, max = 10, message = "Number of characters in department name should be in the range: [3, 10]")
    private String title;

    private Boolean isActive;

    private String createdAt;
}
