package tn.esprit.spring.service.courses;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.FileInfo;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.EventRepo;
import tn.esprit.spring.repository.FileDBRepository;
import tn.esprit.spring.serviceInterface.courses.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService{
	@Autowired
	  private FileDBRepository fileDBRepository;
	@Autowired
	CourseRepository courseRepository;

	
	@Override
	  public FileInfo store(MultipartFile file,Long courseId) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    FileInfo FileInfo = new FileInfo(fileName, file.getContentType(), file.getBytes());
	    Course course = courseRepository.findById(courseId).get();
	    course.getFiles().add(FileInfo);
	    courseRepository.flush();
	    return fileDBRepository.save(FileInfo);
	  }
	
	@Override
	  public FileInfo getFile(String id) {
	    return fileDBRepository.findById(id).get();
	  }
	@Override
	  public Stream<FileInfo> getAllFiles() {
	    return fileDBRepository.findAll().stream();
	  }
	@Override
	  public FileInfo removeFile(String id) {
		   fileDBRepository.deleteById(id);
		   return fileDBRepository.findById(id).get();
	  }


}
