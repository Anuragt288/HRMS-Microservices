package com.hrms.auth;

import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(
			RoleRepository roleRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {

			// ADMIN role create karo
			Role adminRole = roleRepository.findByRoleName("ADMIN")
					.orElseGet(() -> roleRepository.save(
							new Role("ADMIN", "Administrator role")
					));

			// EMPLOYEE role create karo
			Role employeeRole = roleRepository.findByRoleName("EMPLOYEE")
					.orElseGet(() -> roleRepository.save(
							new Role("EMPLOYEE", "Employee role")
					));

			// Admin user create karo
			if (!userRepository.existsByUsername("admin")) {

				User admin = new User(
						"admin",
						"admin@hrms.com",
						passwordEncoder.encode("Admin@123"),
						"ACTIVE"
				);

				admin.addRole(adminRole);

				userRepository.save(admin);

				System.out.println("Default admin user created successfully.");
			} else {
				System.out.println("Admin user already exists.");
			}
			// Employee user create karo
			if (!userRepository.existsByUsername("employee")) {

				User employee = new User(
						"employee",
						"employee@hrms.com",
						passwordEncoder.encode("Employee@123"),
						"ACTIVE"
				);

				employee.addRole(employeeRole);

				userRepository.save(employee);

				System.out.println("Default employee user created successfully.");
			} else {
				System.out.println("Employee user already exists.");
			}
		};
	}
}