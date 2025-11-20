import { useState, useEffect } from 'react';
import { storage } from '../../utils/storage';
import styles from './Avatar.module.css';

interface AvatarProps {
  employeeId: string;
  size?: 'small' | 'large';
}

export const Avatar = ({ employeeId, size = 'small' }: AvatarProps) => {
  const [photoUrl, setPhotoUrl] = useState<string>('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;
    
    const loadPhoto = async () => {
      try {
        const token = storage.getToken();
        const url = `/api/employees/${employeeId}/photo`;
        
        const response = await fetch(url, {
          headers: { Authorization: `Bearer ${token}` }
        });
        
        if (response.ok && isMounted) {
          const blob = await response.blob();
          const objectUrl = URL.createObjectURL(blob);
          setPhotoUrl(objectUrl);
        }
      } catch (err) {
        console.error('Failed to load photo:', err);
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    loadPhoto();
    
    return () => {
      isMounted = false;
      if (photoUrl) {
        URL.revokeObjectURL(photoUrl);
      }
    };
  }, [employeeId]);

  if (loading) {
    return <div className={size === 'large' ? styles.large : styles.small} style={{ background: '#f0f0f0' }} />;
  }

  return (
    <img
      src={photoUrl}
      alt="Employee"
      className={size === 'large' ? styles.large : styles.small}
    />
  );
};
