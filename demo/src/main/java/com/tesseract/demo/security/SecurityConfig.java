package com.tesseract.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tesseract.demo.security.jwt.JwtRequestFilter;
import com.tesseract.demo.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	// @Autowired
	// AuthorizationManager<RequestAuthorizationContext> courseStatisticsAuth; //
	// Custom Authorization Manager inyected

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
                    .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").hasAnyRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/users/**").hasAnyRole("USER", "ADMIN")
					//.requestMatchers(HttpMethod.GET,"/api/v1/users/**").hasAnyRole("ADMIN", "USER")

					.requestMatchers(HttpMethod.POST,"/api/v1/reviews/**").hasAnyRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/v1/reviews/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/reviews/**").hasAnyRole("USER", "ADMIN")
					.requestMatchers(HttpMethod.GET,"/api/v1/reviews/pending").hasAnyRole("ADMIN")

					.requestMatchers(HttpMethod.POST,"/api/v1/enrollments/**").hasAnyRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/v1/enrollments/**").hasAnyRole("USER")

					.requestMatchers(HttpMethod.POST,"/api/v1/courses/**").hasAnyRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/v1/courses/**").hasAnyRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/courses/**").hasAnyRole("USER", "ADMIN")

					// PUBLIC ENDPOINTS
					.requestMatchers("/v3/api-docs.yaml", "/swagger-ui/*", "/swagger-ui.html", "/api/v1/users/me").permitAll()
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/new/**").permitAll()
						.requestMatchers("/signUp/**").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/course/**").permitAll()
						.requestMatchers("/courses/**").permitAll()
						.requestMatchers("/coursesPage/**").permitAll()
						.requestMatchers("/index/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/error-login").permitAll()
						.requestMatchers("/newUser").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/getCourses").permitAll()
						.requestMatchers("/getCoursesByTopic").permitAll()
						.requestMatchers("/showCourse/**").permitAll()
						.requestMatchers("/showCourses/**").permitAll()
						.requestMatchers("/getCoursesByTitle").permitAll()
						.requestMatchers("/findCourses/**").permitAll()
						.requestMatchers("/image/**").permitAll()
						.requestMatchers("/charts").permitAll()
						.requestMatchers("/mostInscribedCathegories").permitAll()
						.requestMatchers("/mostCoursesCategories").permitAll()
						.requestMatchers("/mostInscribedCategories").permitAll()
						.requestMatchers("/v3/api-docs.yaml", "/v3/api-docs*", "/swagger-ui/*", "/swagger-ui.html").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/course/enroll").hasAnyRole("USER")
						.requestMatchers("/user/{id}/image").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/user/{id}/edit").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/enrollment/rate").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/course/newReview").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/reviews/{id}/mark-pending").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/reviews/{id}/desmark-pending").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/editReview").hasAnyRole("ADMIN")
						.requestMatchers("/deleteReview").hasAnyRole("ADMIN")
						.requestMatchers("/profileImage/*").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/newCourse").hasAnyRole("USER")
						.requestMatchers("/notes/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/createCourse").hasAnyRole("USER")
						.requestMatchers("/editCourse/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/updateCourse").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/updateCourse/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/getTaughtCourses").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/getEnrollCourses").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/deleteCourse/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/deleteAccount/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/profile/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/profileImage/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/updateUser/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/admin/**").hasAnyRole("ADMIN")

						.requestMatchers("/statistics/*").hasAnyRole("USER")
						.requestMatchers("/puntuationChart/*").hasAnyRole("USER"))
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureHandler((request, response, exception) -> {
							response.sendRedirect("/error");
						})
						.defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());

		return http.build();
	}
}