import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api';
  private isLoggedIn = false;
  private username: string = '';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + btoa(username + ':' + password)
    });

    return this.http.get(`${this.baseUrl}/tasks`, { headers }).pipe(
      tap(() => {
        this.isLoggedIn = true;
        this.username = username;
        localStorage.setItem('credentials', btoa(username + ':' + password));
        localStorage.setItem('username', username);
      })
    );
  }

  logout(): void {
    this.isLoggedIn = false;
    this.username = '';
    localStorage.removeItem('credentials');
    localStorage.removeItem('username');
  }

  isAuthenticated(): boolean {
    return this.isLoggedIn || !!localStorage.getItem('credentials');
  }

  getUsername(): string {
    return this.username || localStorage.getItem('username') || 'Usuario';
  }
}