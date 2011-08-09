package edu.isi.bmkeg.neuart.atlasserver.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name = "atlas_plate")
public class AtlasPlate {
	
	Integer plateID;
	String plateName;
	double sagitalZOffset;
	String coronalThumbnailURI;
	String coronalImageURI;
	AtlasStructure parent;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "plate_id", nullable = false)
	public Integer getPlateID() {
		return plateID;
	}
	
	public void setPlateID(Integer id) {
		plateID = id;
	}

	@Basic @Column(name  = "coronal_image_uri", nullable = false)
	public String getCoronalImageURI() {
		return coronalImageURI;
	}
	
	public void setCoronalImageURI(String uri) {
		coronalImageURI = uri;
	}

	@Basic @Column(name  = "plate_name", nullable = false)
	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String name) {
		plateName = name;
	}
	
	@Basic @Column(name = "sagital_zoffset", nullable = false)
	public double getSagitalZOffsetFromLeft() {
		return sagitalZOffset;
	}
	
	public void setSagitalZOffsetFromLeft(double offset) {
		sagitalZOffset = offset;
	}

	@Basic @Column(name = "coronal_thumbnail_uri", nullable = false)
	public String getCoronalThumbnailURI() {
		return coronalThumbnailURI;
	}
	
	public void setCoronalThumbnailURI(String value) {
		coronalThumbnailURI = value;
	}

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = AtlasStructure.class )
	@JoinColumn(name="atlas_id", nullable = false)
	public AtlasStructure getParent() {
		return parent;
	}
	
	public void setParent(AtlasStructure p) {
		parent = p;
	}

}
