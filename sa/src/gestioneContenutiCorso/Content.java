package gestioneContenutiCorso;

import java.util.*;

public class Content {
	public List<Section> sections;
	public Content() {
		sections=new ArrayList<Section>();
	}
	public Section addSection(String title, String descr, boolean visibility, Integer sectionCode, 
			int studentNumber, int courseCode, Integer sonOf) {
		Section sez=new Section(title, descr, visibility, sectionCode, studentNumber, courseCode, sonOf);
		sections.add(sez);
		return sez;
	}
}