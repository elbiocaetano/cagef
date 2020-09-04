package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author elbio.caetano
 */
@Entity
@Table(name = "musician")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Musician extends Volunteer implements Serializable {
	private static final long serialVersionUID = 9200081341764819062L;

	@JoinColumn(name = "instrument_id", nullable = true)
	@ManyToOne(cascade = CascadeType.MERGE)
	private Instrument instrument;
	@Column(name = "oficialization_date")
	private LocalDate oficializationDate;
	@Column(name = "rehearsal_date")
	private LocalDate rehearsalDate;
	@Column(name = "rjm_exam_date")
	private LocalDate rjmExamDate;
	@Column(name = "oficial_cult_exam_date")
	private LocalDate oficialCultExamDate;
	@Column(name = "observation", length = 65535, columnDefinition = "Text")
	private String observation;
}
