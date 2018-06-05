package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {

        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldNotFindUserThatIsNotContaintedInDatabase() {
        User persistedUser = entityManager.persist(user);
        Assert.assertThat(repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("???", "???", "???"), Matchers.not(Matchers.contains(user)));
    }

    @Test
    public void shouldFindUserByMatchingFirstName() {
        User persistedUser = entityManager.persist(user);
        Assert.assertThat(repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "???", "???"), Matchers.contains(user));
    }

    @Test
    public void shouldFindUserByMatchingLastName() {
        User persistedUser = entityManager.persist(user);
        Assert.assertThat(repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("???", "Nowak", "???"), Matchers.contains(user));
    }

    @Test
    public void shouldFindUserByMatchingEmail() {
        User persistedUser = entityManager.persist(user);
        Assert.assertThat(repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("???", "???", "john@domain.com"), Matchers.contains(user));
    }

    @Test
    public void shouldFindUserByMatchingFirstNameLastNameAndEmail() {
        User persistedUser = entityManager.persist(user);
        Assert.assertThat(repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("???", "???", "john@domain.com"), Matchers.contains(user));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);
        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseAllValuesWrong() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Bartek", "Werner", "Bartek.Werner@gmail.com");
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseEmailValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Bartek", "Werner", "john@domain.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseLastNameValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Bartek", "Kowalski", "Bartek.Werner@gmail.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseLastNameAndEmailValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Bartek", "Kowalski", "john@domain.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseFirstNameValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Werner", "Bartek.Werner@gmail.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseFirstNameAndEmailValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Werner", "john@domain.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseFirstNameAndLastNameValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Kowalski", "Bartek.Werner@gmail.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserWithDefaultDataUsingFindByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCaseFirstNameAndLastNameAndEmailValid() {

        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Kowalski", "john@domain.com");
        Assert.assertThat(users, Matchers.hasSize(1));
    }
}
