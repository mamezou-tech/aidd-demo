import axios from 'axios';
import { Employee, EmployeeSearchParams, PageResponse } from '../types/employee';

const API_BASE_URL = 'http://localhost:8080/api';

export const employeeService = {
  search: async (params: EmployeeSearchParams): Promise<PageResponse<Employee>> => {
    const response = await axios.get<PageResponse<Employee>>(`${API_BASE_URL}/employees`, {
      params,
      withCredentials: true,
    });
    return response.data;
  },

  getById: async (id: number): Promise<Employee> => {
    const response = await axios.get<Employee>(`${API_BASE_URL}/employees/${id}`, {
      withCredentials: true,
    });
    return response.data;
  },
};
