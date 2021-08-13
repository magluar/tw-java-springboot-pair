package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.repository.CompaniesRepository;
import com.thoughtworks.springbootemployee.service.CompaniesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CompaniesIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompaniesRepository companiesRepository;
    @Autowired
    private CompaniesService companiesService;

}
