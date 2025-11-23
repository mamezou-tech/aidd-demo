import { useState } from 'react';
import { SearchForm } from './SearchForm';
import { EmployeeCard } from './EmployeeCard';
import { EmployeeTable } from './EmployeeTable';
import { Spinner } from '../common/Spinner';
import { Pagination } from '../common/Pagination';
import styles from './EmployeeList.module.css';

interface EmployeeListProps {
  organizations: Array<{ organizationId: string; name: string }>;
  skills: Array<{ skillId: string; name: string }>;
  onSearch: (criteria: any, page: number) => Promise<any>;
  onSelectEmployee: (employeeId: string) => void;
}

export const EmployeeList = ({ organizations, skills, onSearch, onSelectEmployee }: EmployeeListProps) => {
  const [employees, setEmployees] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [searchCriteria, setSearchCriteria] = useState<any>(null);
  const [displayMode, setDisplayMode] = useState<'list' | 'card'>('list');

  const handleSearch = async (criteria: any) => {
    setSearchCriteria(criteria);
    setCurrentPage(1);
    await loadEmployees(criteria, 1);
  };

  const handlePageChange = async (page: number) => {
    setCurrentPage(page);
    await loadEmployees(searchCriteria, page);
  };

  const loadEmployees = async (criteria: any, page: number) => {
    setLoading(true);
    try {
      const result = await onSearch(criteria, page);
      // 組織名マッピングを追加
      const orgMap = new Map(organizations.map(org => [org.organizationId, org.name]));
      const employeesWithOrgName = result.employees.map((emp: any) => ({
        ...emp,
        organizationName: orgMap.get(emp.organizationId) || emp.organizationId
      }));
      setEmployees(employeesWithOrgName);
      setTotalPages(result.totalPages);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <SearchForm 
        organizations={organizations} 
        skills={skills} 
        onSearch={handleSearch}
        displayMode={displayMode}
        onDisplayModeChange={setDisplayMode}
      />
      {loading && <Spinner />}
      {!loading && employees.length === 0 && searchCriteria && (
        <div className={styles.noResults}>該当する社員が見つかりません</div>
      )}
      {!loading && employees.length > 0 && (
        <>
          {displayMode === 'list' ? (
            <EmployeeTable employees={employees} onSelectEmployee={onSelectEmployee} />
          ) : (
            <div className={styles.grid}>
              {employees.map(emp => (
                <EmployeeCard
                  key={emp.employeeId}
                  employee={emp}
                  onClick={() => onSelectEmployee(emp.employeeId)}
                />
              ))}
            </div>
          )}
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={handlePageChange}
          />
        </>
      )}
    </div>
  );
};
