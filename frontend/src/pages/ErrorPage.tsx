import { useNavigate } from 'react-router-dom';

export const ErrorPage = () => {
  const navigate = useNavigate();

  return (
    <div style={{ padding: '20px', textAlign: 'center', marginTop: '100px' }}>
      <h1>エラーが発生しました</h1>
      <p style={{ marginTop: '20px', color: '#666' }}>
        申し訳ございません。システムエラーが発生しました。
      </p>
      <button 
        onClick={() => navigate('/home')}
        style={{ 
          marginTop: '30px',
          padding: '10px 20px',
          backgroundColor: '#1976d2',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        ホームへ戻る
      </button>
    </div>
  );
};
