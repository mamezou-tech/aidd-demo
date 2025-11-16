import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { employeeService } from '../services/employeeService';
import { Employee, EmployeeSearchParams, PageResponse } from '../types/employee';
import { EmployeeSearchForm } from '../components/EmployeeSearchForm';
import { EmployeeTable } from '../components/EmployeeTable';
import { Pagination } from '../components/Pagination';

export const EmployeeSearchPage = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState<EmployeeSearchParams>({});
  const [result, setResult] = useState<PageResponse<Employee> | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchEmployees = async (params: EmployeeSearchParams) => {
    setLoading(true);
    setError(null);
    try {
      const data = await employeeService.search(params);
      setResult(data);
    } catch (err) {
      setError('検索に失敗しました。');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees({ page: 0, size: 20 });
  }, []);

  const handleSearch = (params: EmployeeSearchParams) => {
    const newParams = { ...params, page: 0, size: 20 };
    setSearchParams(newParams);
    fetchEmployees(newParams);
  };

  const handlePageChange = (page: number) => {
    const newParams = { ...searchParams, page, size: 20 };
    setSearchParams(newParams);
    fetchEmployees(newParams);
  };

  return (
    <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
      <h1>社員検索</h1>
      <button onClick={() => navigate('/home')} style={{ marginBottom: '20px' }}>
        ホームへ戻る
      </button>

      <EmployeeSearchForm onSearch={handleSearch} />

      {loading && <p>読み込み中...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      {result && (
        <>
          <p>検索結果: {result.totalElements}件</p>
          <EmployeeTable employees={result.content} />
          <Pagination
            currentPage={result.number}
            totalPages={result.totalPages}
            onPageChange={handlePageChange}
          />
        </>
      )}
    </div>
  );
};
