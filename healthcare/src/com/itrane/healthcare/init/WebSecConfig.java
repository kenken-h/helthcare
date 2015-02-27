package com.itrane.healthcare.init;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import com.itrane.healthcare.service.LoginService;

/**
 * Spring セキュリティ設定クラス.
 */
@EnableWebMvcSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginService loginService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // この認証プロバイダは SHA-256/ソルト無し ハッシュパスワードの保存に DBを使用する
        auth.userDetailsService( loginService )
         .passwordEncoder( new ShaPasswordEncoder(256) );
		
        // 最も単純なプロバイダ
        // ユーザ、パスワード、ロールはすべてテキストとして追加される
		//auth.inMemoryAuthentication()
		//	.withUser("admin").password("admin").roles("ADMIN").and()
		//	.withUser("user1").password("user1").roles("USER").and()
		//	.withUser("user2").password("user2").roles("USER");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        // ユーザーが保護ページにアクセスしたときに表示するページを指定
          	.exceptionHandling()
              	.accessDeniedPage( "/error" )
              	.and()

        // どのロールがどのページにアクセス可能かを指定する
			.authorizeRequests()
				.antMatchers(
						"/js/**",
						"/css/**",
						"/img/**",
						"/login**",
						"/logout**")
					.permitAll()
				.antMatchers("/admin/**")
					.hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
 
        // ログインフォームの設定：
        // login-page: ログインビュー
		// login-processing-url: ログインフォームがサブミットされる URL
        // default-success-url: ログイン成功時にリダイレクトされる URL
    		.formLogin()
				.loginPage("/login")
				//.loginProcessingUrl( "/login" )
                .defaultSuccessUrl( "/" )
                .failureUrl( "/login?error" )
				.and()
			.logout()
				.permitAll()
				//.logoutRequestMatcher(logoutRequestMatcher())
				//.invalidateHttpSession(true)
				.and()
			.csrf()
			    .disable();
	}

}