package attributes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class Message implements Serializable, Comparable<Message>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2655382201966276037L;
	private String sender;
	private String message;
	private LocalDate date;
	
	public Message() {
		   
	}
	
	public Message(String sender, String message, LocalDate date) {
		this.sender = sender;
		this.message = message;
		this.date = date;
	}

	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return date.equals(other.date);
	}

	@Override
	public String toString() {
		return "Sender:  " + sender + "\n   Date=" + date + "\n   Message:  " + message + "\n";
	}

	public int compareTo(Message message) {
		return date.compareTo(message.date);
	}
}
