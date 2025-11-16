import axios from 'axios';
import { LoginRequest, LoginResponse } from '../types/auth';

const api = axios.create({
  baseURL: '/api',
  withCredentials: true
});

export const authService = {
  login: async (username: string, password: string): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>('/auth/login', {
      username,
      password
    } as LoginRequest);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await api.post('/auth/logout');
  }
};
