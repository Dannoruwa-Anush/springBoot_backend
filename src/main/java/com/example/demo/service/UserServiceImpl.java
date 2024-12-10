package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationDTO;
import com.example.demo.dto.response.UserDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

   // Call UserRepository to implement service
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private RoleRepository roleRepository;

   // ---
   @Override
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }
   // ---

   // ---
   @Override
   public User getUserById(long id) {
      return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User is not found" + id));
   }
   // ---

   // ---
   @Override
   public User saveUser(User user) {
      return userRepository.save(user);
   }
   // ---

   // ---
   @Override
   public User updateUserProfile(long id, UserDTO userDTO) {
      // get existing user
      User existingUser = getUserById(id);

      // change existing user according to our requirement
      existingUser.setUsername(userDTO.getUsername());
      existingUser.setAddress(userDTO.getAddress());
      existingUser.setTelephoneNumber(userDTO.getTelephoneNumber());
      existingUser.setEmail(userDTO.getEmail());

      return userRepository.save(existingUser);
   }
   // ---

   // ---
   @Override
   public void deleteUser(long id) {
      userRepository.deleteById(id);
   }
   // ---

   @Override
   public boolean isPasswordReset(UserPaswordResetRequestDTO userPaswordResetRequestDTO) {
      // get existing user
      User existingUser = getUserById(userPaswordResetRequestDTO.getId());

      if (existingUser == null) {
         // If existingUser does not exist, return false
         return false;
      }

      // update - password encoding is needed / spring security
      existingUser.setPassword(userPaswordResetRequestDTO.getNewPassword());

      // save user
      userRepository.save(existingUser);

      return true;
   }

   @Override
   public User addStaff(UserStaffRegistrationDTO userStaffRegistrationDTO) {
      User staffUser = new User();
      staffUser.setUsername(userStaffRegistrationDTO.getUsername());
      staffUser.setEmail(userStaffRegistrationDTO.getEmail());

      //update - need password encoder
      staffUser.setPassword("123"); //default password is set 

      staffUser.setAddress(userStaffRegistrationDTO.getAddress());
      staffUser.setTelephoneNumber(userStaffRegistrationDTO.getTelephoneNumber());

      Set<Role> roles = new HashSet<>();

      for (String roleName : userStaffRegistrationDTO.getExpectingRoles()) {
         Role role = roleRepository.findByRoleName(roleName)
               .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));
         roles.add(role);
      }
      staffUser.setRoles(roles);

      return userRepository.save(staffUser);
   }
}
