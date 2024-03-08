package efub.back.jupjup.global.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
public class AsyncThreadConfiguration {
	@Bean(name = "asyncThreadTaskExecutor")
	public Executor asyncThreadTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(8);
		threadPoolTaskExecutor.setThreadNamePrefix("Executor-");
		threadPoolTaskExecutor.initialize();
		return new HandlingExecutor(threadPoolTaskExecutor);
	}

	// executor를 감싸서 예외 처리
	public class HandlingExecutor implements AsyncTaskExecutor {
		private AsyncTaskExecutor executor;

		public HandlingExecutor(AsyncTaskExecutor executor) {
			this.executor = executor;
		}

		@Override
		public void execute(Runnable task) {
			executor.execute(task);
		}

		@Override
		public void execute(Runnable task, long startTimeout) {
			executor.execute(createWrappedRunnable(task), startTimeout);
		}

		@Override
		public Future<?> submit(Runnable task) {
			return executor.submit(createWrappedRunnable(task));
		}

		@Override
		public <T> Future<T> submit(final Callable<T> task) {
			return executor.submit(createCallable(task));
		}

		private <T> Callable<T> createCallable(final Callable<T> task) {
			return new Callable<T>() {
				@Override
				public T call() throws Exception {
					try {
						return task.call();
					} catch (Exception ex) {
						handle(ex);
						throw ex;
					}
				}
			};
		}

		private Runnable createWrappedRunnable(final Runnable task) {
			return new Runnable() {
				@Override
				public void run() {
					try {
						task.run();
					} catch (Exception ex) {
						handle(ex);
					}
				}
			};
		}

		private void handle(Exception ex) {
			log.info("Failed to execute task. : {}", ex.getMessage());
			log.error("Failed to execute task. ", ex);
		}

	}

}
