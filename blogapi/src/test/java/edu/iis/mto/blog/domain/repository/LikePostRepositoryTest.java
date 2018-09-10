package edu.iis.mto.blog.domain.repository;

import java.util.List;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
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
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    private User user;
    private User user2;

    private BlogPost blogPost;
    private BlogPost blogPost2;

    private LikePost likePost;
    private LikePost likePost2;

    @Before
    public void setUp() {

        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        user = entityManager.persist(user);

        blogPost = new BlogPost();
        blogPost.setEntry("Test");
        blogPost.setUser(user);
        blogPost = entityManager.persist(blogPost);

        user2 = new User();
        user2.setFirstName("Adam");
        user2.setLastName("Nawałka");
        user2.setEmail("adalka@domain.com");
        user2.setAccountStatus(AccountStatus.NEW);
        user2 = entityManager.persist(user2);

        blogPost2 = new BlogPost();
        blogPost2.setEntry("Test nr 2");
        blogPost2.setUser(user);
        blogPost2 = entityManager.persist(blogPost2);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        likePost2 = new LikePost();
        likePost2.setUser(user2);
        likePost2.setPost(blogPost2);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        Assert.assertThat(repository.findAll(), Matchers.hasSize(0));
    }

    @Test
    public void shouldFindTwoLikePostsInRepository() {

        repository.save(likePost);
        repository.save(likePost2);
        Assert.assertThat(repository.findAll(), Matchers.hasSize(2));
    }

    @Test
    public void shouldFindProperLikePostInRepositoryConsistingTwoLikePosts() {

        repository.save(likePost);
        repository.save(likePost2);
        Assert.assertTrue(repository.findByUserAndPost(user, blogPost).isPresent());
        Assert.assertThat(repository.findByUserAndPost(user, blogPost).get(), Matchers.is(likePost));
    }

}
