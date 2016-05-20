package es.udc.csai.rest.service;

import java.util.List;

import es.udc.csai.rest.model.User;

public interface UserService {

	User findById(long id);

	List<User> findAllUsers();

}
