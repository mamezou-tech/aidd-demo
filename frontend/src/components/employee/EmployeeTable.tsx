import { Avatar } from '../common/Avatar';
import styles from './EmployeeTable.module.css';

interface Employee {
  employeeId: string;
  name: string;
  organizationName: string;
  position: string;
  employmentType: string;
  photoUrl?: string;
}

interface EmployeeTableProps {
  employees: Employee[];
  onSelectEmployee: (id: string) => void;
}

export const EmployeeTable = ({ employees, onSelectEmployee }: EmployeeTableProps) => {
  return (
    <table className={styles.table}>
      <thead>
        <tr>
          <th>顔写真</th>
          <th>社員ID</th>
          <th>氏名</th>
          <th>組織</th>
          <th>役職</th>
          <th>雇用形態</th>
        </tr>
      </thead>
      <tbody>
        {employees.map((employee) => (
          <tr 
            key={employee.employeeId} 
            onClick={() => onSelectEmployee(employee.employeeId)}
            className={styles.row}
          >
            <td>
              <Avatar employeeId={employee.employeeId} size="small" />
            </td>
            <td>{employee.employeeId}</td>
            <td>{employee.name}</td>
            <td>{employee.organizationName}</td>
            <td>{employee.position}</td>
            <td>{employee.employmentType}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
