package board.common;
 
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice	//해당 클래스가 예외처리 클래스임을 알려
public class ExceptionHandler {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)	//Exception.class로 모든 예외를 처리함 ( 실제에서는 각가의 예외처리가 필요 )
	public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {
		ModelAndView mv = new ModelAndView("/error/error_default");	// 예외 발생 시 보여줄 화면 지정
		mv.addObject("exception" ,exception);
		
		log.error("exception", exception);	// 에러로그 출력
		
		return mv;
	}
}
