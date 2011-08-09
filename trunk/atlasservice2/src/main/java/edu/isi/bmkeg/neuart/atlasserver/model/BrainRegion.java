package edu.isi.bmkeg.neuart.atlasserver.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity @Table(name = "brain_regions", uniqueConstraints = {@UniqueConstraint(columnNames={"br_abbrev", "atlas_id"})})
@NamedQueries( {
@NamedQuery(name="brainregion.findbyabbrev", query="select br from BrainRegion br, AtlasStructure a where a.atlasURI = :atlasuri and br.abbreviation = :abbrev and a = br.atlas")
})
public class BrainRegion {
	
	Integer brID;
	AtlasStructure atlas;
	String abbrev;
	String description;
	int atlasMinlevel;
	int atlasMaxlevel;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "br_id", nullable = false)
	public Integer getID() {
		return brID;
	}
	
	public void setID(Integer id) {
		brID = id;
	}
	
	@Basic @Column(name  = "br_description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		description = value;
	}
	
	//TODO make unique the combination of atlas and abbrev
	@Basic @Column(name  = "br_abbrev", nullable = false)
	public String getAbbreviation() {
		return abbrev;
	}
	
	public void setAbbreviation(String value) {
		abbrev = value;
	}

	@ManyToOne( targetEntity = AtlasStructure.class )
	@JoinColumn(name="atlas_id", nullable = false)
	public AtlasStructure getAtlas() {
		return atlas;
	}
	
	public void setAtlas(AtlasStructure value) {
		atlas = value;
	}

	@Basic @Column(name  = "atlas_minlevel", nullable = false)
	public int getAtlasMinlevel() {
		return atlasMinlevel;
	}
	
	public void setAtlasMinlevel(int value) {
		atlasMinlevel = value;
	}
	
	@Basic @Column(name  = "atlas_maxlevel", nullable = false)
	public int getAtlasMaxlevel() {
		return atlasMaxlevel;
	}
	
	public void setAtlasMaxlevel(int value) {
		atlasMaxlevel = value;
	}
	

}
