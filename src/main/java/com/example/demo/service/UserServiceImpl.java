package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CustomerRegistrationDTO;
import com.example.demo.dto.request.UserLoginDTO;
import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationDTO;
import com.example.demo.dto.response.JwtResponseDTO;
import com.example.demo.dto.response.UserDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;

@Service
public class UserServiceImpl implements UserService {

   // Call UserRepository to implement service
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private RoleRepository roleRepository;

   @Autowired
   PasswordEncoder passwordEncoder;

   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   JwtUtils jwtUtils;

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
   // ---

   // ---
   @Override
   public boolean addStaff(UserStaffRegistrationDTO userStaffRegistrationDTO) {

      if (userRepository.existsByUsername(userStaffRegistrationDTO.getUsername())) {
         throw new IllegalArgumentException("This username is already taken");
      }

      if (userRepository.existsByEmail(userStaffRegistrationDTO.getEmail())) {
         throw new IllegalArgumentException("This email already in use");
      }

      User staffUser = new User();
      staffUser.setUsername(userStaffRegistrationDTO.getUsername());
      staffUser.setEmail(userStaffRegistrationDTO.getEmail());

      // Temporary password is assigned
      String tempPassword = "staff";
      staffUser.setPassword(passwordEncoder.encode(tempPassword)); // Encode temporary password before setting

      staffUser.setAddress(userStaffRegistrationDTO.getAddress());
      staffUser.setTelephoneNumber(userStaffRegistrationDTO.getTelephoneNumber());

      Set<Role> roles = new HashSet<>();

      for (String roleName : userStaffRegistrationDTO.getExpectingRoles()) {
         Role role = roleRepository.findByRoleName(roleName)
               .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));
         roles.add(role);
      }
      staffUser.setRoles(roles);

      userRepository.save(staffUser);

      return true;
   }
   // ---

   // ---
   @Override
   public boolean addCustomer(CustomerRegistrationDTO customerRegistrationDTO) {

      // Check if the username already exists
      if (userRepository.existsByUsername(customerRegistrationDTO.getUsername())) {
         throw new IllegalArgumentException("This username is already taken");
      }

      // Check if the email already exists
      if (userRepository.existsByEmail(customerRegistrationDTO.getEmail())) {
         throw new IllegalArgumentException("This email is already in use");
      }

      // Roles are set as ["Customer"] at user registration
      // find matching Role entity by matching role name "Customer"
      Set<String> roleNames = Set.of("Customer");
      Set<Role> roles = roleNames.stream()
            .map(roleName -> roleRepository.findByRoleName(roleName)
                  .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName))) // Throws exception if
                                                                                                 // role not found
            .collect(Collectors.toSet());

      // Create a new user object
      User newCustomer = new User();
      newCustomer.setUsername(customerRegistrationDTO.getUsername());
      newCustomer.setEmail(customerRegistrationDTO.getEmail());
      newCustomer.setPassword(passwordEncoder.encode(customerRegistrationDTO.getPassword())); // Encode password before
                                                                                              // setting
      newCustomer.setAddress(customerRegistrationDTO.getAddress());
      newCustomer.setTelephoneNumber(customerRegistrationDTO.getTelephoneNumber());
      newCustomer.setRoles(roles);

      // Save the new customer user
      userRepository.save(newCustomer);

      return true;
   }
   // ---

   // ---
   @Override
   public JwtResponseDTO loginUser(UserLoginDTO loginRequest) {
      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(),
                  loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Generate JWT token
      String jwtToken = jwtUtils.generateJwtToken(authentication);

      User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Username is not found: "));

      //Get roles of user and extract roleName from it and assign it to Set<String>
      Set<String> roleNames = user.getRoles().stream()
                            .map(Role::getRoleName) // Extract roleName from Role
                            .collect(Collectors.toSet()); // Collect to a Set

      JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(
            jwtToken, user.getId(), user.getUsername(), user.getEmail(), roleNames);

      return jwtResponseDTO;
   }
   // ---
}
