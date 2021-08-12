package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeesRepository employeesRepository;

    @Test
    public void should_return_all_employees_when_call_get_employees_api() throws Exception{
        //given
        final Employee employee = new Employee(1, "Tom", 20, "male", 9999);
        employeesRepository.save(employee);
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tom"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[0].gender").value("male"))
                .andExpect(jsonPath("$[0].salary").value(9999));
    }

    @Test
    public void should_create_employee_when_call_create_employee() throws Exception{
        //given
        String employee = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Tom\",\n" +
                "    \"age\": 20,\n" +
                "    \"gender\": \"male\",\n" +
                "    \"salary\": 2000\n" +
                "}";
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.age").value("20"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.salary").value("2000"));
    }

    @Test
    public void should_update_employee_when_call_update_employee_api() throws Exception{
        //given
        final Employee employee = new Employee(1, "Tom", 20, "male", 9999);
        String updateEmployee = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Alice\",\n" +
                "    \"age\": 23,\n" +
                "    \"gender\": \"female\"\n" +
                "}";
        //when

        //then
        int id = employeesRepository.save(employee).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEmployee))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value("23"))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.salary").value("9999"));
    }
}
