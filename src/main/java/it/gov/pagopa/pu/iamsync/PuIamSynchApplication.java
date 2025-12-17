package it.gov.pagopa.pu.iamsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class PuIamSynchApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuIamSynchApplication.class, args);
	}

}
