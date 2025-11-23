import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { storage } from '../utils/storage';

export const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await axios.post('/api/auth/login', { email, password });
      storage.setToken(response.data.token);
      navigate('/');
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response) {
          // バックエンドからのエラーレスポンス
          const message = err.response.data?.message || '認証に失敗しました';
          setError(message);
        } else if (err.request) {
          // バックエンドが起動していない場合
          setError('サーバーに接続できません。バックエンドが起動しているか確認してください。');
        } else {
          setError('認証に失敗しました');
        }
      } else {
        setError('認証に失敗しました');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSkipAuth = async () => {
    setError('');
    setLoading(true);

    try {
      const response = await axios.post('/api/auth/skip');
      storage.setToken(response.data.token);
      navigate('/');
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response) {
          const message = err.response.data?.message || '認証スキップに失敗しました';
          setError(message);
        } else if (err.request) {
          setError('サーバーに接続できません。バックエンドが起動しているか確認してください。');
        } else {
          setError('認証スキップに失敗しました');
        }
      } else {
        setError('認証スキップに失敗しました');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '100px auto', padding: '20px' }}>
      <h1>ログイン</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="メールアドレス"
            style={{ width: '100%', padding: '8px' }}
            required
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="パスワード"
            style={{ width: '100%', padding: '8px' }}
            required
          />
        </div>
        {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}
        <button type="submit" style={{ width: '100%', padding: '10px' }} disabled={loading}>
          {loading ? 'ログイン中...' : 'ログイン'}
        </button>
      </form>
      <div style={{ marginTop: '15px', textAlign: 'center' }}>
        <button
          onClick={handleSkipAuth}
          style={{
            width: '100%',
            padding: '10px',
            backgroundColor: '#6c757d',
            color: 'white',
            border: 'none',
            cursor: 'pointer'
          }}
          disabled={loading}
        >
          デモ用（認証スキップ）
        </button>
      </div>
    </div>
  );
};

