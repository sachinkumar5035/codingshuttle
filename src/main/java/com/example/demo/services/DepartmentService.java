package com.example.demo.services;


import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;


    public DepartmentService(DepartmentRepository departmentRepository,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.departmentRepository = departmentRepository;
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = modelMapper.map(departmentDTO, DepartmentEntity.class);
        DepartmentEntity savedDepartment = departmentRepository.save(departmentEntity);
        return modelMapper.map(savedDepartment, DepartmentDTO.class);
    }

    public DepartmentDTO getDepartment(Long departmentId) {
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        return modelMapper.map(departmentEntity, DepartmentDTO.class);
    }


    public DepartmentDTO updateDepartmentPartially(Map<String, Object> updates, Long departmentId) {
        isExistsByDepartmentById(departmentId); // check if department exists or not
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId).get();
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.getRequiredField(DepartmentEntity.class, field);
            fieldToBeUpdated.setAccessible(true);

            Object convertedValue = value;
            Class<?> fieldType = fieldToBeUpdated.getType();

            if (fieldType == java.time.LocalDate.class && value instanceof String) {
                convertedValue = java.time.LocalDate.parse((String) value);
            } else if (fieldType == Boolean.class && value instanceof String) {
                convertedValue = Boolean.parseBoolean((String) value);
            }

            ReflectionUtils.setField(fieldToBeUpdated, departmentEntity, convertedValue);
        });
        return modelMapper.map(departmentRepository.save(departmentEntity), DepartmentDTO.class);
    }

    void isExistsByDepartmentById(Long departmentId){
        boolean isExists = departmentRepository.existsById(departmentId);
        if(!isExists)
            throw new ResourceNotFoundException("Department not found with id: "+departmentId);
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        return departmentEntities.stream()
                .map(departmentEntity -> modelMapper.map(departmentEntity,DepartmentDTO.class))
                .collect(Collectors.toList());
    }
}
