import { Avatar } from '../common/Avatar';
import styles from './EmployeeCard.module.css';

interface EmployeeCardProps {
  employee: {
    employeeId: string;
    name: string;
    organizationId?: string;
    organizationName?: string;
    position: string;
  };
  onClick: () => void;
}

export const EmployeeCard = ({ employee, onClick }: EmployeeCardProps) => {
  return (
    <div className={styles.card} onClick={onClick} data-testid={`employee-card-${employee.employeeId}`}>
      <Avatar employeeId={employee.employeeId} size="small" />
      <div className={styles.info}>
        <div className={styles.name}>{employee.name}</div>
        <div className={styles.detail}>{employee.organizationName || employee.organizationId || '組織未設定'}</div>
        <div className={styles.detail}>{employee.position}</div>
      </div>
    </div>
  );
};
