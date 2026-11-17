# TaskTracker Pro

Aplicación de gestión de tareas con autenticación, dashboard y CRUD de tareas.
Backend en **Java 21 + Spring Boot + PostgreSQL**, frontend en **Angular**, todo
dockerizado y levantable con un solo comando.

##  Stack

| Capa | Tecnología |
|---|---|
| **Backend** | Java 21, Spring Boot, Spring Data JPA, Spring Security (Basic Auth) |
| **Frontend** | Angular, TypeScript, CSS |
| **Base de datos** | PostgreSQL 16 |
| **Infraestructura** | Docker, Docker Compose, Nginx |

##  Cómo levantar el proyecto

**Requisito:** tener Docker y Docker Compose instalados.

```bash
git clone https://github.com/VictorPareja14/tasktracker.git
cd tasktracker
docker-compose up --build -d