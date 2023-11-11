package efub.back.jupjup.domain.image.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.image.S3Upload;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.ImageUploadResponseDto;
import efub.back.jupjup.domain.post.exception.EmptyInputFilenameException;
import efub.back.jupjup.domain.post.exception.WrongImageFormatException;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.report.domain.Report;
import efub.back.jupjup.domain.report.domain.ReportImage;
import efub.back.jupjup.domain.report.repository.ReportImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageService {
	private final S3Upload s3Upload;
	private final PostImageRepository postImageRepository;
	private final ReportImageRepository reportImageRepository;

	public List<ImageUploadResponseDto> getPresignedUrls(List<String> fileNameList) {
		return fileNameList.stream().map(raw -> {
			isValidFileExtension(raw);
			String fileName = createRandomFileName(raw);
			String presignedUrl = s3Upload.getPresignedUrl(fileName);

			return ImageUploadResponseDto.builder()
				.rawFile(raw)
				.fileName(fileName)
				.presignedUrl(presignedUrl)
				.build();
		}).collect(Collectors.toList());
	}

	public List<String> saveImageUrlsPost(List<String> images, Post post) {
		List<String> imageUrls = new ArrayList<>();
		for (String fileUrl : images) {
			PostImage postImage = postImageRepository.save(new PostImage(fileUrl, post));
			imageUrls.add(postImage.getFileUrl());
		}

		return imageUrls;
	}

	public List<String> saveImageUrlsReport(List<String> images, Report report) {
		List<String> imageUrls = new ArrayList<>();
		for (String fileUrl : images) {
			ReportImage reportImage = reportImageRepository.save(new ReportImage(fileUrl, report));
			imageUrls.add(reportImage.getFileUrl());
		}

		return imageUrls;
	}

	public String createRandomFileName(String rawFile) {
		return UUID.randomUUID() + rawFile;
	}

	private void isValidFileExtension(String fileName) {
		if (fileName.length() == 0) {
			throw new EmptyInputFilenameException();
		}

		String[] validExtensions = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!Arrays.asList(validExtensions).contains(idxFileName)) {
			throw new WrongImageFormatException();
		}
	}
}
