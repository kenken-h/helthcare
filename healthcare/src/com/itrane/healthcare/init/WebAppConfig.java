package com.itrane.healthcare.init;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Spring フレームワークの設定. 
 * 注意：この Java 設定クラスを使用するには Spring Framework 3.1 以上が必要
 */
@Configuration
@EnableWebMvc
@Import({ DbConfig.class })
// データベース設定をインポート
@ComponentScan("com.itrane.healthcare")
@PropertySource("classpath:resources/app.properties")
public class WebAppConfig extends WebMvcConfigurerAdapter {

	@Resource
	private Environment env;
	
	@Autowired
	ServletContext servletContext;

	// 静的リソースの設定
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}

	// テンプレートリゾルバーの設定
	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");	//テンプレートモデルとして HTMLを選択
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	// Thymeleaf テンプレートエンジンの設定
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.setMessageResolver(messageResolver());
		return engine;
	}

	// Thymeleaf ビューリゾルバー設定
	@Bean
	public ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(2);
		viewResolver.setViewNames(new String[] { "*" });
		viewResolver.setCache(false);
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	// メッセージソースの設定
	// WEBページでプロパティファイルを使用できる
	// 日本語メッセージ：messages_ja.properties
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(env.getProperty("message.source.basename"));
		messageSource.setUseCodeAsDefaultMessage(true);	//メッセージのキーがない場合にキーを表示
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(0);				//-1: リロードしない、0: 常にリロード
		return messageSource;
	}

	@Bean
	public SpringMessageResolver messageResolver() {
		SpringMessageResolver resolver = new SpringMessageResolver();
		resolver.setMessageSource(messageSource());
		return resolver;
	}
	
	/**
	 * バイタルマスター作成するためのビーン.
	 * TODO: 実際のアプリでは不要
	 */
	@Bean(initMethod = "initData")
	public InitMaster initData() {
		return new InitMaster();
	}
	/**
	 * １月分のバイタルデータを作成するためのビーン.
	 * TODO: 実際のアプリでは不要
	 */
	@Bean(initMethod = "createData")
	public CreateDummyData createData() {
		return new CreateDummyData();
	}

}
