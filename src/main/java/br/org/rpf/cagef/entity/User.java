package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = -4118435083584469602L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToOne
	@JoinColumn(name = "cidade")
    private City city;
    @Column(name = "email", unique=true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "remember_token")
    private String rememberToken;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
	@Column(name = "created_at", updatable = false)
    private Date createdAt;

	
	public User(String email) {
		this.email = email;
	}
	
	public User(Long id, String name, City city, String email, String role) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.email = email;
		this.role = role;
	}
	
	public User(Long id, String name, City city, String email, String password, String role) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public Collection<SimpleGrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(this.getRole()));
	}
	
	@Override
	public String getUsername() {
		return this.getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
