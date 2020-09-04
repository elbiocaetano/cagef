package br.org.rpf.cagef.dto.ministryorposition;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MinistryOrPositionDTO implements Serializable{

	private static final long serialVersionUID = 8872585354539446287L;

	private Long id;
	
	private String description;
}
