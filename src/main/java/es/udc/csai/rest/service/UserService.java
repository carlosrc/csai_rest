package es.udc.csai.rest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import es.udc.csai.rest.model.User;

public interface UserService extends UserDetailsService {

	boolean checkPassword(String login, String password);

	List<User> findAllUsers();

	User findById(String login);

	User get(String login);

}
