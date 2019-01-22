package gestioneContenutiCorso;

public class Resource {
	public String name, description, path;
	public int sectionCode;
	public int resourceCode;
	public Boolean visibility;
	public String type;
	public Resource(String name, String description, String path, int sectionCode, int resourceCode,
			boolean visibility, String type) {
		this.name=name;
		this.description=description;
		this.path=path;
		this.sectionCode=sectionCode;
		this.resourceCode=resourceCode;
		this.visibility=visibility;
		this.type=type;
	}
}