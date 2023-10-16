package efub.back.jupjup.domain.image;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3Upload {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;

	public String getPresignedUrl(String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPresignedUrlExpiration());

		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
		String presignedUrl = url.toString();

		return presignedUrl;
	}


	/**
	 * presigned url 유효 기간 설정
	 * @return 유효기간
	 */
	private Date getPresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}
}
