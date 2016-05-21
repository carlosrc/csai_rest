package es.udc.csai.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.csai.rest.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private static final AtomicLong counter = new AtomicLong();

	private static HashMap<String, User> users;

	static {
		users = populateDummyUsers();
	}

	@Override
	public boolean checkPassword(String login, String password) {
		return login.equals("carlos") && password.equals("pass");
	}

	@Override
	public List<User> findAllUsers() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public User findById(String login) {
		return users.get(login);
	}

	private static HashMap<String, User> populateDummyUsers() {
		HashMap<String, User> users = new HashMap<String, User>();
		users.put("carlos",
				new User(counter.incrementAndGet(), "carlos", "pass", "ROLE_ADMIN", "A Coruña", "carlos@email.com"));
		users.put("user1",
				new User(counter.incrementAndGet(), "user1", "user1", "ROLE_USER", "A Coruña", "user1@email.com"));
		users.put("user2",
				new User(counter.incrementAndGet(), "user2", "user2", "ROLE_USER", "Madrid", "user2@email.com"));
		users.put("user3",
				new User(counter.incrementAndGet(), "user3", "user3", "ROLE_USER", "Barcelona", "user3@email.com"));
		return users;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = get(login);
		if (user == null) {
			throw new UsernameNotFoundException("User " + login + " does not exists");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, Arrays.asList(new SimpleGrantedAuthority(user.getRole())));
	}

	@Override
	@Transactional(readOnly = true)
	public User get(String login) {
		return users.get(login);
	}

}
