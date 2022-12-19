package com.example.customerprofile.ui.web.cust;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerController controller;

    private RestTemplate restTemplate = mock(RestTemplate.class);

    @BeforeEach
    void setup() {
        controller.restTemplate = restTemplate;
    }

    @Nested
    class Get {

        @Test
        void shouldShowCustomersView() throws Exception {

            mockMvc.perform(get("/protected/customers")
                            .with(oidcLogin())
                            .accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andExpect(content().string(containsString("Customers")));

            verify(restTemplate, times(1)).getForObject("http://localhost:8080/api/customer-profiles/", Customer[].class);
        }

        @Test
        void shouldShowCustomersAddView() throws Exception {

            mockMvc.perform(get("/protected/customers-add")
                            .with(oidcLogin())
                            .accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andExpect(content().string(containsString("Add Customer")));
        }
    }
}

