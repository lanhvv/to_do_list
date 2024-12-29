package com.example.demo.data.mapper;

import com.example.demo.data.response.RoleDTORS;
import com.example.demo.data.response.UserDTORS;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ToDoUserMapper {

    @Mappings({
            @Mapping(source = "roles", target ="roles", qualifiedByName = "roleSetToList")
    })
    UserDTORS UserToUserDTORS(User user);

    @Named("roleSetToList")
    default List<RoleDTORS> roleSetToList(Set<Role> roles) {
        return roles.stream().map(role -> new RoleDTORS(role.getCode(), role.getName())).collect(Collectors.toList());
    }
}
