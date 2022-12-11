package cdw.hk2.shop.laptop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import cdw.hk2.shop.laptop.exception.FileStorageException;
import cdw.hk2.shop.laptop.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//@Component
@Service
public class UrlImageUtils {

	private final Path fileStorageLocation;

	@Autowired
	public UrlImageUtils(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
				.toAbsolutePath().normalize();
		System.out.println("alo"+
				fileStorageLocation);

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public Boolean saveImage(MultipartFile[] multipartFiles, boolean key, String extenpath, long id)
			throws IOException {

		for (MultipartFile multipartFile : multipartFiles) {
			if (key) {
				String uploadDir = "C:\\Users\\MAC HM\\Desktop\\New folder (9)\\Website laptop\\WebsiteLaptop\\src\\main\\resources\\static\\product\\laptop\\"
						+ extenpath + "\\";

				String fileName = multipartFile.getOriginalFilename().replace(' ', '_');
				save(uploadDir, multipartFile, fileName);
			} else {
				String uploadDir = "C:\\Users\\MAC HM\\Desktop\\New folder (7)\\TLCN\\src\\main\\resources\\static\\images\\banner\\";
				String fileName = multipartFile.getOriginalFilename().replace(' ', '_');
				save(uploadDir, multipartFile, fileName);
			}

		}
		return key;
	}

	public void save(String uploadDir, MultipartFile multipartFile, String fileName) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}
	
}
