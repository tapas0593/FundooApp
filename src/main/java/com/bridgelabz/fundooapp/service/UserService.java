package com.bridgelabz.fundooapp.service;

import com.bridgelabz.fundooapp.dto.LoginDTO;
import com.bridgelabz.fundooapp.dto.RegisterDTO;
import com.bridgelabz.fundooapp.dto.ResetPasswordDTO;
import com.bridgelabz.fundooapp.dto.UserDetails;
import com.bridgelabz.fundooapp.model.User;

public interface UserService {

	public User register(RegisterDTO registerDTO);

	public String verifyToken(String token);

	public UserDetails login(LoginDTO loginDTO);

	public String forgetPassword(String emailId);

	public String resetPassword(ResetPasswordDTO resetPasswordDTO, String token);

	public Long getUserByEmailId(String emailId, String token);
}
