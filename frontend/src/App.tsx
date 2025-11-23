import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Login } from './pages/Login';
import { Top } from './pages/Top';
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
          path="/"
          element={
            <ProtectedRoute>
              <Top />
            </ProtectedRoute>
          }
        />
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
      </Routes>
    </BrowserRouter>
  );
}

export default App;
