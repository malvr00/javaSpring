package board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

// multipartResolver를 등록 했기 때문에 첨부파일과 관련된 자동 구성을 사용하지 않도록 변경.
// SpringBootApllication은 SpringBootConfiguration, ComponentScan, EnableAutoConfiguration 세개의
// 어노테이션으로 구성되어 있는데 EnableAutoConfiguration은 스프링 부트의 자동구성을 사용할 때 
// exclude를 이용해서 특정한 자동구성을 사용하지 않도록 할 수 있다.
@SpringBootApplication(exclude= {MultipartAutoConfiguration.class})
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
