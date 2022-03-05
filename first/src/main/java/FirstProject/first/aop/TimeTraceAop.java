

@Aspect //AOP는 Aspect Annotation 사용
// 컨트롤러에 인식시키기 위해 @Component를 써도 되지만 SpringConfig에 @Bean 등록
public class TimeTraceAop{

    @Around("execution(* FirstProject..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString()); //어떤 메서드가 호출된지 알려줌
        try{
            Object result = joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString()+" "+ timeMs + "ms"); //어떤 메서드가 호출된지 알려줌
        }
    }
}
