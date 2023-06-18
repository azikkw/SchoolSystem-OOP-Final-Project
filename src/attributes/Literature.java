package attributes;

import java.io.Serializable;
import java.time.LocalDate;

public class Literature implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3429937779808872570L;

	/**
     */
	private String name;

    /**
     */
    private String author;
    
    private LocalDate deadline; 

    /**
     */
    private int pages;

    /**
     */
    public Literature() {
    	
    }
   
	public Literature(String name, String author, int pages) {
		this.name = name;
		this.author = author;
		this.pages = pages;
		this.setDeadline(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "Name=" + name + ", author=" + author + ", pages=" + pages + "\n";
	}

}