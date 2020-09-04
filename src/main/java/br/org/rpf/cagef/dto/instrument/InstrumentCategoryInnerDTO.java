package br.org.rpf.cagef.dto.instrument;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
public class InstrumentCategoryInnerDTO implements Serializable {
	private static final long serialVersionUID = -5635268674032590299L;

	@NotNull
	@Min(1)
	private Long id;
}
