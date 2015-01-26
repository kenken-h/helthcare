package com.itrane.healthcare.init;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.XmlViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.itrane.healthcare.task.TakeMedTask;

/**
 * Spring フレームワークの設定. 
 * 注意：この Java 設定クラスを使用するには Spring Framework 3.1 以上が必要
 */
@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@Import({ DbConfig.class })
// データベース設定をインポート
@ComponentScan("com.itrane.healthcare")
@PropertySource("classpath:resources/app.properties")
public class WebAppConfig extends WebMvcConfigurerAdapter
				implements SchedulingConfigurer {

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
	
	// Excel ビューリゾルバー
	// "spreadsheet-views.xml"で"accounts/list"と呼ばれるビーンにマップする
	@Bean
	public ViewResolver excelViewResolver() throws Exception {
		XmlViewResolver resolver = new XmlViewResolver();
		resolver.setOrder(1);
		//resolver.setLocation(new ServletContextResource(servletContext,"/WEB-INF/views.xml"));
		return resolver;
	}
	
	// PDF ビューリゾルバー
	@Bean
	public ViewResolver pdfViewResolver() throws Exception {
		ResourceBundleViewResolver resolver = new ResourceBundleViewResolver();
		resolver.setOrder(1);
		resolver.setBasename("views");
		return resolver;
	}

	// マルチパートリゾルバ
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(500000000);
		return multipartResolver;
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

	/**
	 * タスクスケジューラを登録して、固定間隔でスケジュールされたタスクを追加.
	 */
	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addFixedRateTask(
            new Runnable() {
                public void run() {
                    takeMedTask().noticeTakeMedicine();
                }
            }, 5000
        );
    }

	/**
	 * タスクスケジューラの生成.
	 */
    @Bean(destroyMethod="shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(20);
    }

    /**
     * 薬の服用を通知するタスクの生成.
     */
    @Bean
    public TakeMedTask takeMedTask() {
        return new TakeMedTask();
    }
}
