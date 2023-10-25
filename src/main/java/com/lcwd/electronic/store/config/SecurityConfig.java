package com.lcwd.electronic.store.config;


import com.lcwd.electronic.store.services.implementations.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    private final String[] PUBLIC_URL={
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**"
    };


    @Autowired
    private CustomUserDetailService customUserDetailService;


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


//    @Bean
//    public UserDetailsService userDetailsService(){
//         UserDetails normal= User.builder()
//                 .username("Himanshu")
//                 .password(passwordEncoder().encode("Himanshu"))
//                 .roles("NORMAL")
//                 .build();
//
//        UserDetails admin= User.builder()
//                .username("Harsh")
//                .password(passwordEncoder().encode("Harsh"))
//                .roles("ADMIN")
//                .build();
//        //user create
//        //InMemoryUserDetailsManager is implementation class of UserDetailsService
//        return new InMemoryUserDetailsManager(normal,admin);
//
//    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
