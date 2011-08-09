package edu.isi.bmkeg.neuart.atlasserver.datamaps.model;

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

import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;

@Entity @Table(name = "data_map_plate")
public class DataMapPlate {
	
	Integer id;
	String coronalLayerImageURI;
	AtlasPlate atlasPlate;
	DataMap parent;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Basic @Column(name  = "coronal_layer_image_uri", nullable = false)
	public String getCoronalLayerImageURI() {
		return coronalLayerImageURI;
	}
	
	public void setCoronalLayerImageURI(String uri) {
		coronalLayerImageURI = uri;
	}

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = DataMap.class )
	@JoinColumn(name="data_map_fk", nullable = false)
	public DataMap getParent() {
		return parent;
	}
	
	public void setParent(DataMap p) {
		parent = p;
	}

	@ManyToOne
	@JoinColumn(name="atlas_plate_fk", nullable = false)
	public AtlasPlate getAtlasPlate() {
		return atlasPlate;
	}
	
	public void setAtlasPlate(AtlasPlate p) {
		atlasPlate = p;
	}

}
