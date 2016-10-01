package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.PickupRequest;
import com.zzheads.CompShop.service.QwintryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class QwintryApiTest {
    @Mock
    private MockHttpSession session;

    @Mock
    private QwintryService qwintryService;
    @InjectMocks
    private QwintryApi qwintryApi;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(qwintryApi).build();
    }

    @Test
    public void costPickupTest() throws Exception {
        final String ANSWER_COST = "Cost of delivery to pickup point";
        PickupRequest pickupRequest = new PickupRequest("100", "100x100x100", "msk_1", "false", "649.56", "hundredths-inches", "hundredths-pounds");
        when(qwintryService.getCostPickup(pickupRequest.toJson())).thenReturn(ANSWER_COST);
        MvcResult result = mockMvc.perform(post("/costpickup").contentType(MediaType.APPLICATION_JSON).content(pickupRequest.toJson())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(ANSWER_COST, result.getResponse().getContentAsString());
        verify(qwintryService).getCostPickup(any(String.class));
    }

}