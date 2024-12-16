package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.common.customException.FirstLoginCustomException;
import com.example.demo.dto.request.CustomerRegistrationDTO;
import com.example.demo.dto.request.UserFindRequestDTO;
import com.example.demo.dto.request.UserLoginRequestDTO;
import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationRequestDTO;
import com.example.demo.dto.response.JwtResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;

@Service
public class UserServiceImpl implements UserService {

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

   private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

   // ***
   private UserResponseDTO toUserResponseDTO(User user) {
      UserResponseDTO dto = new UserResponseDTO();
      dto.setId(user.getId());
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());
      dto.setAddress(user.getAddress());
      dto.setTelephoneNumber(user.getTelephoneNumber());

      Set<String> userRoleNames = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
      dto.setRoleNames(userRoleNames);

      return dto;
   }
   // ***

   // ---
   @Override
   public List<UserResponseDTO> getAllUsers() {
      List<User> users = userRepository.findAll();
      List<UserResponseDTO> userResponseDTOs = users.stream().map(this::toUserResponseDTO).collect(Collectors.toList());
      return userResponseDTOs;
   }
   // ---

   // ---
   @Override
   public UserResponseDTO getUserById(long id) {
      User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User is not found" + id));
      return toUserResponseDTO(user);
   }
   // ---

   // ---
   @Override
   public UserResponseDTO updateUserProfile(long id, UserRequestDTO userRequestDTO) {
      // get existing user
      User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User is not found" + id));

      // change existing user according to our requirement
      existingUser.setUsername(userRequestDTO.getUsername());
      existingUser.setAddress(userRequestDTO.getAddress());
      existingUser.setTelephoneNumber(userRequestDTO.getTelephoneNumber());
      existingUser.setEmail(userRequestDTO.getEmail());
      // Role update is not allowed for any user.

      return toUserResponseDTO(userRepository.save(existingUser));
   }
   // ---

   // ---
   @Override
   public void deleteUser(long id) {
      if (!userRepository.existsById(id)) {
         throw new IllegalArgumentException("User is not found with id: " + id);
      }
      userRepository.deleteById(id);
      logger.info("User with id {} was deleted.", id);
   }
   // ---

   // ---
   @Override
   public boolean isPasswordReset(UserPaswordResetRequestDTO userPaswordResetRequestDTO) {
      // get existing user
      User existingUser = userRepository.findById(userPaswordResetRequestDTO.getId())
            .orElseThrow(() -> new NoSuchElementException("User is not found" + userPaswordResetRequestDTO.getId()));

      // Encode temporary password before setting
      existingUser.setPassword(passwordEncoder.encode(userPaswordResetRequestDTO.getNewPassword()));
      existingUser.setFirstLogin(false); // Update the firstLogin flag to false

      // save user
      userRepository.save(existingUser);

      return true;
   }
   // ---

   // ---
   @Override
   public UserResponseDTO addStaff(UserStaffRegistrationRequestDTO userStaffRegistrationDTO) {

      if (userRepository.existsByUsername(userStaffRegistrationDTO.getUsername())) {
         throw new IllegalArgumentException(
               "The username '" + userStaffRegistrationDTO.getUsername() + "' is already taken.");
      }

      if (userRepository.existsByEmail(userStaffRegistrationDTO.getEmail())) {
         throw new IllegalArgumentException(
               "The email '" + userStaffRegistrationDTO.getEmail() + "' is already in use.");
      }

      User newStaffUserToSave = new User();
      newStaffUserToSave.setUsername(userStaffRegistrationDTO.getUsername());
      newStaffUserToSave.setEmail(userStaffRegistrationDTO.getEmail());

      // Temporary password is assigned
      String tempPassword = "staff";
      newStaffUserToSave.setPassword(passwordEncoder.encode(tempPassword)); // Encode temporary password before setting

      newStaffUserToSave.setAddress(userStaffRegistrationDTO.getAddress());
      newStaffUserToSave.setTelephoneNumber(userStaffRegistrationDTO.getTelephoneNumber());

      // Assign requested roles for new staff member
      Set<Role> roles = new HashSet<>();

      for (Long roleId : userStaffRegistrationDTO.getExpectingRoleIds()) {
         Role role = roleRepository.findById(roleId)
               .orElseThrow(() -> new IllegalArgumentException("Role is not found : " + roleId));

         // Validate roles
         if ("Admin".equals(role.getRoleName()) || "Customer".equals(role.getRoleName())) {
            throw new IllegalArgumentException("Unauthorized role assignment: '" + role.getRoleName() + "'.");
         }

         roles.add(role);
      }

      newStaffUserToSave.setRoles(roles);

      userRepository.save(newStaffUserToSave);

      return toUserResponseDTO(newStaffUserToSave);
   }
   // ---

   // ---
   @Override
   public UserResponseDTO addCustomer(CustomerRegistrationDTO customerRegistrationDTO) {

      // Check if the username already exists
      if (userRepository.existsByUsername(customerRegistrationDTO.getUsername())) {
         throw new IllegalArgumentException(
               "The username '" + customerRegistrationDTO.getUsername() + "' is already taken.");
      }

      // Check if the email already exists
      if (userRepository.existsByEmail(customerRegistrationDTO.getEmail())) {
         throw new IllegalArgumentException(
               "The email '" + customerRegistrationDTO.getEmail() + "' is already in use.");
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
      User newCustomerToSave = new User();
      newCustomerToSave.setUsername(customerRegistrationDTO.getUsername());
      newCustomerToSave.setEmail(customerRegistrationDTO.getEmail());
      newCustomerToSave.setPassword(passwordEncoder.encode(customerRegistrationDTO.getPassword())); // Encode password before
                                                                                              // setting
      newCustomerToSave.setAddress(customerRegistrationDTO.getAddress());
      newCustomerToSave.setTelephoneNumber(customerRegistrationDTO.getTelephoneNumber());
      newCustomerToSave.setRoles(roles);

      /*
       * Customer can create their own password,
       * no need to reset it at the first login
       */
      newCustomerToSave.setFirstLogin(false);

      // Save the new customer user
      userRepository.save(newCustomerToSave);

      return toUserResponseDTO(newCustomerToSave);
   }
   // ---

   // ---
   @Override
   public JwtResponseDTO loginUser(UserLoginRequestDTO loginRequest) {
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

      // Get roles of user and extract roleName from it and assign it to Set<String>
      Set<String> roleNames = user.getRoles().stream()
            .map(Role::getRoleName) // Extract roleName from Role
            .collect(Collectors.toSet()); // Collect to a Set

      // Default temporary password is set at the creation of admin and staff roles
      // Password reset is needed only for this roles at the first time login
      if (user.isFirstLogin() && !roleNames.contains("Customer")) {
         throw new FirstLoginCustomException("Password reset required for first login");
      }

      JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(
            jwtToken, user.getId(), user.getUsername(), user.getEmail(), roleNames);

      return jwtResponseDTO;
   }
   // ---

   // ---
   @Override
   public UserResponseDTO getUserByUsername(UserFindRequestDTO userFindRequestDTO) {
      User user = userRepository.findByUsername(userFindRequestDTO.getUsername()).orElseThrow(() -> new NoSuchElementException("User is not found bu username :" + userFindRequestDTO.getUsername()));
      
      return toUserResponseDTO(user);
   }
   // ---
}
