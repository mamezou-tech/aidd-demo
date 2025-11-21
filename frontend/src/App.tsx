import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { Login } from './pages/Login';
import { EmployeeSearch } from './pages/EmployeeSearch';
import { EmployeeDetail } from './components/employee/EmployeeDetail';
import { storage } from './utils/storage';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = !!storage.getToken();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/employees"
          element={
            <ProtectedRoute>
              <EmployeeSearch />
            </ProtectedRoute>
          }
        />
        <Route
          path="/employees/:id"
          element={
            <ProtectedRoute>
              <EmployeeDetail onLoad={async (id) => {
                const response = await fetch(`/api/employees/${id}`, {
                  headers: { Authorization: `Bearer ${storage.getToken()}` }
                });
                if (!response.ok) throw new Error('Not found');
                return response.json();
              }} />
            </ProtectedRoute>
          }
        />
        <Route path="/" element={<Navigate to={storage.getToken() ? "/employees" : "/login"} />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
