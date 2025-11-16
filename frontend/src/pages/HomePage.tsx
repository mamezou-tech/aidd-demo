import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';

export const HomePage = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await authService.logout();
      navigate('/login', { state: { message: 'ログアウトしました。' } });
    } catch (err) {
      console.error('Logout failed:', err);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>社員検索システム</h1>
      <p>ユーザー名: admin</p>
      
      <div style={{ marginTop: '20px' }}>
        <button onClick={() => navigate('/employees')} style={{ marginRight: '10px' }}>
          社員検索
        </button>
        <button onClick={handleLogout}>ログアウト</button>
      </div>
    </div>
  );
};
