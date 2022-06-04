package tn.esprit.spring.serviceInterface.courses;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import tn.esprit.spring.exceptions.CourseOwnerShip;
@Service
public interface CourseLiveService {
	public String createchannel(long courseId,long userId) throws IOException, InterruptedException;
	public void startChannel(long courseId,long userId) throws IOException, InterruptedException;
	public void stopChannel(long courseId,long userId) throws IOException,InterruptedException;
	public void deleteChannel(long courseId,long userId) throws IOException,InterruptedException, CourseOwnerShip;
	public HttpResponse<String> getChannelStatus(long courseId) throws IOException,InterruptedException;
	
}
