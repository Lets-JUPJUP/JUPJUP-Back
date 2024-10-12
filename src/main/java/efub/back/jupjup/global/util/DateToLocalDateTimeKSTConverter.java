package efub.back.jupjup.global.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class DateToLocalDateTimeKSTConverter implements Converter<LocalDateTime, Date> {
	@Override
	public Date convert(LocalDateTime source) {
		// LocalDateTime에 9시간을 더해 한국 표준시로 변환 후, Timestamp로 변환
		return Timestamp.valueOf(source.plusHours(9));
	}
}
