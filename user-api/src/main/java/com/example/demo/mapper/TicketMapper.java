package com.example.demo.mapper;

import com.example.demo.controller.dto.TicketCreateDTO;
import com.example.demo.domain.Ticket;
import com.example.demo.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "creator", expression = "java(fromId(dto.creatorId()))")
    @Mapping(target = "status", expression = "java(com.example.demo.domain.enums.TicketStatus.valueOf(dto.status()))")
    Ticket toEntity(TicketCreateDTO dto);

    default User fromId(Integer id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}