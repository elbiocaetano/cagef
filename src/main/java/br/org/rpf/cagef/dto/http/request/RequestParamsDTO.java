package br.org.rpf.cagef.dto.http.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class RequestParamsDTO {
	
	private Long id;
	private Integer offset;
	private Integer limit;
	private String orderBy;
	private String direction;
	
	public PageRequest getPageRequest() {
		return PageRequest.of(offset, limit, Direction.fromString(direction), orderBy);
	}
}
