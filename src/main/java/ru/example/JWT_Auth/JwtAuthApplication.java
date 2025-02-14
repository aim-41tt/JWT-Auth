package ru.example.JWT_Auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс JwtAuthApplication — точка входа в приложение Spring Boot.
 *
 * <p>
 * Отвечает за запуск и инициализацию контекста Spring.
 * Аннотация @SpringBootApplication включает в себя: - @Configuration —
 * позволяет использовать класс как источник конфигурации.
 * - @EnableAutoConfiguration — автоматически настраивает Spring на основе
 * зависимостей. - @ComponentScan — включает сканирование компонентов (Bean'ов)
 * в текущем пакете и подпакетах.
 * </p>
 *
 *
 * @author aim_41tt
 * @version 1.0
 * @since 10.02.2025
 */
@SpringBootApplication
public class JwtAuthApplication {

	/**
	 * Метод main() — точка входа в приложение. Spring Boot автоматически
	 * инициализирует контекст и запускает встроенный сервер.
	 *
	 * @param args аргументы командной строки (необязательные).
	 */
	public static void main(String[] args) {
		SpringApplication.run(JwtAuthApplication.class, args);
	}
}
