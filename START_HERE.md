Start Here — Salary Management Assessment

This project is built using Java, Spring Boot, Angular, and SQLite.

Getting Started with Docker

1. Extract the project ZIP file.

2. Open Docker Desktop and wait until the status shows Engine running.

3. Open the extracted "Salary Assessment Submission" project folder.

4. Type "powershell" in the folder's address bar and press Enter.

5. Run the following command:
   
   docker compose up --build

6. An internet connection is required during the first build to download dependencies. The application seeds 10,000 fictional employee records during initial startup.

7. Open the frontend in your browser:
   
   "http://localhost:4200"

8. To check the backend health, open:
   
   "http://localhost:8080/api/health"
   
   Expected response:
   
   {"status":"ok"}

Stopping the Application

- Press "Ctrl + C" in the terminal.
- Run "docker compose down" to stop and remove the containers.

To reset the database, run:

docker compose down -v

Warning: This command deletes the database volume and saved salary changes.

Running Without Docker

Make sure the following tools are installed:

- JDK 21
- Maven 3.9 or later
- Node.js 20.11 or later
- npm

Start the backend:

1. Open a terminal in the "Backend" folder.

2. Run:
   
   mvn spring-boot:run

Start the frontend in a separate terminal:

1. Open a terminal in the "frontend" folder.

2. Run:
   
   npm install
npm start

