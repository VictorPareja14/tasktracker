export interface TaskRequest {
  title: string;
  description?: string;
  priority: string; // "BAJA" | "MEDIA" | "ALTA" | "URGENTE"
  assignedToId: number;
  dueDate?: string; // Formato: YYYY-MM-DD
}