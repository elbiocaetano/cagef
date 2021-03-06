package br.org.rpf.cagef.dto.city;

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
public class CityInnerDTO implements Serializable {
	private static final long serialVersionUID = 1078912772390906332L;

	@NotNull
	@Min(1)
	private Long id;
}
