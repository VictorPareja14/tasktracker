import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../task.service';
import { TaskResponse } from '../models/task-response';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-detail.component.html',
  styleUrl: './task-detail.component.css'
})
export class TaskDetailComponent implements OnInit {
  task: TaskResponse | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      if (id) {
        this.loadTask(id);
      }
    });
  }

  loadTask(id: number): void {
    this.isLoading = true;
    this.taskService.getTaskById(id).subscribe({
      next: (task) => {
        this.task = task;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar la tarea:', err);
        this.errorMessage = 'Error al cargar la tarea. Inténtalo de nuevo.';
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  editTask(): void {
    if (this.task) {
      this.router.navigate([`/tasks/edit/${this.task.id}`]);
    }
  }

  statusClass(status: string): string {
    const classes: { [key: string]: string } = {
      'PENDIENTE': 'status-pending',
      'EN_PROGRESO': 'status-progress',
      'COMPLETADA': 'status-completed'
    };
    return classes[status] || '';
  }
}