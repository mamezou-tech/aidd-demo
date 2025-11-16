import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { employeeService } from '../services/employeeService';
import { Employee } from '../types/employee';

export const EmployeeDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchEmployee = async () => {
      if (!id) return;
      
      setLoading(true);
      setError(null);
      try {
        const data = await employeeService.getById(Number(id));
        setEmployee(data);
      } catch (err) {
        setError('社員情報の取得に失敗しました。');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchEmployee();
  }, [id]);

  if (loading) return <div style={{ padding: '20px' }}>読み込み中...</div>;
  if (error) return <div style={{ padding: '20px', color: 'red' }}>{error}</div>;
  if (!employee) return <div style={{ padding: '20px' }}>社員が見つかりません。</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <h1>社員詳細</h1>
      
      <div style={{ marginBottom: '20px' }}>
        <button onClick={() => navigate('/employees')} style={{ marginRight: '10px' }}>
          検索画面へ戻る
        </button>
        <button onClick={() => navigate('/home')}>
          ホームへ戻る
        </button>
      </div>

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <tbody>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', width: '200px', backgroundColor: '#f5f5f5' }}>
              社員コード
            </th>
            <td style={{ padding: '12px' }}>{employee.employeeCode}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              氏名
            </th>
            <td style={{ padding: '12px' }}>{employee.fullName}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              氏名カナ
            </th>
            <td style={{ padding: '12px' }}>{employee.fullNameKana}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              メールアドレス
            </th>
            <td style={{ padding: '12px' }}>{employee.email}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              役職
            </th>
            <td style={{ padding: '12px' }}>{employee.position || '-'}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              雇用形態
            </th>
            <td style={{ padding: '12px' }}>{employee.employmentType}</td>
          </tr>
          <tr style={{ borderBottom: '1px solid #ddd' }}>
            <th style={{ padding: '12px', textAlign: 'left', backgroundColor: '#f5f5f5' }}>
              入社日
            </th>
            <td style={{ padding: '12px' }}>{employee.hireDate}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};
