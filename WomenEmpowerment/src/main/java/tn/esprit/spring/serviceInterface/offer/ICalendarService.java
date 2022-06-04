package tn.esprit.spring.serviceInterface.offer;

import java.io.IOException;

import com.nylas.RequestFailedException;

public interface ICalendarService {
	public String createCal() throws IOException, RequestFailedException ;
	public void postEventExample() throws IOException, RequestFailedException;
}
