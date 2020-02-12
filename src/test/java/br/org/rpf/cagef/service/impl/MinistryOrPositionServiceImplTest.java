package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionDTO;
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionInnerDTO;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.service.MinistryOrPositionService;
import br.org.rpf.cagef.util.MinistryOrPositionSpecification;

@RunWith(SpringRunner.class)
public class MinistryOrPositionServiceImplTest {

	@TestConfiguration
	static class MinistryOrPositionServiceImplTestConfiguration {
		@Bean(name = "ministryOrPositionService")
		public MinistryOrPositionService ministryOrPositionService() {
			return new MinistryOrPositionServiceImpl();
		}
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private MinistryOrPositionServiceImpl ministryOrPositionService;

	@MockBean
	private MinistryOrPositionRepository ministryOrPositionRepository;

	@Test
	public void findAllErrorTest() {
		Mockito.when(ministryOrPositionRepository.findAll(any(MinistryOrPositionSpecification.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.ministryOrPositionService.findAll(null, null, null, 0, 24, "id", "ASC");
	}

	@Test
	public void findAllSuccessTest() {
		Mockito.when(ministryOrPositionRepository.findAll(any(MinistryOrPositionSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<MinistryOrPosition>(getMinisteriesOrPositionsList()));
		Page<MinistryOrPosition> ministeriesOrPositions = this.ministryOrPositionService.findAll(null, null, null, 0, 24,
				"id", "ASC");
		List<MinistryOrPosition> ministeriesOrPositionsList = ministeriesOrPositions.getContent();
		assertEquals(4, ministeriesOrPositionsList.size());
		assertEquals(1l, ministeriesOrPositionsList.get(0).getId().longValue());
		assertEquals("Ancião", ministeriesOrPositionsList.get(0).getDescription());

		assertEquals(2l, ministeriesOrPositionsList.get(1).getId().longValue());
		assertEquals("Cooperador do Ofício Ministerial", ministeriesOrPositionsList.get(1).getDescription());

		assertEquals(3l, ministeriesOrPositionsList.get(2).getId().longValue());
		assertEquals("Diácono", ministeriesOrPositionsList.get(2).getDescription());

		assertEquals(4l, ministeriesOrPositionsList.get(3).getId().longValue());
		assertEquals("Encarregado Regional", ministeriesOrPositionsList.get(3).getDescription());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		Mockito.when(ministryOrPositionRepository.findById(1l)).thenReturn(Optional.ofNullable(null));

		this.ministryOrPositionService.byId(1l);
	}

	@Test()
	public void findByIdSuccessTest() {
		Mockito.when(ministryOrPositionRepository.findById(1l)).thenReturn(Optional.of(generateMinistryOrPosition()));

		MinistryOrPosition ministryOrPosition = this.ministryOrPositionService.byId(1l);
		assertEquals(1l, ministryOrPosition.getId().longValue());
		assertEquals("Teste", ministryOrPosition.getDescription());
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		Mockito.when(ministryOrPositionRepository.save(any())).thenThrow(new Exception("Error"));

		this.ministryOrPositionService.save(generateMinistryOrPositionDTO());
	}

	@Test()
	public void saveSuccessTest() {
		Mockito.when(ministryOrPositionRepository.save(any())).thenReturn(generateMinistryOrPosition());

		MinistryOrPosition ministryOrPosition = this.ministryOrPositionService.save(generateMinistryOrPositionDTO());
		assertEquals(1l, ministryOrPosition.getId().longValue());
		assertEquals("Teste", ministryOrPosition.getDescription());
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		Mockito.when(ministryOrPositionRepository.save(any())).thenThrow(new Exception("Error"));

		this.ministryOrPositionService.save(generateMinistryOrPositionDTO());
	}

	@Test()
	public void updateSuccessTest() {
		Mockito.when(ministryOrPositionRepository.save(any())).thenReturn(generateMinistryOrPosition());

		MinistryOrPosition ministryOrPosition = this.ministryOrPositionService.update(1l,
				generateMinistryOrPositionDTO());
		assertEquals(1l, ministryOrPosition.getId().longValue());
		assertEquals("Teste", ministryOrPosition.getDescription());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(ministryOrPositionRepository).deleteById(1l);

		this.ministryOrPositionService.save(generateMinistryOrPositionDTO());
	}

	@Test()
	public void removeSuccessTest() {
		Mockito.doNothing().when(ministryOrPositionRepository).deleteById(1l);

		this.ministryOrPositionService.remove(1l);
	}

	public static MinistryOrPosition generateMinistryOrPosition() {
		return new MinistryOrPosition(1l, "Teste");
	}

	public static MinistryOrPositionDTO generateMinistryOrPositionDTO() {
		return new MinistryOrPositionDTO(1l, "Teste");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static MinistryOrPositionInnerDTO generateMinistryOrPositionInnerDTO() {
		return new MinistryOrPositionInnerDTO(new ArrayList(Arrays.asList(1l)));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static MinistryOrPositionInnerDTO generateMinistryOrPositionMusicianInnerDTO() {
		return new MinistryOrPositionInnerDTO(new ArrayList(Arrays.asList(32l)));
	}

	public static List<MinistryOrPosition> getMinisteriesOrPositionsList() {
		List<MinistryOrPosition> list = new ArrayList<>();

		list.add(new MinistryOrPosition(1l, "Ancião"));
		list.add(new MinistryOrPosition(2l, "Cooperador do Ofício Ministerial"));
		list.add(new MinistryOrPosition(3l, "Diácono"));
		list.add(new MinistryOrPosition(4l, "Encarregado Regional"));

		return list;
	}
}
