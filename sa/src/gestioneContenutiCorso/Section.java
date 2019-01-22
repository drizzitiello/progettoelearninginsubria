package gestioneContenutiCorso;

import java.util.ArrayList;
import java.util.List;

public class Section {

	public Integer sonOf;
	public String title;
	public String description;
	public Boolean visibility;
	public Integer courseCode;
	public List<Resource> resources=new ArrayList<Resource>();
	public int sectionCode;
	public int studentNumber;
	public Section(String title, String descr, boolean visibility, Integer sectionCode, 
			int studentNumber, int courseCode, Integer sonOf) {
		this.title=title;
		this.sectionCode=sectionCode;
		this.description=descr;
		this.visibility=visibility;
		this.courseCode=courseCode;
		this.studentNumber=studentNumber;
		this.sonOf=sonOf;
	}

	public void addResource(String name, String descr, String path, int sectionCode, int codRisorsa,
			boolean visibilita, String tipo) {
		Resource ris=new Resource(name, descr, path, sectionCode, codRisorsa, visibilita, tipo);
		resources.add(ris);
	}

}
