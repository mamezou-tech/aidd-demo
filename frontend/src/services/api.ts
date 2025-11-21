import axios from 'axios';
import { Employee } from '../types/Employee';
import { Organization } from '../types/Organization';

const API_BASE_URL = 'http://localhost:8080/api';

export const employeeApi = {
  getAll: () => axios.get<Employee[]>(`${API_BASE_URL}/employees`),
  search: (name: string) => axios.get<Employee[]>(`${API_BASE_URL}/employees/search`, { params: { name } }),
  getById: (id: string) => axios.get<Employee>(`${API_BASE_URL}/employees/${id}`)
};

export const organizationApi = {
  getAll: () => axios.get<Organization[]>(`${API_BASE_URL}/organizations`)
};
