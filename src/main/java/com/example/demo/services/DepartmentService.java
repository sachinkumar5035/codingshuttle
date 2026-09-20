package com.example.demo.services;


import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

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
        isExistsByEmployeeId(departmentId); // check if department exists or not
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId).get();
        updates.forEach((field,value)->{
            Field fieldToBeUpdated = ReflectionUtils.getRequiredField(DepartmentEntity.class,field);
            fieldToBeUpdated.setAccessible(true); // because all the field of the entity is private so making them accessible to update
            ReflectionUtils.setField(fieldToBeUpdated,departmentEntity,value);
        });
        return modelMapper.map(departmentRepository.save(departmentEntity),DepartmentDTO.class);
    }

    void isExistsByEmployeeId(Long departmentId){
        boolean isExists = departmentRepository.existsById(departmentId);
        if(!isExists)
            throw new ResourceNotFoundException("Department not found with id: "+departmentId);
    }
}
