package tn.esprit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication(exclude = ElasticsearchDataAutoConfiguration.class)
@ComponentScan({"tn.esprit.*"})
@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
public class WomenEmpowermentApplication {
	


	public static void main(String[] args) {
		SpringApplication.run(WomenEmpowermentApplication.class, args);
	}
	

}
