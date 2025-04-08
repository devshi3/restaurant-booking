## Restaurant Booking System

A lightweight Java-based REST API with a simple HTML+JS frontend to manage restaurant table bookings. Built using **mu-server**, this Maven project simulates a real-world restaurant system with both customer and owner flows.

---

### Tech Stack

- **Java 11+**
- **Mu-Server** (Java web server)
- **Gson** (JSON serialization)
- **Maven** (Build tool)
- **Vanilla HTML + JS** (Frontend)

---

### 🚀 Run the App

1. **Clone and build:**
   ```bash
   git clone https://github.com/your-username/restaurant-booking.git
   cd restaurant-booking
   mvn clean install
   ```

2. **Run the server:**
   ```bash
   mvn exec:java -Dexec.mainClass="app.Main"
   ```

3. **Visit the homepage:**
   ```
   http://localhost:8080/
   ```

---

### 📝 Assumptions

To simplify the scope and focus on demonstrating core functionality, the following assumptions have been made:

- **No persistent storage**  
  All data (e.g., bookings, sessions) is stored **in memory** and lost when the server restarts.

- **Single owner user**  
  There is **only one hardcoded owner account**:  
  - Username: `admin`  
  - Password: `password123`

- **No user registration/authentication for customers**  
  Customers do **not need to log in**; they simply submit their booking form.

- **Date and time validation is minimal**  
  Assumes:
  - The customer inputs a valid future date
  - The selected time is from the predefined list

- **No concurrency handling**  
  Since this is a demo, it doesn’t handle concurrent access or race conditions (e.g., double-booking a table).

- **Frontend is served as static resources**  
  All HTML pages (`index.html`, `booking-form.html`, etc.) are located in the `resources/static/` directory and are served directly via the embedded server.

- **Basic cookie-based session**  
  Login sets a session cookie; no advanced JWT or OAuth used.

- **No CSS frameworks or build tools**  
  Styling is done using basic inline CSS without libraries like Bootstrap or Tailwind.

---

### 🧑‍🍳 User Roles

#### 1. Customer

- Goes to `index.html` and clicks **“I am a Customer”**
- Lands on `booking-form.html`
- Fills:
  - Name
  - Table size
  - Date (within next month)
  - Time slot (from predefined options)
- On successful booking, a confirmation message appears.

#### 2. Restaurant Owner

- Goes to `index.html` and clicks **“I am the Restaurant Owner”**
- Enters hardcoded credentials:
  - Username: `admin`
  - Password: `password123`
- Redirected to `owner-dashboard.html` after login.
- Can:
  - View all bookings or filter by date
  - Logout securely

---

### ⚙️ API Endpoints

| Method | Endpoint         | Description               |
|--------|------------------|---------------------------|
| POST   | `/login`         | Owner login               |
| POST   | `/logout`        | Logout and clear cookie   |
| POST   | `/booking`       | Create a new booking      |
| GET    | `/bookings`      | Get all bookings (owner)  |

---

### Future Improvements

- Connect to a real database
- Add registration & better auth
- Build analytics & reporting
- Deploy to cloud or Dockerize

---
