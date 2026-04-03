package com.pfc.inventorytrackerjpa.services;

import com.pfc.inventorytrackerjpa.dto.UserLocationRoleRequest;
import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.entities.User;
import com.pfc.inventorytrackerjpa.entities.UserLocationRole;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserLocationRoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLocationRoleServiceTest {

    @Mock
    private UserLocationRoleRepository userLocationRoleRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private LocationRepository locationRepo;

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private UserLocationRoleService service;

    private User user;
    private Location location;
    private Role locationRole;
    private UserLocationRoleRequest request;

    @BeforeEach
    void setUp() {
        user = new User("jdoe", "secret", "Jane Doe", "E123", true);
        user.setId(1L);

        location = new Location();
        location.setId(2L);
        location.setName("Main Pantry");

        locationRole = new Role();
        locationRole.setId(3L);
        locationRole.setRoleName("LOCATION_VIEWER");
        locationRole.setScope(RoleScope.LOCATION);

        request = new UserLocationRoleRequest();
        request.setUserId(1L);
        request.setLocationId(2L);
        request.setRoleId(3L);
    }

    @Test
    void create_shouldPersistAssignmentAndLinkUserToLocation() throws Exception {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepo.findById(2L)).thenReturn(Optional.of(location));
        when(roleRepo.findById(3L)).thenReturn(Optional.of(locationRole));
        when(userLocationRoleRepo.existsByUserIdAndLocationIdAndRoleId(1L, 2L, 3L)).thenReturn(false);
        when(userLocationRoleRepo.save(any(UserLocationRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserLocationRole result = service.create(request);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(location, result.getLocation());
        assertEquals(locationRole, result.getRole());
        assertTrue(user.getLocations().contains(location));
        verify(userRepo).save(user);
        verify(userLocationRoleRepo).save(any(UserLocationRole.class));
    }

    @Test
    void create_shouldThrowWhenAssignmentAlreadyExists() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepo.findById(2L)).thenReturn(Optional.of(location));
        when(roleRepo.findById(3L)).thenReturn(Optional.of(locationRole));
        when(userLocationRoleRepo.existsByUserIdAndLocationIdAndRoleId(1L, 2L, 3L)).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> service.create(request));

        verify(userLocationRoleRepo, never()).save(any(UserLocationRole.class));
    }

    @Test
    void create_shouldThrowWhenRoleIsNotLocationScoped() {
        Role appRole = new Role();
        appRole.setId(3L);
        appRole.setRoleName("APP_ADMIN");
        appRole.setScope(RoleScope.APP);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepo.findById(2L)).thenReturn(Optional.of(location));
        when(roleRepo.findById(3L)).thenReturn(Optional.of(appRole));

        assertThrows(InvalidRoleException.class, () -> service.create(request));

        verify(userLocationRoleRepo, never()).save(any(UserLocationRole.class));
    }

    @Test
    void edit_shouldThrowWhenAssignmentDoesNotExist() {
        when(userLocationRoleRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> service.edit(99L, request));
    }

    @Test
    void delete_shouldReturnFalseWhenAssignmentMissing() {
        when(userLocationRoleRepo.findById(44L)).thenReturn(Optional.empty());

        boolean deleted = service.delete(44L);

        assertFalse(deleted);
        verify(userLocationRoleRepo, never()).delete(any(UserLocationRole.class));
    }

    @Test
    void delete_shouldRemoveUserLocationWhenLastAssignmentRemoved() {
        user.addLocation(location);

        UserLocationRole assignment = new UserLocationRole(user, location, locationRole);
        assignment.setId(10L);

        when(userLocationRoleRepo.findById(10L)).thenReturn(Optional.of(assignment));
        when(userLocationRoleRepo.countByUserIdAndLocationId(1L, 2L)).thenReturn(0L);

        boolean deleted = service.delete(10L);

        assertTrue(deleted);
        assertFalse(user.getLocations().contains(location));
        verify(userLocationRoleRepo).delete(assignment);
        verify(userRepo).save(user);
    }

    @Test
    void getByUser_shouldDelegateToRepository() {
        service.getByUser(1L);
        verify(userLocationRoleRepo).findAllByUserId(1L);
    }

    @Test
    void getByLocation_shouldDelegateToRepository() {
        service.getByLocation(2L);
        verify(userLocationRoleRepo).findAllByLocationId(2L);
    }

    @Test
    void getAll_shouldDelegateToRepository() {
        service.getAll();
        verify(userLocationRoleRepo).findAll();
    }
}
