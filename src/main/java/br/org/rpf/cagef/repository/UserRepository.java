package br.org.rpf.cagef.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Transactional
	@Modifying
	@Query("update User u set u.name = :name, u.city = :city, u.role = :role where id = :id")
	public void updateUser(@Param("id") Long id, @Param("name") String name, @Param("city") City city, @Param("role") String role);

}
