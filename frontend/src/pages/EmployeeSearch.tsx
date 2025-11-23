import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { EmployeeList } from '../components/employee/EmployeeList';
import { storage } from '../utils/storage';

export const EmployeeSearch = () => {
  const navigate = useNavigate();
  const [organizations, setOrganizations] = useState<any[]>([]);
  const [skills, setSkills] = useState<any[]>([]);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadMasterData();
  }, []);

  const loadMasterData = async () => {
    try {
      const token = storage.getToken();
      const headers = { Authorization: `Bearer ${token}` };
      
      const [orgsRes, skillsRes] = await Promise.all([
        axios.get('/api/organizations', { headers }),
        axios.get('/api/skills', { headers })
      ]);
      
      setOrganizations(orgsRes.data);
      setSkills(skillsRes.data);
    } catch (err) {
      console.error('Failed to load master data:', err);
      setError('マスタデータの読み込みに失敗しました');
    }
  };

  const handleSearch = async (criteria: any, page: number) => {
    try {
      const token = storage.getToken();
      const params = new URLSearchParams({
        page: String(page - 1),
        size: '20',
      });
      
      if (criteria.name) params.append('name', criteria.name);
      if (criteria.organizationId) params.append('organizationId', criteria.organizationId);
      if (criteria.position) params.append('position', criteria.position);
      if (criteria.employmentType) params.append('employmentType', criteria.employmentType);
      if (criteria.skillIds && criteria.skillIds.length > 0) {
        criteria.skillIds.forEach((skillId: string) => params.append('skillIds', skillId));
      }
      
      const response = await axios.get(`/api/employees?${params}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      return {
        employees: response.data.content || response.data,
        totalPages: response.data.totalPages || 1
      };
    } catch (err) {
      console.error('Search failed:', err);
      setError('検索に失敗しました');
      return { employees: [], totalPages: 0 };
    }
  };

  const handleLogout = () => {
    storage.removeToken();
    navigate('/login');
  };

  return (
    <div>
      <div style={{ padding: '10px', borderBottom: '1px solid #ccc', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <button onClick={() => navigate('/')} style={{ padding: '8px 16px' }}>TOPへ戻る</button>
          <h1 style={{ margin: 0 }}>社員検索システム</h1>
        </div>
        <button onClick={handleLogout} style={{ padding: '8px 16px' }}>ログアウト</button>
      </div>
      {error && <div style={{ color: 'red', padding: '10px' }}>{error}</div>}
      <EmployeeList
        organizations={organizations}
        skills={skills}
        onSearch={handleSearch}
        onSelectEmployee={(id) => navigate(`/employees/${id}`)}
      />
    </div>
  );
};
