import { UserResponse } from './user-response';

export interface TaskResponse {
  id: number;
  title: string;
  description?: string;
  status: string; // "PENDIENTE" | "EN_PROGRESO" | "COMPLETADA" | "CANCELADA"
  priority: string; // "BAJA" | "MEDIA" | "ALTA" | "URGENTE"
  assignedTo?: UserResponse;
  createdBy?: UserResponse;
  dueDate?: string;
  completedAt?: string;
  createdAt: string;
  updatedAt: string;
}