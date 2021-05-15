package com.springstudy.project.myweddingplanner.util;

import org.springframework.beans.BeanUtils;

public class EntityDtoConverter {
    public static <T> T convertDtoToEntity(Object dto, T entity) {
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static <T> T convertEntityToDto(Object entity, T dto) {
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
