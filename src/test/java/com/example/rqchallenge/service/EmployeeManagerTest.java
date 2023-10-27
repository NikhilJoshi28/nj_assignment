package com.example.rqchallenge.service;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.exceptions.ExternalServiceException;
import com.example.rqchallenge.exceptions.InvalidResponseDataException;
import com.example.rqchallenge.service.external.AbstractExternalService;
import com.example.rqchallenge.service.external.employee.createempl.CreateEmplRequestParams;
import com.example.rqchallenge.service.external.employee.createempl.CreateEmplResponse;
import com.example.rqchallenge.service.external.employee.deleteempl.DeleteEmplRequest;
import com.example.rqchallenge.service.external.employee.deleteempl.DeleteEmplResponse;
import com.example.rqchallenge.service.external.employee.getempl.GetEmplParams;
import com.example.rqchallenge.service.external.employee.getempl.GetEmplResponse;
import com.example.rqchallenge.service.external.employee.getempls.GetEmployeeParams;
import com.example.rqchallenge.service.external.employee.getempls.GetEmployeesResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeManagerTest {

    @InjectMocks
    private EmployeeManager employeeManager;

    @Mock
    private AbstractExternalService<GetEmplResponse, GetEmplParams> getEmplService;

    @Mock
    private AbstractExternalService<GetEmployeesResponse, GetEmployeeParams> getEmployeesService;

    @Mock
    private AbstractExternalService<CreateEmplResponse, CreateEmplRequestParams> createEmployeeService;

    @Mock
    private AbstractExternalService<DeleteEmplResponse, DeleteEmplRequest> deleteEmployeeService;

    private GetEmplResponse getEmplResponse;
    private GetEmplResponse.EmplDetailsObj emplDetailsObj;
    private GetEmployeesResponse getEmployeesResponse;
    private CreateEmplResponse createEmplResponse;

    @Before
    public void setUp() throws Exception {

        getEmplResponse = new GetEmplResponse();
        getEmplResponse.setStatus("success");
        emplDetailsObj = new GetEmplResponse.EmplDetailsObj();
        emplDetailsObj.setId(1);
        emplDetailsObj.setName("example");
        emplDetailsObj.setAge(20);
        emplDetailsObj.setSalary(1000);
        getEmplResponse.setData(emplDetailsObj);

        getEmployeesResponse = new GetEmployeesResponse();
        GetEmployeesResponse.EmployeeDetails details1 = new GetEmployeesResponse.EmployeeDetails();
        details1.setId(1);
        details1.setSalary(2000);
        details1.setName("example1");

        GetEmployeesResponse.EmployeeDetails details2 = new GetEmployeesResponse.EmployeeDetails();
        details1.setId(2);
        details1.setSalary(21000);
        details1.setName("example2");
        List<GetEmployeesResponse.EmployeeDetails> data = new ArrayList<GetEmployeesResponse.EmployeeDetails>(
                Arrays.asList(details1, details2));
        getEmployeesResponse.setData(data);
    }

    @Test
    public void getEmployeeDetails() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {
        String emplId = "1";
        when(getEmplService.callExternalService(ArgumentMatchers.<GetEmplParams, Object>anyMap()))
                .thenReturn(getEmplResponse);
        Employee employee = employeeManager.getEmployeeDetails(emplId);
        Assert.assertEquals(1, employee.getId());
    }

    @Test
    public void getAllEmployees() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {

        when(getEmployeesService.callExternalService(ArgumentMatchers.<GetEmployeeParams, Object>anyMap()))
                .thenReturn(getEmployeesResponse);
        List<Employee> employeeList = employeeManager.getAllEmployees();
        assertTrue(employeeList.size()==2);
    }

    @Test
    public void createEmployee() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {
        when(createEmployeeService.callExternalService(ArgumentMatchers.<CreateEmplRequestParams, Object>anyMap()))
                .thenReturn(createEmplResponse);
        Map<String,Object> empl = new HashMap<String, Object>();
        empl.put("name", "example3");
        empl.put("salary", 1000);
        empl.put("age", 40);
        Employee employee = employeeManager.createEmployee(empl);
        assertNotNull(employee);
        assertEquals("example3",employee.getName());
    }

    @Test(expected = Test.None.class)
    public void deleteEmployee() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {
        when(deleteEmployeeService.callExternalService(ArgumentMatchers.<DeleteEmplRequest, Object>anyMap()))
                .thenReturn(null);
        when(getEmployeesService.callExternalService(ArgumentMatchers.<GetEmployeeParams, Object>anyMap()))
                .thenReturn(getEmployeesResponse);
        String emplId = "1";
        employeeManager.deleteEmployee(emplId);
    }

    @Test
    public void getTopTenEmpl() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {
        when(getEmployeesService.callExternalService(ArgumentMatchers.<GetEmployeeParams, Object>anyMap()))
                .thenReturn(getEmployeesResponse);
        List<Employee> employeeList = employeeManager.getAllEmployees();
        assertTrue(employeeList.size()==2);
        assertEquals(2, employeeList.get(0).getId());
        assertEquals(1, employeeList.get(1).getId());
    }

    @Test
    public void getHighestPaidEmployee() throws ExternalServiceException, InvalidResponseDataException, URISyntaxException {
        when(getEmployeesService.callExternalService(ArgumentMatchers.<GetEmployeeParams, Object>anyMap()))
                .thenReturn(getEmployeesResponse);
        List<Employee> employeeList = employeeManager.getAllEmployees();
        assertTrue(employeeList.size()==2);
        assertEquals(2, employeeList.get(0).getId());
    }
}