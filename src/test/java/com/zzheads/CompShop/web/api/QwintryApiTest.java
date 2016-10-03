package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.PickupRequest;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private ProductService productService;
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
        Product product = new Product("Test product", "Test desc", "photo url", 495.67, 537.67, 0, null, null);
        product.setWeight(100);
        product.setHeight(100);
        product.setLength(100);
        product.setWidth(100);
        product.setUnitsL("cm");
        product.setUnitsW("kg");
        final String CALCULATED_COST_OF_DELIVERY = "56.78";
        when(productService.findById(1L)).thenReturn(product);
        when(qwintryService.getCostPickup(product.getPickupRequest("msk_1", "false").toJson())).thenReturn(CALCULATED_COST_OF_DELIVERY);

        MvcResult result = mockMvc.perform(get("/costpickup/1").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(CALCULATED_COST_OF_DELIVERY, result.getResponse().getContentAsString());
        verify(qwintryService).getCostPickup(any(String.class));
    }

}