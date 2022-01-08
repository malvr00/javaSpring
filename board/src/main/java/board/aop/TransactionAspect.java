package board.aop;

import java.util.Collections;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;	// 더 이상 사용하지 않음
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {
	/* 
	 * 직접만든 트랜잭션
	 * 모든 예외발동 시에 트랜잭션이 실행 된다.
	 * */
	
	// 트랜잭션 설정 값
	private static final String AOP_TRANSACTION_METHOD_NAME = "*";
	private static final String AOP_TRNASACTION_EXPRESSION = "execution(* board..service.*Impl.*(..))";
	
	@Autowired
	private TransactionManager transactionManager;
	
	@Bean
	public TransactionInterceptor transactionAdvice() {
		// 설정한 트랜잭션 속성 적용
		MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
		// 트랜잭션 속성 적용 변수
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		
		// 트랜잭션 이름 설정
		transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);
		// 트랜잭션에서 롤백을 하는 룰을 설정 여기서는 예외가 일어나면 롤백이 수행되도록 지정 Exception.class < 롤백의 룰로 등록
		transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		// 설정한 속성 적용
		source.setTransactionAttribute(transactionAttribute);
		
		return new TransactionInterceptor(transactionManager, source);
	}
	
	@Bean
	public Advisor transactionAdviceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		
		// AOP 포인트컷 설정. 여기서는 비즈니스 로직이 수행되는 모든 ServiceImpl 클래스의 모든 메서드를 지정
		pointcut.setExpression(AOP_TRNASACTION_EXPRESSION);
		// AOP 포인트컷 설정에 트랜잭션 설정한 속성 담아서 리턴
		// DefaultPointcutAdvisor = 어드바이스 & 포인트컷 결합시키는클래스
		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}
}
