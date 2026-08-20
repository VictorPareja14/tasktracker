import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { TaskService } from '../task.service';
import { AuthService } from '../auth.service';
import { TaskResponse } from '../models/task-response';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  // Variables
  username: string = 'Usuario';
  tasks: TaskResponse[] = [];
  filteredTasks: TaskResponse[] = [];
  searchTerm: string = '';
  selectedFilter: string = 'ALL';

  totalTasks: number = 0;
  pendingTasks: number = 0;
  inProgressTasks: number = 0;
  completedTasks: number = 0;

  constructor(
    private taskService: TaskService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (data) => {
        this.tasks = data.content || [];
        this.filteredTasks = [...this.tasks];
        this.calculateStats();
        console.log('Tareas cargadas:', this.tasks);
      },
      error: (err) => {
        console.error('Error al cargar tareas:', err);
      }
    });
  }

  calculateStats(): void {
    this.totalTasks = this.tasks.length;
    this.pendingTasks = this.tasks.filter(t => t.status === 'PENDIENTE').length;
    this.inProgressTasks = this.tasks.filter(t => t.status === 'EN_PROGRESO').length;
    this.completedTasks = this.tasks.filter(t => t.status === 'COMPLETADA').length;
  }

  filterTasks(): void {
    this.filteredTasks = this.tasks.filter(task => {
      const matchesSearch = task.title.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesStatus = this.selectedFilter === 'ALL' || task.status === this.selectedFilter;
      return matchesSearch && matchesStatus;
    });
  }

  filterByStatus(status: string): void {
    this.selectedFilter = status;
    this.filterTasks();
  }

  statusClass(status: string): string {
    const classes: { [key: string]: string } = {
      'PENDIENTE': 'status-pending',
      'EN_PROGRESO': 'status-progress',
      'COMPLETADA': 'status-completed'
    };
    return classes[status] || '';
  }

  // ==========================================
  // NUEVO MÉTODO PARA FORMATAR EL ESTADO
  // ==========================================
  formatStatus(status: string): string {
    switch (status) {
      case 'PENDIENTE': return 'Pendiente';
      case 'EN_PROGRESO': return 'En Progreso';
      case 'COMPLETADA': return 'Completada';
      default: return status;
    }
  }

  openCreateTask(): void {
    this.router.navigate(['/tasks/create']);
  }

  editTask(id: number): void {
    this.router.navigate([`/tasks/edit/${id}`]);
  }

  deleteTask(id: number): void {
  Swal.fire({
    title: '⚠️ ¿Estás seguro?',
    text: 'Esta acción no se puede deshacer.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: '🗑️ Sí, eliminar',
    cancelButtonText: '↩️ Cancelar',
    reverseButtons: true
  }).then((result) => {
    if (result.isConfirmed) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          Swal.fire({
            title: '✅ Eliminada',
            text: 'La tarea ha sido eliminada correctamente.',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
          });
          this.loadTasks();
        },
        error: (err) => {
          Swal.fire({
            title: '❌ Error',
            text: 'No se pudo eliminar la tarea.',
            icon: 'error'
          });
          console.error('Error al eliminar tarea:', err);
        }
      });
    }
  });
}

  changeStatus(id: number, newStatus: string): void {
    this.taskService.changeStatus(id, newStatus).subscribe({
      next: () => {
        console.log('Estado cambiado:', id, newStatus);
        this.loadTasks();
      },
      error: (err) => {
        console.error('Error al cambiar estado:', err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}