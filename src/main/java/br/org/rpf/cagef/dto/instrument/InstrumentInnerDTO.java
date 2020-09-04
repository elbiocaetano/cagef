package br.org.rpf.cagef.dto.instrument;

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
public class InstrumentInnerDTO implements Serializable{
	private static final long serialVersionUID = 429050103363953895L;
	
	private Long id;
}
