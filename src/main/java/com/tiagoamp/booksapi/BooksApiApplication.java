package com.tiagoamp.booksapi;

import com.tiagoamp.booksapi.model.AppUser;
import com.tiagoamp.booksapi.model.Role;
import com.tiagoamp.booksapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;

@SpringBootApplication
public class BooksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			// roles
			userService.save(new Role(null, "ROLE_USER"));
			userService.save(new Role(null, "ROLE_ADMIN"));
			userService.save(new Role(null, "ROLE_SUPER_ADMIN"));
			// users
			userService.save(new AppUser(null, "James Kirk", "kirk", "123456", new ArrayList<>()));
			userService.save(new AppUser(null, "Spock", "spock", "123456", new ArrayList<>()));
			userService.save(new AppUser(null, "Leonard McCoy", "mccoy", "123456", new ArrayList<>()));
			userService.save(new AppUser(null, "Montgomery Scott", "scott", "123456", new ArrayList<>()));
			// roles -> users
			userService.addRoleToUser("kirk", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("kirk", "ROLE_ADMIN");
			userService.addRoleToUser("spock", "ROLE_ADMIN");
			userService.addRoleToUser("spock", "ROLE_USER");
			userService.addRoleToUser("mccoy", "ROLE_USER");
			userService.addRoleToUser("scott", "ROLE_USER");
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

}
