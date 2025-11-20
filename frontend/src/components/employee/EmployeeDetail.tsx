import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Avatar } from '../common/Avatar';
import { Spinner } from '../common/Spinner';
import styles from './EmployeeDetail.module.css';

interface EmployeeDetailData {
  employeeId: string;
  name: string;
  email: string;
  position: string;
  employmentType: string;
  hireDate: string;
  organizationId?: string;
  organizationName?: string;
  skills?: Array<{ skillName: string; level: number }>;
}

interface EmployeeDetailProps {
  onLoad: (id: string) => Promise<EmployeeDetailData>;
}

export const EmployeeDetail = ({ onLoad }: EmployeeDetailProps) => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [employee, setEmployee] = useState<EmployeeDetailData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      loadEmployee(id);
    }
  }, [id]);

  const loadEmployee = async (employeeId: string) => {
    setLoading(true);
    setError(null);
    try {
      const data = await onLoad(employeeId);
      setEmployee(data);
    } catch (err) {
      console.error('Failed to load employee:', err);
      setError('社員が見つかりません');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Spinner />;
  if (error) return <div className={styles.error}>{error}</div>;
  if (!employee) return null;

  return (
    <div className={styles.container}>
      <button onClick={() => navigate('/employees')} className={styles.backButton}>
        ← 一覧へ戻る
      </button>
      <div className={styles.content}>
        <Avatar employeeId={employee.employeeId} size="large" />
        <div className={styles.info}>
          <h2>{employee.name}</h2>
          <div className={styles.field}>
            <span className={styles.label}>社員ID:</span> {employee.employeeId}
          </div>
          <div className={styles.field}>
            <span className={styles.label}>組織:</span> {employee.organizationName || employee.organizationId || '未設定'}
          </div>
          <div className={styles.field}>
            <span className={styles.label}>役職:</span> {employee.position}
          </div>
          <div className={styles.field}>
            <span className={styles.label}>雇用形態:</span> {employee.employmentType}
          </div>
          <div className={styles.field}>
            <span className={styles.label}>入社日:</span> {employee.hireDate}
          </div>
          <div className={styles.field}>
            <span className={styles.label}>メール:</span> {employee.email}
          </div>
        </div>
      </div>
      {employee.skills && employee.skills.length > 0 && (
        <div className={styles.skills}>
          <h3>スキル</h3>
          <ul>
            {employee.skills.map((skill, idx) => (
              <li key={idx}>
                {skill.skillName} - レベル {skill.level}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};
