package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User user, user2;
    private BlogPost blogPost;

    @Before
    public void setUp() {

        user = new User();
        user.setFirstName("Tomasz");
        user.setLastName("Bogdan");
        user.setEmail("tobo@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        user.setId(11L);

        user2 = new User();
        user2.setFirstName("Janusz");
        user2.setLastName("Toporek");
        user2.setEmail("jantop@domain.com");
        user2.setAccountStatus(AccountStatus.CONFIRMED);
        user2.setId(32L);

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(user);
        blogPost.setEntry("Test Entry");
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {

        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void confirmedUserShouldBeAbleToLikePost() {

        Mockito.when(userRepository.findOne(user2.getId())).thenReturn(user2);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> list = Optional.empty();
        Mockito.when(likePostRepository.findByUserAndPost(user2, blogPost)).thenReturn(list);

        Assert.assertThat(blogService.addLikeToPost(user2.getId(), blogPost.getId()), Matchers.is(true));
    }

    @Test(expected = DomainError.class)
    public void newUserShouldNotBeAbleToLikePost() {

        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> list = Optional.empty();
        Mockito.when(likePostRepository.findByUserAndPost(user, blogPost)).thenReturn(list);

        Assert.assertThat(blogService.addLikeToPost(user.getId(), blogPost.getId()), Matchers.is(false));
    }
}
