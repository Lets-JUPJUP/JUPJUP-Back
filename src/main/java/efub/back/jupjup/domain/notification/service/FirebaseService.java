package efub.back.jupjup.domain.notification.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseService {
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

	/**
	 * topic으로 FCM 알림을 보낸다
	 * @param title
	 * @param body
	 * @throws IOException
	 * @throws FirebaseMessagingException
	 */
	//
	public void sendMessageByTopic(String title, String body) throws IOException, FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setTopic(topicName)
			.build());
	}

	/**
	 * 토큰으로 FCM 알림을 보낸다
	 * @param title
	 * @param body
	 * @param token
	 * @throws FirebaseMessagingException
	 */
	public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(token)
			.build());
	}

}
