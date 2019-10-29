package com.bridgelabz.fundooapp.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooapp.dto.LoginDTO;
import com.bridgelabz.fundooapp.dto.RegisterDTO;
import com.bridgelabz.fundooapp.dto.ResetPasswordDTO;
import com.bridgelabz.fundooapp.dto.UserDetails;
import com.bridgelabz.fundooapp.exception.UserException;
import com.bridgelabz.fundooapp.model.User;
import com.bridgelabz.fundooapp.repository.UserRepository;
import com.bridgelabz.fundooapp.util.EmailUtil;
import com.bridgelabz.fundooapp.util.GenerateURL;
import com.bridgelabz.fundooapp.util.JWTUtil;

@Service
public class UserRegistrationAndLogin implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User register(RegisterDTO registerDTO) {
		if (userRepository.findByEmailId(registerDTO.getEmailId()).isPresent()
				|| userRepository.findByMobileNumber(registerDTO.getMobileNumber()).isPresent())
			throw new UserException(400, "User already present");

		registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		User user = modelMapper.map(registerDTO, User.class);
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		user.setCreatedDate(currentTime);
		user.setUpdatedDate(currentTime);
		user = userRepository.save(user);
		String url = GenerateURL.getUrl("verify", user.getId());
		EmailUtil.sendEmail(registerDTO.getEmailId(), "Email Verification ", url);
		return user;
	}

	@Override
	public String verifyToken(String token) {
		Long id = JWTUtil.verifyToken(token);
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(400, "Invalid token"));
		user.setVerified(true);
		userRepository.save(user);
		return "Token Verified Successfully.";
	}

	@Override
	public UserDetails login(LoginDTO loginDTO) {
		UserDetails userDetails = new UserDetails();
		User user = userRepository.findByEmailId(loginDTO.getEmailId())
				.orElseThrow(() -> new UserException(404, "User not registered."));
		if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()) && user.isVerified()) {
			userDetails.setEmailId(user.getEmailId());
			userDetails.setFirstName(user.getFirstName());
			userDetails.setLastName(user.getLastName());
			userDetails.setToken(JWTUtil.generateToken(user.getId()));
			return userDetails;
		} else if (!user.isVerified()) {
			String url = GenerateURL.getUrl("verify", user.getId());
			EmailUtil.sendEmail(loginDTO.getEmailId(), "Email Verification", url);
			throw new UserException(404, "User is not validated.");
		} else {
			throw new UserException(401, "Incorrect Details");
		}
	}

	@Override
	public String forgetPassword(String emailId) {
		User user = userRepository.findByEmailId(emailId)
				.orElseThrow(() -> new UserException(404, "User not registered."));
		String url = GenerateURL.getUrl("resetpassword", user.getId());
		EmailUtil.sendEmail(emailId, "Password Reset", url);
		return "Password reset link has been sent to the registered email.";
	}

	@Override
	public String resetPassword(ResetPasswordDTO resetPasswordDTO, String token) {
		String message;
		Long id = JWTUtil.verifyToken(token);
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(404, "User Not found."));
		if (resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
			user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
			user.setUpdatedDate(LocalDateTime.now());
			userRepository.save(user);
			message = "Password successfully updated. Please login.";
		} else {
			throw new UserException(401, "Please enter the password fields correctly");
		}
		return message;
	}

	@Override
	public Long getUserByEmailId(String emailId, String token) {
		JWTUtil.verifyToken(token);
		return userRepository.findByEmailId(emailId).map(user -> {
			return user.getId();
		}).orElseThrow(() -> new UserException(404, "User not found"));
	}

}
