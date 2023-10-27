package com.example.rqchallenge.service;

import com.example.rqchallenge.adapter.EmployeeAdapter;
import com.example.rqchallenge.datastructure.EmployeeTrie;
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
import com.example.rqchallenge.utils.AnagramUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.*;

@Service
public class EmployeeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeManager.class);
    private AbstractExternalService<GetEmplResponse, GetEmplParams> getEmplService;
    private AbstractExternalService<GetEmployeesResponse, GetEmployeeParams> getEmployeesService;

    private AbstractExternalService<CreateEmplResponse, CreateEmplRequestParams> createEmployeeService;
    private AbstractExternalService<DeleteEmplResponse, DeleteEmplRequest> deleteEmployeeService;

    @Autowired
    public EmployeeManager(AbstractExternalService<GetEmplResponse, GetEmplParams> getEmplService,
                           AbstractExternalService<GetEmployeesResponse, GetEmployeeParams> getEmployeesService,
                           AbstractExternalService<CreateEmplResponse, CreateEmplRequestParams> createEmployeeService,
                           AbstractExternalService<DeleteEmplResponse, DeleteEmplRequest> deleteEmployeeService) {
        this.getEmplService = getEmplService;
        this.getEmployeesService = getEmployeesService;
        this.createEmployeeService = createEmployeeService;
        this.deleteEmployeeService = deleteEmployeeService;
    }

    public Employee getEmployeeDetails(String id){
        Map<GetEmplParams,Object> args = new HashMap<GetEmplParams,Object>();
        args.put(GetEmplParams.ID,id);
        Employee employee = null;
        try {
            GetEmplResponse response = getEmplService.callExternalService(args);
            GetEmplResponse.EmplDetailsObj emplDetils = response.getData();
            employee = Employee.builder()
                    .age(emplDetils.getAge())
                    .salary(emplDetils.getAge())
                    .name(emplDetils.getName())
                    .id(emplDetils.getId())
                    .build();
        } catch (ExternalServiceException e) {
            LOGGER.error("External Service failed %s", e);
        } catch (InvalidResponseDataException e) {
            LOGGER.error("Invalid External Service Response %s", e);
        } catch (URISyntaxException e) {
            LOGGER.error("External Service failed %s", e);
        }
        return employee;
    }

    public List<Employee> getAllEmployees(){
        Map<GetEmployeeParams,Object> args = new HashMap<GetEmployeeParams,Object>();
        try {
            GetEmployeesResponse response = getEmployeesService.callExternalService(args);
            List<GetEmployeesResponse.EmployeeDetails> employeeDetails = response.getData();
            return EmployeeAdapter.buildEmployee(employeeDetails);
        } catch (ExternalServiceException e) {
            LOGGER.error("External Service failed %s", e);
        } catch (InvalidResponseDataException e) {
            LOGGER.error("Invalid External Service Response %s", e);
        } catch (URISyntaxException e) {
            LOGGER.error("External Service failed %s", e);
        }
        return new ArrayList<Employee>();
    }

    public Employee createEmployee(Map<String,Object> emplDetails){
        Map<CreateEmplRequestParams, Object> args = new HashMap<CreateEmplRequestParams, Object>();
        args.put(CreateEmplRequestParams.NAME,emplDetails.get("name"));
        args.put(CreateEmplRequestParams.AGE,emplDetails.get("age"));
        args.put(CreateEmplRequestParams.SALARY, emplDetails.get("salary"));

        try {
            CreateEmplResponse response =  createEmployeeService.callExternalService(args);
            CreateEmplResponse.EmplObj empl = response.getData();

            List<String> anagrams = AnagramUtils.getAllAnagrams(empl.getName());

            Employee employee = Employee.builder()
                    .name(empl.getName())
                    .age(empl.getAge())
                    .id(empl.getId())
                    .salary(empl.getSalary())
                    .build();

            for(String anagram : anagrams){
                EmployeeTrie.insertEmployee(anagram, employee);
            }

            return employee;

        } catch (InvalidResponseDataException e) {
            LOGGER.error("External Service failed %s", e);
        } catch (ExternalServiceException e) {
            LOGGER.error("Invalid External Service Response %s", e);
        } catch (URISyntaxException e) {
            LOGGER.error("External Service failed %s", e);
        }
        return null;
    }

    public String deleteEmployee(String id){
        Map<DeleteEmplRequest, Object> args = new HashMap<DeleteEmplRequest, Object>();
        args.put(DeleteEmplRequest.ID, id);
        try {
            Employee employee = getEmployeeDetails(id);
            if(employee != null){
                deleteEmployeeService.callExternalService(args);

                List<String> anagrams = AnagramUtils.getAllAnagrams(employee.getName());
                for(String anagram: anagrams){
                    EmployeeTrie.delete(anagram);
                }

                return employee.getName();
            }

        } catch (InvalidResponseDataException e) {
            LOGGER.error("External Service failed %s", e);
        } catch (ExternalServiceException e) {
            LOGGER.error("Invalid External Service Response %s", e);
        } catch (URISyntaxException e) {
            LOGGER.error("External Service failed %s", e);
        }
        return null;
    }

    public List<String> getTopTenEmpl(){
        List<Employee> employeeList = getAllEmployees();
        sortOnSalary(employeeList);
        return EmployeeAdapter.buildEmployeeName(employeeList);
    }

    public Integer getHighestPaidEmployee(){
        List<Employee> employeeList = getAllEmployees();
        sortOnSalary(employeeList);
        return employeeList.get(0).getId();
    }

    private void sortOnSalary(List<Employee> employees){
        Collections.sort(employees, new Comparator<Employee>() {
            public int compare(Employee a, Employee b) {
                return a.getSalary() - b.getSalary();
            }
        });
        Collections.reverse(employees);
    }

    public List<Employee> searchEmployee(String searchString){
        /* We can implement full text search via Elasticsearch / creation of in-memory
           inverted index and calculation of tf-idf
         */
        List<String> searchStringAnagrams = AnagramUtils.getAllAnagrams(searchString);
        Set<Employee> result = new HashSet<Employee>();

        for(String str : searchStringAnagrams){
            Employee employee =  EmployeeTrie.findEmployee(str);
            result.add(employee);
        }
        return new ArrayList<Employee>(result);
    }

}
