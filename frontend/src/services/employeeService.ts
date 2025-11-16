import axios from 'axios';
import { Employee, EmployeeSearchParams, PageResponse } from '../types/employee';

const api = axios.create({
  baseURL: '/api',
  withCredentials: true
});

export const employeeService = {
  search: async (params: EmployeeSearchParams): Promise<PageResponse<Employee>> => {
    const response = await api.get<PageResponse<Employee>>('/employees', { params });
    return response.data;
  },

  getById: async (id: number): Promise<Employee> => {
    const response = await api.get<Employee>(`/employees/${id}`);
    return response.data;
  },
};
