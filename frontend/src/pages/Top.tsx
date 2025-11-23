import { useNavigate } from 'react-router-dom';
import { storage } from '../utils/storage';
import styles from './Top.module.css';

export const Top = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    storage.clearToken();
    navigate('/login');
  };

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h1>タレントマネジメントシステム</h1>
        <button onClick={handleLogout} className={styles.logoutButton}>
          ログアウト
        </button>
      </header>
      
      <main className={styles.main}>
        <section className={styles.category}>
          <h2 className={styles.categoryTitle}>社員管理</h2>
          <button 
            onClick={() => navigate('/employees')} 
            className={styles.menuButton}
          >
            社員検索
          </button>
        </section>
      </main>
    </div>
  );
};
