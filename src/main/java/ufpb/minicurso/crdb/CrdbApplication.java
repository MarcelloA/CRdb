package ufpb.minicurso.crdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ufpb.minicurso.crdb.seguranca.TokenFilter;

@EnableJpaAuditing
@SpringBootApplication
@EnableSwagger2
public class CrdbApplication {

	@Bean
	public FilterRegistrationBean<TokenFilter> filterJwt() {
		FilterRegistrationBean<TokenFilter> filterRB = new FilterRegistrationBean<>();
		filterRB.setFilter(new TokenFilter());
		filterRB.addUrlPatterns("/api/disciplina/avaliacao/**");
		return filterRB;
	}

	public static void main(String[] args) {
		SpringApplication.run(CrdbApplication.class, args);
	}

}
