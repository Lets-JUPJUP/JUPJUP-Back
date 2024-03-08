package efub.back.jupjup.domain.notification.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.dto.TestTokenDto;
import efub.back.jupjup.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
	private final RedisService redisService;

	@Value("${fcm.service-key}")
	private String serviceKeyFilePath;

	@Value("${fcm.topic-name}")
	private String topicName;

	@Value("${fcm.project-id}")
	private String projectId;

	@PostConstruct
	public void intialize() throws IOException {
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceKeyFilePath).getInputStream()))
			.setProjectId(projectId)
			.build();

		FirebaseApp.initializeApp(options);
	}

	public void saveFcmToken(Member member, TestTokenDto tokenDto) {
		redisService.setData("FcmToken:" + member.getId(), tokenDto.getToken());
	}

	public void sendMessageByTopic(String title, String body) throws IOException, FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setTopic(topicName)
			.build());
	}

	public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(token)
			.build());
	}

	@Async("asyncThreadTaskExecutor")
	public void sendPushMessage(Long memberId, String title, String body) throws FirebaseMessagingException {
		String fcmToken = redisService.getData("FcmToken:" + memberId);
		if (fcmToken == null) {
			return;
		}
		log.info("알림 전송 memberId : " + memberId);
		FirebaseMessaging.getInstance().sendAsync(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(fcmToken)
			.build());
	}

}
