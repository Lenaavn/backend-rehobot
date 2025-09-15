package com.reho.service.authentication;

import com.reho.web.models.AuthResponse;
import com.reho.web.models.AuthenticationRequest;
import com.reho.web.models.RegisterRequest;

public interface AuthService {
	
	public AuthResponse register (RegisterRequest request);
	
	public AuthResponse authenticate (AuthenticationRequest request);

}
