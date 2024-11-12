package com.example.project.utils;

import com.example.project.entities.RoleEntity;
import com.example.project.enums.RoleEnum;
import com.example.project.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.CUSTOMER, RoleEnum.ADMIN, RoleEnum.BUSINESS };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.CUSTOMER, "End User",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.BUSINESS, "Business Person"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            System.out.println(roleName);
            Optional<RoleEntity> optionalRole = roleRepository.findByName(roleName);
            System.out.println(optionalRole);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                RoleEntity roleToCreate = new RoleEntity();
                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }
}
