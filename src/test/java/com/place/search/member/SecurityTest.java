package com.place.search.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.place.search.controller.SearchController;
import com.place.search.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchController.class)
public class SecurityTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  MemberService memberService;

  @WithMockUser(roles = "MEMBER")
  @Test
  public void givenAuthRequestService_shouldSucceedWith200() throws Exception {
    mvc.perform(get("/search"))
        .andExpect(status().isOk());
  }

  @WithMockUser(roles = "TEST")
  @Test
  public void givenNonAuthRequestService_shouldFailWith403() throws Exception {
    mvc.perform(get("/search"))
        .andExpect(status().is4xxClientError());
  }

}
