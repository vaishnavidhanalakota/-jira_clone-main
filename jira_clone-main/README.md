# Jira Clone — Full-stack starter

This project contains a Next.js frontend, Spring Boot REST API, and MongoDB persistence.

## Requirements
- Node.js 20+ and npm
- Java 17+ (Java 21 is also fine)
- Maven 3.9+ (or use the included `server/mvnw.cmd` wrapper on Windows)
- MongoDB local, or a MongoDB Atlas database

## 1. Configure MongoDB

### Option A — MongoDB Atlas
1. Create a cluster in MongoDB Atlas.
2. In **Database Access**, create a database user and password.
3. In **Network Access**, allow your current IP address.
4. Click **Connect → Drivers**, copy the connection string, and replace the username/password placeholders.
5. Open `server/src/main/resources/application.properties` and set the `MONGODB_URI` environment variable as shown below. Do not commit a real password to Git.

Windows Command Prompt (temporary for the current terminal):
```bat
set MONGODB_URI=mongodb+srv://DB_USER:DB_PASSWORD@YOUR_CLUSTER.mongodb.net/jira_clone?retryWrites=true&w=majority
```

If your password contains characters such as `@`, `:`, `/`, `#`, or `%`, URL-encode those characters in the connection string.

### Option B — Local MongoDB
Install and start MongoDB Community Server. The default URI is `mongodb://localhost:27017/jira_clone`, so no environment variable is required.

## 2. Start the backend
Open Command Prompt in the project folder:
```bat
cd server
mvnw.cmd spring-boot:run
```
If Maven is installed, `mvn spring-boot:run` also works. Keep this terminal open. Backend URL: `http://localhost:8080`.

Check the API:
- `http://localhost:8080/` — should say `Server is Running`
- `http://localhost:8080/health` — checks the MongoDB connection

## 3. Start the frontend
Open a second Command Prompt in the project folder:
```bat
cd client
copy .env.local.example .env.local
npm install
npm run dev
```
Open `http://localhost:3000`.

The frontend uses `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080` by default. If your backend runs elsewhere, edit `client/.env.local` and restart the frontend.

## Main API routes
- `POST /api/users/signup`
- `POST /api/users/login`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `GET, POST /api/projects`
- `GET, PUT, DELETE /api/projects/{id}`
- `POST /api/issues`
- `GET /api/issues/project/{projectId}`
- `GET, PUT, DELETE /api/issues/{id}`
- `POST /api/sprints`
- `GET /api/sprints/project/{projectId}`
- `PUT /api/sprints/{id}/start`
- `PUT /api/sprints/{id}/complete`

## Important security note
This is a learning/starter project, not a production-secured Jira replacement. Passwords are hashed and are not returned by the API, but API routes are not yet protected with JWT authorization and role-based access control. Add those before deploying publicly. Do not expose MongoDB credentials or commit `.env` files.
