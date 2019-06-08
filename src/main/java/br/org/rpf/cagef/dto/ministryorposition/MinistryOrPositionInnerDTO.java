package br.org.rpf.cagef.dto.ministryorposition;

import java.io.Serializable;
import java.util.List;

public class MinistryOrPositionInnerDTO implements Serializable {

	private static final long serialVersionUID = -8238567569655001057L;

	private List<Long> ids;
	
	public MinistryOrPositionInnerDTO() {
		super();
	}
	
	public MinistryOrPositionInnerDTO(List<Long> ids) {
		super();
		this.ids = ids;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
