package com.microservices.auth;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.microservices.auth.entity.Permission;
import com.microservices.auth.entity.User;
import com.microservices.auth.repository.PermissionRepository;
import com.microservices.auth.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PermissionRepository permissionRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		return args -> {
			initUsers(userRepository, permissionRepository, bCryptPasswordEncoder);
		};
	}

	public void initUsers(UserRepository userRepository, PermissionRepository permissionRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		Permission permission = null;
		Permission findPermission = permissionRepository.findByDescription("Admin");
		if (findPermission == null) {
			permission = new Permission();
			permission.setDescription("Admin");
			permission = permissionRepository.save(permission);
		} else {
			permission = findPermission;
		}
		
		User admin = new User();
		admin.setUserName("oliveira");
		admin.setPassword(bCryptPasswordEncoder.encode("123456"));
		admin.setAccountNonExpired(true);
		admin.setAccountNonLocked(true);
		admin.setCredentialsNonExpired(true);
		admin.setEnabled(true);
		admin.setPermissions(Arrays.asList(permission));
		
		User find = userRepository.findByUserName("oliveira");
		if (find == null) {
			userRepository.save(admin);
		}
	}
}
