package com.demo.language_booking.users;

import com.demo.language_booking.users.dto.UserCreateRequest;
import com.demo.language_booking.users.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserService {
	/**
	 * Authenticates if the username and password are correct.
	 *
	 * @param username         The unique username of the user.
	 * @param unhashedPassword The unhashed password.
	 * @return Returns the {@link User} if it matches, otherwise returns empty.
	 */
	Optional<User> authenticate(@NotNull String username, @NotNull String unhashedPassword);

	/**
	 * Gets all the users in the database.
	 *
	 * @return A list of User.
	 */
	@NonNull
	List<User> getAll();

	/**
	 * Get a user by the given id if it exists.
	 *
	 * @param id the unique identifier of the user.
	 * @return a {@link User} if it exists, otherwise returns empty.
	 */
	Optional<User> findById(long id);

	/**
	 * Creates a new user resource
	 *
	 * @param userCreateRequest the data required for a new user
	 * @return a newly created {@link User}
	 */
	@NonNull
	User create(@Valid @NotNull UserCreateRequest userCreateRequest);

	/**
	 * Updates an existing user resource with the provided data.
	 *
	 * @param id                the unique identifier of the user to be updated
	 * @param userUpdateRequest the request object containing updated user information
	 * @return the updated User entity
	 */
	@NonNull
	User update(long id, @Valid @NotNull UserUpdateRequest userUpdateRequest);

	/**
	 * Deletes a user resource by the given id.
	 *
	 * @param id the id of the user resource to delete
	 */
	void delete(long id);
}
