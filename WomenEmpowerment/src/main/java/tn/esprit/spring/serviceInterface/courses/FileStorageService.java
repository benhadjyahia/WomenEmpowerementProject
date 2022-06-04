package tn.esprit.spring.serviceInterface.courses;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.FileInfo;
@Service
public interface FileStorageService {
	public FileInfo store(MultipartFile file,Long courseId)throws IOException;
	public FileInfo getFile(String id);
	public Stream<FileInfo> getAllFiles();
	public FileInfo removeFile(String id);
	 
}
