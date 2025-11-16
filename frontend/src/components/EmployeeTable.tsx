import { Employee } from '../types/employee';

interface EmployeeTableProps {
  employees: Employee[];
}

export const EmployeeTable = ({ employees }: EmployeeTableProps) => {
  if (employees.length === 0) {
    return <p>検索結果が見つかりませんでした。</p>;
  }

  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr style={{ borderBottom: '2px solid #ddd' }}>
          <th style={{ padding: '12px', textAlign: 'left' }}>社員コード</th>
          <th style={{ padding: '12px', textAlign: 'left' }}>氏名</th>
          <th style={{ padding: '12px', textAlign: 'left' }}>氏名カナ</th>
          <th style={{ padding: '12px', textAlign: 'left' }}>メールアドレス</th>
          <th style={{ padding: '12px', textAlign: 'left' }}>役職</th>
          <th style={{ padding: '12px', textAlign: 'left' }}>雇用形態</th>
        </tr>
      </thead>
      <tbody>
        {employees.map((employee) => (
          <tr key={employee.id} style={{ borderBottom: '1px solid #ddd' }}>
            <td style={{ padding: '12px' }}>{employee.employeeCode}</td>
            <td style={{ padding: '12px' }}>{employee.fullName}</td>
            <td style={{ padding: '12px' }}>{employee.fullNameKana}</td>
            <td style={{ padding: '12px' }}>{employee.email}</td>
            <td style={{ padding: '12px' }}>{employee.position}</td>
            <td style={{ padding: '12px' }}>{employee.employmentType}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
