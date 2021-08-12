package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeesRepository;
import com.thoughtworks.springbootemployee.service.EmployeesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private EmployeesService employeesService;

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

    @Test
    public void should_return_employee_id_when_call_find_employee_by_id_api() throws Exception{
        //given
        final Employee employee = new Employee(1, "Tom", 20, "male", 9999);
        employeesRepository.save(employee);
        //when

        //then
        int id = 1;
        Employee searchEmployee = employeesService.findEmployeeById(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(searchEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.salary").value(9999));
    }

    @Test
    public void should_return_employees_by_gender_when_find_employee_by_gender_api() throws Exception{
        //given
        final Employee employee1 = new Employee(1, "Tom", 20, "male", 9999);
        final Employee employee2 = new Employee(2, "Jerry", 21, "male", 9999);
        final Employee employee3 = new Employee(2, "Alice", 21, "female", 9999);
        employeesRepository.save(employee1);
        employeesRepository.save(employee2);
        employeesRepository.save(employee3);
        //when

        //then
        String gender = "male";
        List<Employee> searchEmployee = employeesRepository.findAllByGender(gender);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees").param("gender", gender))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gender").value("male"))
                .andExpect(jsonPath("$[1].gender").value("male"));
    }
}
