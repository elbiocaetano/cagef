package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.http.request.city.UserRequestParamsDTO;
import br.org.rpf.cagef.dto.user.UserDTO;
import br.org.rpf.cagef.entity.User;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private UserServiceImpl userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CityRepository cityRepository;

	@MockBean
	private BCryptPasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		when(cityRepository.findById(any())).thenReturn(Optional.of(CityServiceImplTest.generateCity()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void loadByUsernameSuccessTest() {
		when(userRepository.findOne(any(Example.class))).thenReturn(Optional.of(generateUserNotAdmin()));

		User user = this.userService.loadUserByUsername("mail@mail.com");

		assertTrue(user.getId() == 1l);
		assertTrue(user.getName() == "Teste");
		assertTrue(user.getEmail() == "mail@mail.com");
		assertTrue(user.getPassword() == "123");
		assertTrue(user.getRole() == "ROLE_USUARIO");
		assertTrue(user.getCity().getId() == 1l);
		assertTrue(user.getCity().getName() == "Teste");
		assertTrue(user.getCity().getState() == "SP");
		assertTrue(user.getCity().getRegional() == true);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void loadByUsernameErrorTest() {
		when(userRepository.findOne(any(Example.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.userService.loadUserByUsername("mail@mail.com");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void loadByUsernameNotFoundTest() {
		when(userRepository.findOne(any(Example.class))).thenReturn(Optional.ofNullable(null));

		expectedEx.expect(ObjectNotFoundException.class);

		this.userService.loadUserByUsername("mail@mail.com");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllErrorTest() {
		when(userRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.userService.findAll(UserRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllSuccessTest() {
		when(userRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(new PageImpl<User>(getUsersList()));
		Page<User> users = this.userService.findAll(UserRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<User> usersList = users.getContent();
		assertEquals(4, usersList.size());
		assertEquals(1l, usersList.get(0).getId().longValue());
		assertEquals("Casa Branca", usersList.get(0).getName());
		assertEquals("mail@mail.com", usersList.get(0).getEmail());
		assertEquals("123", usersList.get(0).getPassword());
		assertEquals("ROLE_USUARIO", usersList.get(0).getRole());
		assertEquals(1l, usersList.get(0).getCity().getId().longValue());
		assertEquals("Teste", usersList.get(0).getCity().getName());
		assertEquals("SP", usersList.get(0).getCity().getState());
		assertTrue(usersList.get(0).getCity().getRegional());

		assertEquals(2l, usersList.get(1).getId().longValue());
		assertEquals("Mococa", usersList.get(1).getName());
		assertEquals("mail@mail.com", usersList.get(1).getEmail());
		assertEquals("123", usersList.get(1).getPassword());
		assertEquals("ROLE_ADMIN", usersList.get(1).getRole());
		assertEquals(1l, usersList.get(1).getCity().getId().longValue());
		assertEquals("Teste", usersList.get(1).getCity().getName());
		assertEquals("SP", usersList.get(1).getCity().getState());
		assertTrue(usersList.get(1).getCity().getRegional());

		assertEquals(3l, usersList.get(2).getId().longValue());
		assertEquals("Jaguariuna", usersList.get(2).getName());
		assertEquals("mail@mail.com", usersList.get(2).getEmail());
		assertEquals("123", usersList.get(2).getPassword());
		assertEquals("ROLE_USUARIO", usersList.get(2).getRole());
		assertEquals(1l, usersList.get(2).getCity().getId().longValue());
		assertEquals("Teste", usersList.get(2).getCity().getName());
		assertEquals("SP", usersList.get(2).getCity().getState());
		assertTrue(usersList.get(2).getCity().getRegional());

		assertEquals(4l, usersList.get(3).getId().longValue());
		assertEquals("Poços de Caldas", usersList.get(3).getName());
		assertEquals("mail@mail.com", usersList.get(3).getEmail());
		assertEquals("123", usersList.get(3).getPassword());
		assertEquals("ROLE_ADMIN", usersList.get(3).getRole());
		assertEquals(1l, usersList.get(3).getCity().getId().longValue());
		assertEquals("Teste", usersList.get(3).getCity().getName());
		assertEquals("SP", usersList.get(3).getCity().getState());
		assertTrue(usersList.get(3).getCity().getRegional());
		
		verify(userRepository).findAll(any(Example.class), any(Pageable.class));
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		when(userRepository.findById(1l)).thenReturn(Optional.ofNullable(null));

		this.userService.byId(1l);
	}

	@Test
	public void findByIdSuccessTest() {
		when(userRepository.findById(1l)).thenReturn(Optional.of(generateUserNotAdmin()));

		User user = this.userService.byId(1l);
		assertEquals(1l, user.getId().longValue());
		assertEquals("Teste", user.getName());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("123", user.getPassword());
		assertEquals("ROLE_USUARIO", user.getRole());
		assertEquals(1l, user.getCity().getId().longValue());
		assertEquals("Teste", user.getCity().getName());
		assertEquals("SP", user.getCity().getState());
		assertTrue(user.getCity().getRegional());
		
		verify(userRepository).findById(1l);
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		when(userRepository.save(any())).thenThrow(new Exception("Error"));

		this.userService.save(generateUserDto());
	}

	@Test
	public void saveSuccessTest() {
		when(userRepository.save(any(User.class))).thenReturn(generateUserNotAdmin());

		User user = this.userService.save(generateUserDto());
		assertEquals(1l, user.getId().longValue());
		assertEquals("Teste", user.getName());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("123", user.getPassword());
		assertEquals("ROLE_USUARIO", user.getRole());
		assertEquals(1l, user.getCity().getId().longValue());
		assertEquals("Teste", user.getCity().getName());
		assertEquals("SP", user.getCity().getState());
		assertTrue(user.getCity().getRegional());
		
		verify(userRepository).save(any(User.class));
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		when(userRepository.save(any())).thenThrow(new Exception("Error"));

		this.userService.save(generateUserDto());
	}

	@Test
	public void updateSuccessEmptyPasswordTest() {
		Mockito.doNothing().when(userRepository).updateUser(eq(1l), eq("Teste"), any(), eq("ROLE_USUARIO"));

		User user = this.userService.update(1l, generateUserDtoBlankPassword());
		assertEquals(1l, user.getId().longValue());
		assertEquals("Teste", user.getName());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals(null, user.getPassword());
		assertEquals("ROLE_USUARIO", user.getRole());
		assertEquals(1l, user.getCity().getId().longValue());
		assertEquals("Teste", user.getCity().getName());
		assertEquals("SP", user.getCity().getState());
		assertTrue(user.getCity().getRegional());
		
		verify(userRepository).updateUser(eq(1l), eq("Teste"), any(), eq("ROLE_USUARIO"));
	}

	@Test
	public void updateSuccessTest() {
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(userRepository.save(any())).thenReturn(generateUserNotAdmin());

		User user = this.userService.update(1l, generateUserDto());
		assertEquals(1l, user.getId().longValue());
		assertEquals("Teste", user.getName());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("123", user.getPassword());
		assertEquals("ROLE_USUARIO", user.getRole());
		assertEquals(1l, user.getCity().getId().longValue());
		assertEquals("Teste", user.getCity().getName());
		assertEquals("SP", user.getCity().getState());
		assertTrue(user.getCity().getRegional());
		
		verify(passwordEncoder).encode(any());
		verify(userRepository).save(any());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void updateCityNotFoundTest() {
		when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(userRepository.save(any())).thenReturn(generateUserNotAdmin());

		this.userService.update(1l, generateUserDto());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(userRepository).deleteById(1l);

		this.userService.save(generateUserDto());
	}

	@Test
	public void removeSuccessTest() {
		Mockito.doNothing().when(userRepository).deleteById(1l);

		this.userService.remove(1l);
		
		verify(userRepository).deleteById(1l);
	}

	public static List<User> getUsersList() {
		List<User> list = new ArrayList<>();

		list.add(new User(1l, "Casa Branca", CityServiceImplTest.generateCity(), "mail@mail.com", "123",
				"ROLE_USUARIO"));
		list.add(new User(2l, "Mococa", CityServiceImplTest.generateCity(), "mail@mail.com", "123", "ROLE_ADMIN"));
		list.add(
				new User(3l, "Jaguariuna", CityServiceImplTest.generateCity(), "mail@mail.com", "123", "ROLE_USUARIO"));
		list.add(new User(4l, "Poços de Caldas", CityServiceImplTest.generateCity(), "mail@mail.com", "123",
				"ROLE_ADMIN"));

		return list;
	}

	public static User generateUserAdmin() {
		return new User(1l, "Teste", CityServiceImplTest.generateCity(), "mail@mail.com", "123", "ROLE_ADMIN");
	}
	
	public static User generateUserMusicAdmin() {
		return new User(1l, "Teste", CityServiceImplTest.generateCity(), "mail@mail.com", "123", "ROLE_ADMIN_MUSICA");
	}
	
	public static User generateUserNotAdmin() {
		return new User(1l, "Teste", CityServiceImplTest.generateCity(), "mail@mail.com", "123", "ROLE_USUARIO");
	}

	public static UserDTO generateUserDtoBlankPassword() {
		return new UserDTO(1l, "Teste", CityServiceImplTest.generateCityInnerDTO(), "mail@mail.com", null,
				"ROLE_USUARIO");
	}

	public static UserDTO generateUserDto() {
		return new UserDTO(1l, "Teste", CityServiceImplTest.generateCityInnerDTO(), "mail@mail.com", "123",
				"ROLE_USUARIO");
	}
}
