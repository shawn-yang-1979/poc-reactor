package idv.shawnyang.pocreactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class PocReactorApplication {

	private static final Logger logger = LoggerFactory.getLogger(PocReactorApplication.class);

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PocReactorApplication.class, args);
		Flux.just("red", "white", "blue")//
				.log()//
				.map(AppFunctions::capitalize)//
				.doOnNext(AppFunctions::log)//
				.doOnComplete(AppFunctions::log)//
				.subscribe();

		Flux.just("red", "white", "blue")//
				.log()//
				.map(AppFunctions::capitalize)//
				.doOnNext(AppFunctions::log)//
				.doOnComplete(AppFunctions::log)//
				.subscribeOn(Schedulers.parallel())//
				.subscribe();

		Flux.just("red", "white", "blue")//
				.log().flatMap(value -> Mono.just(value.toUpperCase()).subscribeOn(Schedulers.parallel()))//
				.subscribe(value -> logger.info("Consumed: " + value));

		Flux.just("red", "white", "blue")//
				.log()//
				.map(String::toUpperCase)//
				.subscribeOn(Schedulers.newParallel("sub"))//
				.publishOn(Schedulers.newParallel("pub"))//
				.subscribe(value -> logger.info("Consumed: " + value));

		Thread.sleep(5000);
	}

	public static class AppFunctions {
		private AppFunctions() {

		}

		public static String capitalize(String s) {
			return StringUtils.capitalize(s);
		}

		public static void log(String s) {
			logger.info(s);
		}

		public static void log() {
			logger.info("complete");
		}
	}

}
