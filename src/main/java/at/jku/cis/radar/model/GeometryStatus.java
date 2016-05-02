package at.jku.cis.radar.model;

public enum GeometryStatus {
    CREATED("created"), ERASED("erased");
	
	private String status;
	
	private GeometryStatus(String status) {
		this.status = status;
	}
	
	public String getValue(){
		return status;
	}
	
}
