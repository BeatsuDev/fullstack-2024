package no.ntnu.fullstack.backend.quiz;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.security.UserDetailsImpl;
import no.ntnu.fullstack.backend.security.UserDetailsServiceImpl;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @MockBean
  private UserDetailsImpl userDetails;

  @BeforeEach
  public void setup() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail("user@example");
    user.setPassword("password");
    user.setName("user");

    userDetails = new UserDetailsImpl(user);

    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);

    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
  }

  @Test
  public void createQuiz_ValidRequest_ReturnsQuiz() throws Exception {
    String quizCreateJson = "{\"title\":\"Example Quiz\",\"description\":\"An example quiz.\"}";


    mockMvc
        .perform(post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Example Quiz"))
        .andExpect(jsonPath("$.description").value("An example quiz."));
  }

    @Test
    public void createQuiz_InvalidRequest_ReturnsBadRequest() throws Exception {
        String quizCreateJson = "{\"title\":\"\",\"description\":\"\"}";

        mockMvc
            .perform(post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreateJson))
            .andExpect(status().isBadRequest());
    }
}
