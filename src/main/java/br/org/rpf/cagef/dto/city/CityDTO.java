package br.org.rpf.cagef.dto.city;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class CityDTO implements Serializable {
	private static final long serialVersionUID = 5492705028119380094L;

	@Min(1)
	private Long id;

	@NotNull
	@Size(min = 3, max = 255)
	private String name;

	@NotNull
	@Size(min = 3, max = 255)
	private String state;

	@NotNull
	private Boolean regional;
}
