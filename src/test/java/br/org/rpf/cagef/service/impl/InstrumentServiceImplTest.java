package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.http.request.city.InstrumentRequestParamsDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentCategoryInnerDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.InstrumentCategory;
import br.org.rpf.cagef.repository.InstrumentCategoryRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.util.InstrumentSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstrumentServiceImplTest {
	
	@Autowired
	@InjectMocks
	private InstrumentServiceImpl instrumentService;
	
	@MockBean
	private InstrumentRepository instrumentRepository;
	
	@MockBean
	private InstrumentCategoryRepository instrumentCategoryRepository;
	
	@Before
	public void setup() {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(UserServiceImplTest.generateUserNotAdmin());
		
		when(instrumentRepository.findById(1l)).thenReturn(Optional.of(generateInstrument()));
		when(instrumentCategoryRepository.findById(1l)).thenReturn(Optional.of(generateInstrumentCategory()));
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void findAllErrorTest() {
		when(instrumentRepository.findAll(any(InstrumentSpecification.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));
		this.instrumentService.findAll(InstrumentRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
	}
	
	@Test
	public void findAllSuccessTest() {
		when(instrumentRepository.findAll(any(InstrumentSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Instrument>(getInstrumentList()));
		Page<Instrument> instruments = this.instrumentService.findAll(InstrumentRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		
		List<Instrument> instrumentsList = instruments.getContent();
		assertEquals(4, instrumentsList.size());
		assertEquals(1l, instrumentsList.get(0).getId().longValue());
		assertEquals("Saxofone Soprano", instrumentsList.get(0).getDescription());
		assertEquals(1l, instrumentsList.get(0).getCategory().getId().longValue());
		assertEquals("Madeiras", instrumentsList.get(0).getCategory().getDescription());
		
		assertEquals(2l, instrumentsList.get(1).getId().longValue());
		assertEquals("Saxofone Alto", instrumentsList.get(1).getDescription());
		assertEquals(1l, instrumentsList.get(1).getCategory().getId().longValue());
		assertEquals("Madeiras", instrumentsList.get(1).getCategory().getDescription());
		
		assertEquals(3l, instrumentsList.get(2).getId().longValue());
		assertEquals("Violino", instrumentsList.get(2).getDescription());
		assertEquals(2l, instrumentsList.get(2).getCategory().getId().longValue());
		assertEquals("Cordas", instrumentsList.get(2).getCategory().getDescription());
		
		assertEquals(4l, instrumentsList.get(3).getId().longValue());
		assertEquals("Trompete", instrumentsList.get(3).getDescription());
		assertEquals(3l, instrumentsList.get(3).getCategory().getId().longValue());
		assertEquals("Metais", instrumentsList.get(3).getCategory().getDescription());
		
		verify(this.instrumentRepository).findAll(any(InstrumentSpecification.class), any(Pageable.class));
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void byIdNotFoundTest() {
		when(instrumentRepository.findById(1l))
				.thenReturn(Optional.empty());
		this.instrumentService.byId(1l);
	}
	
	@Test
	public void byIdSuccessTest() {
		Instrument instrument = this.instrumentService.byId(1l);
		
		assertEquals(1l, instrument.getId().longValue());
		assertEquals("Saxofone", instrument.getDescription());
		assertEquals(1l, instrument.getCategory().getId().longValue());
		assertEquals("Madeiras", instrument.getCategory().getDescription());
		
		verify(instrumentRepository).findById(1l);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void saveInstrumentCategoryNotFoundError() {
		when(instrumentCategoryRepository.findById(1l)).thenReturn(Optional.empty());
		this.instrumentService.save(generateInstrumentDTO());
	}
	
	@Test
	public void saveInstrumentSucessTest() {
		when(this.instrumentRepository.save(any(Instrument.class))).thenReturn(generateInstrument());
		Instrument instrument = this.instrumentService.save(generateInstrumentDTO());
		assertEquals(1l, instrument.getId().longValue());
		assertEquals("Saxofone", instrument.getDescription());
		assertEquals(1l, instrument.getCategory().getId().longValue());
		assertEquals("Madeiras", instrument.getCategory().getDescription());
		
		verify(this.instrumentRepository).save(any(Instrument.class));
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void updateInstrumentNotFoundError() {
		when(instrumentRepository.findById(1l)).thenReturn(Optional.empty());
		this.instrumentService.update(1l, generateInstrumentDTO());
	}
	
	@Test
	public void updateInstrumentSucessTest() {
		when(this.instrumentRepository.save(any(Instrument.class))).thenReturn(generateInstrument());
		Instrument instrument = this.instrumentService.update(1l, generateInstrumentDTO());
		assertEquals(1l, instrument.getId().longValue());
		assertEquals("Saxofone", instrument.getDescription());
		assertEquals(1l, instrument.getCategory().getId().longValue());
		assertEquals("Madeiras", instrument.getCategory().getDescription());
		
		verify(this.instrumentRepository).save(any(Instrument.class));
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void removeInstrumentErrorTest() {
		when(instrumentRepository.findById(1l)).thenReturn(Optional.empty());
		this.instrumentService.remove(1l);
	}
	
	@Test
	public void removeInstrumentSuccessTest() {
		this.instrumentService.remove(1l);
		
		verify(instrumentRepository).findById(1l);
		verify(instrumentRepository).delete(any(Instrument.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void findAllCategoriesSuccessTest() {
		when(instrumentCategoryRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(new PageImpl<InstrumentCategory>(Arrays.asList(generateInstrumentCategory())));
		Page<InstrumentCategory> categories = this.instrumentService.findAllCategories(null, null, 0, 50, "id", "ASC");

		List<InstrumentCategory> categoriesList = categories.getContent();
		assertEquals(1, categoriesList.size());
		assertEquals(1l, categoriesList.get(0).getId().longValue());
		assertEquals("Madeiras", categoriesList.get(0).getDescription());
		verify(instrumentCategoryRepository).findAll(any(Example.class), any(Pageable.class));
	}
	
	public static List<Instrument> getInstrumentList() {
		List<Instrument> list = new ArrayList<>();
		list.add(Instrument.builder().id(1l).description("Saxofone Soprano")
				.category(InstrumentCategory.builder().id(1l).description("Madeiras").build()).build());
		list.add(Instrument.builder().id(2l).description("Saxofone Alto")
				.category(InstrumentCategory.builder().id(1l).description("Madeiras").build()).build());
		list.add(Instrument.builder().id(3l).description("Violino")
				.category(InstrumentCategory.builder().id(2l).description("Cordas").build()).build());
		list.add(Instrument.builder().id(4l).description("Trompete")
				.category(InstrumentCategory.builder().id(3l).description("Metais").build()).build());
		return list;
	}
	
	public static List<InstrumentCategory> getInstrumentCategoryList() {
		List<InstrumentCategory> list = new ArrayList<>();
		list.add(InstrumentCategory.builder().id(1l).description("Madeiras").build());
		list.add(InstrumentCategory.builder().id(2l).description("Cordas").build());
		list.add(InstrumentCategory.builder().id(3l).description("Metais").build());
		list.add(InstrumentCategory.builder().id(4l).description("Outros").build());
		return list;
	}
	
	
	public static Instrument generateInstrument() {
		return new Instrument(1l, "Saxofone", generateInstrumentCategory());
	}
	
	public static InstrumentCategory generateInstrumentCategory() {
		return new InstrumentCategory(1l, "Madeiras");
	}
	
	public static InstrumentDTO generateInstrumentDTO() {
		return InstrumentDTO.builder().description("Saxofone").category(new InstrumentCategoryInnerDTO(1l)).build();
	}
}
