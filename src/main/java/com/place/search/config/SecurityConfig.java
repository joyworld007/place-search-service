package com.place.search.config;

import com.place.search.service.MemberService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private MemberService memberService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/search/**").hasRole("MEMBER")
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf()
        .ignoringAntMatchers("/place/**")
        .and()
        .headers()
        .addHeaderWriter(
            new XFrameOptionsHeaderWriter(
                new WhiteListedAllowFromStrategy(Arrays.asList("localhost"))
            )
        ).frameOptions().sameOrigin()
        .and()
        .formLogin()
        .loginPage("/user/login")
        .defaultSuccessUrl("/user/login/result")
        .failureUrl("/user/login/fail")
        .permitAll()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/user/login")
        .invalidateHttpSession(true)
        .and()
        .exceptionHandling().accessDeniedPage("/user/denied");
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
  }
}
