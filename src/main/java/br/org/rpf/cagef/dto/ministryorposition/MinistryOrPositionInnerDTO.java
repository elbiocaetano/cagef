package br.org.rpf.cagef.dto.ministryorposition;

import java.io.Serializable;
import java.util.List;

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
public class MinistryOrPositionInnerDTO implements Serializable {

	private static final long serialVersionUID = -8238567569655001057L;

	private List<Long> ids;
}
