package io.mirukman.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.mirukman.config.RootConfig;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
@Slf4j
@WebAppConfiguration
public class BoardControllerTest {
    
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void listTest() throws Exception {
        
        log.info(
            mockMvc.perform(MockMvcRequestBuilders.get("/board/list").param("page", "2"))
            .andReturn()
            .getModelAndView()
            .getModelMap()
            .toString()
        );
    }

    @Test
    public void getTest() throws Exception {
        log.info(
            mockMvc.perform(MockMvcRequestBuilders.get("/board/get").param("bno", "3"))
            .andReturn()
            .getModelAndView()
            .getModelMap()
            .toString()
        );
    }
    
    @Test
    public void regtisterTest() throws Exception {
        MockHttpServletRequestBuilder MockHttpServletRequestBuilder =  MockMvcRequestBuilders.post("/board/register")
            .param("title", "테스트 새글 제목")
            .param("content", "테스트 새글 내용")
            .param("writer", "user00");

        String resultPage = mockMvc.perform(MockHttpServletRequestBuilder)
            .andReturn()
            .getModelAndView()
            .getViewName();
            
        assertEquals("redirect:/board/list", resultPage);
    }
    
    @Test
    public void modifyTest() throws Exception {
        MockHttpServletRequestBuilder MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/board/modify")
            .param("bno", "1")
            .param("title", "테스트 새글 제목22")
            .param("content", "테스트 새글 내용22")
            .param("writer", "user22");

        String resultPage = mockMvc.perform(MockHttpServletRequestBuilder)
            .andReturn()
            .getModelAndView()
            .getViewName();
            
        assertEquals("redirect:/board/list", resultPage);
    }
    
    @Test
    public void removeTest() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/board/remove")
                .param("bno", "1");

        String resultPage = mockMvc.perform(mockHttpServletRequestBuilder)
            .andReturn()
            .getModelAndView()
            .getViewName();

        assertEquals("redirect:/board/list", resultPage);
    }
}
