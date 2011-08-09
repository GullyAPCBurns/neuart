package edu.isi.bmkeg.neuart.atlasserver.datamaps.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;

@Entity @Table(name = "data_map")
@NamedQueries( {
@NamedQuery(name="datamap.findbyuri", query="select d from DataMap d where d.uri = :uri"),
@NamedQuery(name="datamap.retrieveDescriptions", 
		query="select new edu.isi.bmkeg.neuart.atlasserver.datamaps.model." +
				"DataMapDescription(a.name, a.description, a.citation, a.digitalLibraryKey, a.atlas.atlasURI, a.uri) " +
				"from DataMap a where a.atlas.atlasURI = :atlas_uri")
})
public class DataMap {
	
	Integer id;
	String uri;
	String name;
	String description;
	String citation;
	String digitalLibraryKey;
	AtlasStructure atlas;
	Set<DataMapPlate> dataMapPlates;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Basic @Column(name  = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		description = value;
	}
	
	@Basic @Column(name  = "name", nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		name = value;
	}

	@Basic @Column(name  = "uri", nullable = false, unique = true)
	public String getUri() {
		return uri;
	}
	
	public void setUri(String value) {
		uri = value;
	}

	@Basic @Column(name  = "citation", nullable = true)
	public String getCitation() {
		return citation;
	}

	public void setCitation(String value) {
		citation = value;
	}
	
	@Basic @Column(name  = "digital_library_key", nullable = true)
	public String getDigitalLibraryKey() {
		return digitalLibraryKey;
	}

	public void setDigitalLibraryKey(String value) {
		digitalLibraryKey = value;
	}
	
	@OneToMany(mappedBy = "parent",
			cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<DataMapPlate> getDataMapPlates() {		
		if (dataMapPlates == null) 
			dataMapPlates = new HashSet<DataMapPlate>();
		return dataMapPlates;
	}
	
	public void setDataMapPlates(Set<DataMapPlate> plates) {
		dataMapPlates = plates;
	}

	public void addDataMapPlate(DataMapPlate plate) {
		plate.setParent(this);
		getDataMapPlates().add(plate);
	}

	@ManyToOne
	@JoinColumn(name="atlas_fk", nullable = false)
	public AtlasStructure getAtlas() {
		return atlas;
	}
	
	public void setAtlas(AtlasStructure a) {
		atlas = a;
	}
	
}
