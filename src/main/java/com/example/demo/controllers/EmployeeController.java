package com.example.demo.controllers;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {



    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(name = "employeeId") Long id) {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);
        return employeeDTO
                .map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1))
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: "+id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(required = false, name = "inputAge") Integer age,
                                                @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmployee(@RequestBody @Valid EmployeeDTO inputEmployee) {
        EmployeeDTO savedEmployee = employeeService.createNewEmployee(inputEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }


    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody @Valid EmployeeDTO employeeDTO, @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.updateEmployeeById(employeeId, employeeDTO));
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long employeeId) {
        boolean gotDeleted = employeeService.deleteEmployeeById(employeeId);
        if (gotDeleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmployeeById(@RequestBody Map<String, Object> updates,
                                                 @PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.updatePartialEmployeeById(employeeId, updates);
        if (employeeDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/age")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeOrderByAge(){ // sort by age
        List<EmployeeDTO> employeeDTOS = employeeService.getEmployeeOrderByAge();
        if(employeeDTOS == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDTOS);
    }


    // sort by the given field
    @GetMapping("/sortby")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeSortByField(@RequestParam(defaultValue = "id") String sortBy){
//        return ResponseEntity.ok(employeeService.getEmployeeSortByField(Sort.by(sortBy))); // asc order sorting
        return ResponseEntity.ok(employeeService.getEmployeeSortByField(Sort.by(Sort.Direction.DESC,sortBy))); // desc order sorting
    }

//    pagination
    @GetMapping("/pages")
    public ResponseEntity<List<EmployeeDTO>> getEmployee(@RequestParam(defaultValue = "") String sortBy, @RequestParam(defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(employeeService.findAllByPagination(sortBy,pageNumber));
    }

}










