export interface Employee {
  id: number;
  employeeCode: string;
  fullName: string;
  fullNameKana: string;
  email: string;
  position: string;
  employmentType: string;
  hireDate: string;
}

export interface EmployeeSearchParams {
  fullName?: string;
  fullNameKana?: string;
  employeeCode?: string;
  email?: string;
  position?: string;
  employmentType?: string;
  page?: number;
  size?: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
