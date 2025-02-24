package ru.example.JWT_Auth.config.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * краткое описание.
 *
 * <p>
 * Подробное описание
 * </p>
 *
 *
 * @author aim_41tt
 * @version 1.0
 * @since 14.02.2025
 */
@Configuration
@EnableAsync
public class AdaptiveThreadPoolConfig {

	private static final Logger logger = LoggerFactory.getLogger(AdaptiveThreadPoolConfig.class);

	/**
	 * Настройка асинхронного пула потоков. Используется для управления задачами в
	 * сервисе управления пользователями.
	 */
	@Bean(name = "defaultTaskExecutor")
	@Primary // Этот бин будет использоваться по умолчанию для @Async
	protected Executor taskExecutor() {
		// Получение доступного количества процессоров
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		// Получение максимальной памяти, доступной JVM, в мегабайтах
		long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);

		// Вычисление ёмкости очереди на основе максимальной памяти
		int calculatedQueueCapacity = (int) (maxMemoryMB / 1.7);
		int queueCapacity = Math.min(calculatedQueueCapacity, 4000); // Ограничение: максимум 4000 задач

		logger.info("Настройка пула потоков: доступно процессоров={}, макс. память={}MB, ёмкость очереди={}",
				availableProcessors, maxMemoryMB, queueCapacity);

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		// Основное количество потоков - равно количеству доступных процессоров
		executor.setCorePoolSize(availableProcessors);
		// Максимальное количество потоков - в 2 раза больше базового, можно
		// регулировать в зависимости от характера задач
		executor.setMaxPoolSize(availableProcessors * 2);
		// Размер очереди задач
		executor.setQueueCapacity(queueCapacity);
		// Префикс имен потоков для удобства отладки
		executor.setThreadNamePrefix("AdaptivePool-");

		// При остановке приложения ждём завершения всех задач (graceful shutdown)
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(120);

		// Стратегия обработки задач при переполнении очереди:
		// Вместо простого логирования, можно использовать CallerRunsPolicy, чтобы
		// выполнить задачу в вызывающем потоке.
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		executor.initialize();
		return executor;
	}
}
