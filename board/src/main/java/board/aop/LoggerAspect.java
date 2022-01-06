package board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect		// 자바코드에서 AOP를 설정
public class LoggerAspect {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	// Around = 실행될 시점 지정 ( 어드바이스(Advice) 정의 ) 동작시점, 
	// execution = 포인트컷(pointcut) 표현. 어떤 조인포인트 ( 적용지점 )를 사용할 것인가.
	@Around("execution(* board..controller.*Controller.*(..)) or execution(* board..service.*Impl.*(..)) or execution(* board..mapper.*Mapper.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable{
		String type = "";
		// ProccedingJoinPoint 적용지점에 대한 정보를 가지고 있음
		// getSignature : 호출되는 메서드에 대한 정보를 구함
		String name = joinPoint.getSignature().getDeclaringTypeName();
		if(name.indexOf("Controller") > -1) {
			type = "Controller  \t:  ";
		}else if(name.indexOf("Service") > -1) {
			type = "ServiceImpl  \t:  ";
		}else if(name.indexOf("Mapper") > -1) {
			type = "Mapper  \t\t: ";
		}
		log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
