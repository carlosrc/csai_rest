package es.udc.csai.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.csai.rest.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private static final AtomicLong counter = new AtomicLong();

	private static List<User> users;

	static {
		users = populateDummyUsers();
	}

	@Override
	public List<User> findAllUsers() {
		return users;
	}

	@Override
	public User findById(long id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	private static List<User> populateDummyUsers() {
		List<User> users = new ArrayList<User>();
		users.add(new User(counter.incrementAndGet(), "user1", "A Coruña", "user1@email.com"));
		users.add(new User(counter.incrementAndGet(), "user2", "Madrid", "user2@email.com"));
		users.add(new User(counter.incrementAndGet(), "user3", "Barcelona", "user3@email.com"));
		return users;
	}

}
