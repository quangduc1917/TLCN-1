package cdw.hk2.shop.laptop;

import cdw.hk2.shop.laptop.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cdw.hk2.shop.laptop.model.Product;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class WebsiteLaptopApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebsiteLaptopApplication.class, args);
		
	}

}
