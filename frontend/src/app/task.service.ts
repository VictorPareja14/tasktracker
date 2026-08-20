import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskRequest } from './models/task-request';
import { TaskResponse } from './models/task-response';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  /**
   * Obtiene todas las tareas del usuario autenticado
   * GET /api/tasks
   */
  getTasks(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  /**
   * Obtiene una tarea por ID
   * GET /api/tasks/{id}
   */
  getTaskById(id: number): Observable<TaskResponse> {
    return this.http.get<TaskResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crea una nueva tarea
   * POST /api/tasks
   */
  createTask(task: TaskRequest): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(this.apiUrl, task);
  }

  /**
   * Actualiza una tarea existente
   * PUT /api/tasks/{id}
   */
  updateTask(id: number, task: TaskRequest): Observable<TaskResponse> {
    return this.http.put<TaskResponse>(`${this.apiUrl}/${id}`, task);
  }

  /**
   * Elimina una tarea
   * DELETE /api/tasks/{id}
   */
  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Cambia el estado de una tarea
   * PATCH /api/tasks/{id}/status?status=...
   */
  changeStatus(id: number, status: string): Observable<TaskResponse> {
    return this.http.patch<TaskResponse>(
      `${this.apiUrl}/${id}/status?status=${status}`,
      {}
    );
  }
}