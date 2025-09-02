# 💼 RecruitingApp

## 🚀 Recruiting Platform App

A full-stack recruitment platform simulating real-world job portals like LinkedIn, built with Spring Boot
backend and React frontend.

## 👥 User Stories Implemented

- **As a 🎯recruiter**: I can post jobs and manage hiring processes.
- **As a 👤candidate**: I can apply for jobs and follow my application status.
- **As an 🔧administrator**: I can manage users and platform content.

## ✨ Essential Features

- Job posting and candidate management.
- Applications and follow-up.
- User management.

## 👥 User Types

- 🎯Recruiters.
- 👤Candidates.
- 🔧Administrators.

### Core Functionality

- ✅ **Job Management** - 🎯Recruiters create, edit, and publish job offers.
- ✅ **Application System** - 👤Candidates can apply for jobs and track status.
- ✅ **User Management** - Role-based access control.
- ✅ **Notification System** - Real-time updates for applications.

## 🛠️ Technology Stack

### Backend (Current Implementation)

- **Java 21** - Programming language
- **Spring Boot 3.5** - Framework
- **Spring Data JPA** - Database persistence
- **Spring Security** - Authentication & authorization
- **MySQL** - Database
- **Maven** - Dependency management
- **Lombok** - Code reduction

### Frontend (Planned)

- React.js
- JavaScript/TypeScript
- CSS3/Tailwind

## 📊 Database Design

![ER Diagram](src/main/resources/static/images/erd-diagram.png)

The database schema includes:

- **Users** - Base user information with role-based access
- **JobOffers** - Job postings and details
- **Applications** - Application tracking system
- **Companies** - Company information
- **Notifications** - User notification system
- **AdminLogs** - Admin logs

## 📁 Project Structure

```
recruiting_app_backend
├── config
│ ├── AdminDataLoader.java
│ └── CompanyDataLoader.java
├── controller
│ └── UserController.java
├── dto
├── model
│ ├── AdminLog.java
│ ├── Application.java
│ ├── Company.java
│ ├── JobOffer.java
│ ├── Notification.java
│ ├── UserCompany.java
│ └── User.java
├── RecruitingAppBackendApplication.java
├── repository
│ ├── ICompanyRepository.java
│ └── IUserRepository.java
├── security
│ ├── AuthController.java
│ ├── AuthRequestDTO.java
│ ├── AuthResponseDTO.java
│ ├── AuthService.java
│ ├── CustomUserDetailsService.java
│ ├── JwtAuthenticationFilter.java
│ ├── JwtUtil.java
│ ├── RegisterRequestDTO.java
│ └── SecurityConfig.java
└── service
    └── UserService.java
```

## 🔐 Security Features

- Role-based authentication (Candidate, Recruiter, Admin)
- JWT token-based security
- Password encryption
- Protected API endpoints
- CORS configuration

## API Endpoints

| Method | Endpoint                            | Description             | Access            |
|--------|-------------------------------------|-------------------------|-------------------|
| POST   | `/api/auth/register`                | User registration       | Public            |
| POST   | `/api/auth/login`                   | User authentication     | Public            |
| POST   | `/api/job-offers/create`            | Create job offer        | Recruiter         |
| GET    | `/api/job-offers/getAllJobOffers`   | List all job offers     | Public            |
| GET    | `/api/job-offers/getJobOfferById/7` | List job offer by id    | Public            |
| GET    | `/api/job-offers/getMyJobOffers`    | Get recruiter's jobs    | Recruiter         |
| DELETE | `/api/job-offers/{id}`              | Delete job offer        | Recruiter (owner) |
| UPADTE | `/api/job-offers/update`            | Update recruiter's jobs | Recruiter         |

## 📌 Project Management

This project is managed using **[Taiga](https://www.taiga.io/)** for backlog, sprints, and epics tracking.

- 📋 [Backlog](https://tree.taiga.io/project/martinbergagno-recruitingapp/backlog)
- 🗂️ [Epics](https://tree.taiga.io/project/martinbergagno-recruitingapp/epics)
- 🏃 Sprint Boards available per iteration

## 📄 License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0).  
See the [LICENSE](LICENSE) file for details.

---

## 🔑 Test Users & Authentication

### Pre-loaded Test Users (Auto-created on first run)

**Admin User:**

- Email: `admin@recruitingapp.com`
- Password: `password123`
- Role: `ADMIN`

**Recruiter User:**

- Email: `recruiter@google.com`
- Password: `password123`
- Role: `RECRUITER`

**Candidate User:**

- Email: `candidate@example.com`
- Password: `password123`
- Role: `CANDIDATE`
- Skills: Java, Spring, React

### Obtaining JWT Tokens

**1. Login Endpoint:**

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "recruiter@google.com",
  "password": "password123"
}
```

**2. Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "recruiter@google.com",
  "role": "RECRUITER"
}
```

**3. Using Tokens:**

```http
Authorization: Bearer your_jwt_token_here
```

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request