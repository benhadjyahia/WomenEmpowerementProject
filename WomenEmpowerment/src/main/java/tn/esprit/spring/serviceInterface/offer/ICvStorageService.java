package tn.esprit.spring.serviceInterface.offer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.CvInfo;
public interface ICvStorageService {
	public CvInfo store(MultipartFile file,Long courseId)throws IOException;
	public CvInfo getFile(String id);
	public Stream<CvInfo> getAllFiles();
	public CvInfo removeFile(String id);
}
