import { useState } from 'react';
import { EmployeeSearchParams } from '../types/employee';

interface EmployeeSearchFormProps {
  onSearch: (params: EmployeeSearchParams) => void;
}

export const EmployeeSearchForm = ({ onSearch }: EmployeeSearchFormProps) => {
  const [params, setParams] = useState<EmployeeSearchParams>({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(params);
  };

  const handleChange = (field: keyof EmployeeSearchParams, value: string) => {
    setParams({ ...params, [field]: value || undefined });
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: '20px' }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
        <div>
          <label>氏名</label>
          <input
            type="text"
            value={params.fullName || ''}
            onChange={(e) => handleChange('fullName', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label>氏名カナ</label>
          <input
            type="text"
            value={params.fullNameKana || ''}
            onChange={(e) => handleChange('fullNameKana', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label>社員コード</label>
          <input
            type="text"
            value={params.employeeCode || ''}
            onChange={(e) => handleChange('employeeCode', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label>メールアドレス</label>
          <input
            type="text"
            value={params.email || ''}
            onChange={(e) => handleChange('email', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label>役職</label>
          <input
            type="text"
            value={params.position || ''}
            onChange={(e) => handleChange('position', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label>雇用形態</label>
          <input
            type="text"
            value={params.employmentType || ''}
            onChange={(e) => handleChange('employmentType', e.target.value)}
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
      </div>
      <button type="submit" style={{ marginTop: '16px', padding: '8px 24px' }}>
        検索
      </button>
    </form>
  );
};
