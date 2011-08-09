package edu.isi.bmkeg.neuart.atlasserver.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity @Table(name = "atlas_structure")
@NamedQueries( {
@NamedQuery(name="atlasstructure.findbyuri", query="select a from AtlasStructure a where a.atlasURI = :uri"),
@NamedQuery(name="atlasstructure.retrieveDescriptions", 
		query="select new edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription(a.atlasName, a.atlasDescription, a.atlasYear, a.atlasURI) from AtlasStructure a")
})
public class AtlasStructure {
	
	Integer atlasID;
	String atlasURI;
	String atlasName;
	String atlasDescription;
	int atlasYear;
	String sagitalImageURI;
	double sagitalLength;
	double distanceFromLeftToBregma;
	Set<AtlasPlate> atlasPlates;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "atlas_id", nullable = false)
	public Integer getAtlasID() {
		return atlasID;
	}
	
	public void setAtlasID(Integer id) {
		atlasID = id;
	}
	
	@Basic @Column(name  = "atlas_description", nullable = true)
	public String getAtlasDescription() {
		return atlasDescription;
	}

	public void setAtlasDescription(String value) {
		atlasDescription = value;
	}
	
	@Basic @Column(name  = "atlas_name", nullable = false)
	public String getAtlasName() {
		return atlasName;
	}
	
	public void setAtlasName(String value) {
		atlasName = value;
	}

	@Basic @Column(name  = "atlas_uri", nullable = false, unique = true)
	public String getAtlasURI() {
		return atlasURI;
	}
	
	public void setAtlasURI(String value) {
		atlasURI = value;
	}

	@Basic @Column(name  = "atlas_year", nullable = true)
	public int getAtlasYear() {
		return atlasYear;
	}
	
	public void setAtlasYear(int value) {
		atlasYear = value;
	}
	
	@Basic @Column(name  = "sagital_image_uri", nullable = false)
	public String getSagitalImageURI() {
		return sagitalImageURI;
	}

	public void setSagitalImageURI(String value) {
		sagitalImageURI = value;
	}
	 
	@Basic @Column(name  = "sagital_zlength", nullable = false)
	public double getSagitalZLength() {
		return sagitalLength;
	}
	
	public void setSagitalZLength(double value) {
		sagitalLength = value;
	}

	@Basic @Column(name  = "distance_to_bregma", nullable = false)
	public double getDistanceFromLeftToBregma() {
		return distanceFromLeftToBregma;
	}
	
	public void setDistanceFromLeftToBregma(double value) {
		distanceFromLeftToBregma = value;
	}

	@OneToMany(mappedBy = "parent", targetEntity = AtlasPlate.class,
			cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<AtlasPlate> getAtlasPlates() {		
		if (atlasPlates == null) 
			atlasPlates = new HashSet<AtlasPlate>();
		return atlasPlates;
	}
	
	public void setAtlasPlates(Set<AtlasPlate> plates) {
		atlasPlates = plates;
	}

	public void addPlate(AtlasPlate plate) {
		plate.setParent(this);
		getAtlasPlates().add(plate);
	}

}
