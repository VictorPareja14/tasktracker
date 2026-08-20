import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../task.service';
import { TaskRequest } from '../models/task-request';
import { TaskResponse } from '../models/task-response';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-form.component.html',
  styleUrl: './task-form.component.css'
})
export class TaskFormComponent implements OnInit {
  isEditMode: boolean = false;
  taskId: number | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  task: TaskRequest = {
    title: '',
    description: '',
    priority: 'MEDIA',
    assignedToId: 1,
    dueDate: ''
  };

  users = [
    { id: 1, username: 'admin' },
    { id: 2, username: 'user' }
  ];

  priorities = ['BAJA', 'MEDIA', 'ALTA', 'URGENTE'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.taskId = +params['id'];
        this.loadTask();
      }
    });
  }

  loadTask(): void {
    if (!this.taskId) return;
    this.isLoading = true;
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (task: TaskResponse) => {
        this.task = {
          title: task.title,
          description: task.description || '',
          priority: task.priority,
          assignedToId: task.assignedTo?.id || 1,
          dueDate: task.dueDate || ''
        };
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar la tarea:', err);
        this.errorMessage = 'Error al cargar la tarea. Inténtalo de nuevo.';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (!this.task.title.trim()) {
      this.errorMessage = 'El título es obligatorio.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    if (this.isEditMode && this.taskId) {
      this.taskService.updateTask(this.taskId, this.task).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Error al actualizar la tarea:', err);
          this.errorMessage = 'Error al actualizar la tarea. Inténtalo de nuevo.';
          this.isLoading = false;
        }
      });
    } else {
      this.taskService.createTask(this.task).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Error al crear la tarea:', err);
          this.errorMessage = 'Error al crear la tarea. Inténtalo de nuevo.';
          this.isLoading = false;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}