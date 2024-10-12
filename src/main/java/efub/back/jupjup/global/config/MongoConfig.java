package efub.back.jupjup.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import efub.back.jupjup.global.util.DateToLocalDateTimeKSTConverter;
import efub.back.jupjup.global.util.LocalDateTimeToDateKSTConverter;

@Configuration
public class MongoConfig {

	// MongoDB 저장시에 UTC로 저장되는 것을 KST로 자동 변환
	@Bean
	public MongoCustomConversions customConversions(
		LocalDateTimeToDateKSTConverter localDateTimeToDateKstConverter,
		DateToLocalDateTimeKSTConverter dateToLocalDateTimeKstConverter) {

		return new MongoCustomConversions(
			Arrays.asList(
				localDateTimeToDateKstConverter,
				dateToLocalDateTimeKstConverter
			)
		);
	}
}