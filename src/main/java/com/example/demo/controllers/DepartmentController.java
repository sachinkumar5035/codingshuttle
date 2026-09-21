package com.example.demo.controllers;


import com.example.demo.dto.DepartmentDTO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody @Valid DepartmentDTO departmentDTO) {
        // Call the service to create a department
        DepartmentDTO savedDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long departmentId) {
        DepartmentDTO department = departmentService.getDepartment(departmentId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PatchMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> updateDepartmentPartially(@RequestBody Map<String,Object> updates,@PathVariable Long departmentId){
        DepartmentDTO departmentDTO = departmentService.updateDepartmentPartially(updates,departmentId);
        if(departmentDTO == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(departmentDTO);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }


}
