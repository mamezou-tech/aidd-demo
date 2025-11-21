import { Employee } from '../types/Employee';

interface Props {
  employees: Employee[];
  onSelectEmployee: (employee: Employee) => void;
}

export const EmployeeList = ({ employees, onSelectEmployee }: Props) => {
  return (
    <div>
      <h2>社員一覧</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ padding: '10px' }}>写真</th>
            <th style={{ padding: '10px' }}>氏名</th>
            <th style={{ padding: '10px' }}>役職</th>
            <th style={{ padding: '10px' }}>メール</th>
          </tr>
        </thead>
        <tbody>
          {employees.map(emp => (
            <tr 
              key={emp.employeeId} 
              onClick={() => onSelectEmployee(emp)}
              style={{ cursor: 'pointer', borderBottom: '1px solid #eee' }}
            >
              <td style={{ padding: '10px' }}><img src={emp.photoPath} alt={emp.name} width="50" /></td>
              <td style={{ padding: '10px' }}>{emp.name}</td>
              <td style={{ padding: '10px' }}>{emp.position}</td>
              <td style={{ padding: '10px' }}>{emp.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
