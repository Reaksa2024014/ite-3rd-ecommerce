package co.istad.reaksa.ecommerce.features.auth;

import co.istad.reaksa.ecommerce.features.auth.dto.RegisterRequest;
import co.istad.reaksa.ecommerce.features.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
}