package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.org.rpf.cagef.dto.http.request.city.UserRequestParamsDTO;
import br.org.rpf.cagef.dto.user.UserDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.entity.User;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.UserRepository;
import br.org.rpf.cagef.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User loadUserByUsername(String username) {
		return this.userRepository.findOne(Example.of(new User(username)))
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(username, User.class.getName()));
	}

	@Override
	public Page<User> findAll(UserRequestParamsDTO requestParams) {

		return userRepository.findAll(Example.of(new User(requestParams.getId(), requestParams.getName(),
				new City(null, requestParams.getCity()), requestParams.getEmail(), requestParams.getRole()),
				getExampleMatcher()), requestParams.getPageRequest());
	}

	@Override
	public User byId(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, User.class.getName()));
	}

	@Override
	public User save(UserDTO user) {
		return this.userRepository.save(this.fromDTO(null, user));
	}

	@Override
	public User update(Long id, UserDTO userDTO) {
		User user = fromDTO(id, userDTO);
		if(!StringUtils.isEmpty(user.getPassword())) {
			return this.userRepository.save(user);
		}
		this.userRepository.updateUser(user.getId(), user.getName(), user.getCity(), user.getRole());
		return user;
	}

	@Override
	public void remove(Long id) {
		this.userRepository.deleteById(id);
	}

	private User fromDTO(Long id, UserDTO userDTO) {
		City city = cityRepository.findById(userDTO.getCity().getId()).orElseThrow(
				() -> new org.hibernate.ObjectNotFoundException(userDTO.getCity().getId(), City.class.getName()));
			return new User(id, userDTO.getName(), city, userDTO.getEmail(), encodePassword(userDTO.getPassword()), userDTO.getRole());
	}
	
	private String encodePassword(String password) {
		if(!StringUtils.isEmpty(password)) {
			return passwordEncoder.encode(password);
		}
		return null;
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase().withMatcher("name", new GenericPropertyMatcher().contains())
				.withMatcher("city.name", new GenericPropertyMatcher().contains())
				.withMatcher("email", new GenericPropertyMatcher().contains())
				.withMatcher("state", new GenericPropertyMatcher().contains())
				.withMatcher("role", new GenericPropertyMatcher().contains());

	}
}
