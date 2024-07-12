package com.lamdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityThirdLearnApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityThirdLearnApplication.class, args);
	}
	
//	@Override
//	public void run(String... args) throws Exception {
////		User adminAccount = userRepository.findByRole(Role.ADMIN);
////		
////		if(adminAccount == null) {
////			User user = new User();
////			
////			user.setEmail("admin");
////			user.setFirstName("Lam");
////			user.setLastName("Do");
////			user.setRole(Role.ADMIN);
////			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
////			userRepository.save(user);
////		}
//		System.out.println("Xin chao");
//	}

}

	