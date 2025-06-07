package com.lorelis.cotizacion.repository.user;

import com.lorelis.cotizacion.enums.RoleList;
import com.lorelis.cotizacion.model.usuario.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleList name);
}
