import { Employee } from '../types/Employee';

interface Props {
  employee: Employee;
  onClose: () => void;
}

export const EmployeeDetail = ({ employee, onClose }: Props) => {
  return (
    <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '600px', width: '100%' }}>
        <h2>社員詳細</h2>
        <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
          <img src={employee.photoPath} alt={employee.name} width="150" />
          <div>
            <p><strong>氏名:</strong> {employee.name}</p>
            <p><strong>社員ID:</strong> {employee.employeeId}</p>
            <p><strong>役職:</strong> {employee.position}</p>
            <p><strong>雇用形態:</strong> {employee.employmentType}</p>
            <p><strong>入社日:</strong> {employee.hireDate}</p>
            <p><strong>メール:</strong> {employee.email}</p>
          </div>
        </div>
        <button onClick={onClose}>閉じる</button>
      </div>
    </div>
  );
};
