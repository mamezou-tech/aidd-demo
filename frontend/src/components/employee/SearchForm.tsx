import { useState } from 'react';
import styles from './SearchForm.module.css';

interface SearchFormProps {
  organizations: Array<{ organizationId: string; name: string }>;
  skills: Array<{ skillId: string; name: string }>;
  onSearch: (criteria: SearchCriteria) => void;
}

interface SearchCriteria {
  name?: string;
  organizationId?: string;
  position?: string;
  employmentType?: string;
  skillIds?: string[];
}

export const SearchForm = ({ organizations, skills, onSearch }: SearchFormProps) => {
  const [name, setName] = useState('');
  const [organizationId, setOrganizationId] = useState('');
  const [position, setPosition] = useState('');
  const [employmentType, setEmploymentType] = useState('');
  const [selectedSkills, setSelectedSkills] = useState<string[]>([]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch({
      name: name || undefined,
      organizationId: organizationId || undefined,
      position: position || undefined,
      employmentType: employmentType || undefined,
      skillIds: selectedSkills.length > 0 ? selectedSkills : undefined,
    });
  };

  const toggleSkill = (skillId: string) => {
    setSelectedSkills(prev =>
      prev.includes(skillId) ? prev.filter(id => id !== skillId) : [...prev, skillId]
    );
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <div className={styles.row}>
        <input
          type="text"
          placeholder="氏名"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className={styles.input}
        />
        <select value={organizationId} onChange={(e) => setOrganizationId(e.target.value)} className={styles.select}>
          <option value="">組織を選択</option>
          {organizations.map(org => (
            <option key={org.organizationId} value={org.organizationId}>{org.name}</option>
          ))}
        </select>
      </div>
      <div className={styles.row}>
        <input
          type="text"
          placeholder="役職"
          value={position}
          onChange={(e) => setPosition(e.target.value)}
          className={styles.input}
        />
        <select value={employmentType} onChange={(e) => setEmploymentType(e.target.value)} className={styles.select}>
          <option value="">雇用区分を選択</option>
          <option value="正社員">正社員</option>
          <option value="契約社員">契約社員</option>
          <option value="派遣社員">派遣社員</option>
        </select>
      </div>
      <div className={styles.skills}>
        <label>スキル:</label>
        {skills.map(skill => (
          <label key={skill.skillId} className={styles.skillLabel}>
            <input
              type="checkbox"
              checked={selectedSkills.includes(skill.skillId)}
              onChange={() => toggleSkill(skill.skillId)}
            />
            {skill.skillName || skill.name}
          </label>
        ))}
      </div>
      <button type="submit" className={styles.button}>検索</button>
    </form>
  );
};
