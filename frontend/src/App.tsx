import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './pages/LoginPage';
import { HomePage } from './pages/HomePage';
import { EmployeeSearchPage } from './pages/EmployeeSearchPage';
import { EmployeeDetailPage } from './pages/EmployeeDetailPage';
import { ErrorPage } from './pages/ErrorPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/employees" element={<EmployeeSearchPage />} />
        <Route path="/employees/:id" element={<EmployeeDetailPage />} />
        <Route path="/error" element={<ErrorPage />} />
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
