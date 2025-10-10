# 💬 Java Client–Server Chat Application (Swing + Socket + File Handling)

A **Java-based Client–Server Chat Application** with a modern Swing GUI that supports real-time communication between two systems. The project demonstrates **Socket Programming**, **Multithreading**, and **File Handling** for logging chat sessions.

---

## 🚀 Features

✅ **Real-time Communication** – The client and server exchange messages instantly using TCP sockets.  
✅ **Swing GUI Interface** – Clean and easy-to-use graphical interface for both client and server.  
✅ **Enter Key to Send** – Messages can be sent by pressing **Enter** or clicking the **Send** button.  
✅ **Session Logging** – Every chat session (start and end) is automatically recorded in a text file with timestamps.  
✅ **Single Log File (Server-Side)** – The server maintains all conversation logs to prevent duplication.  
✅ **Graceful Exit** – “Exit” button or “close/byee” messages properly close sockets and streams.

---

## 🧠 Concepts Used

- Java **Socket Programming**
- **Multithreading** (for continuous message listening)
- **Swing GUI Components**
- **Event Handling (ActionListeners)**
- **File Handling** with `FileWriter`
- **DateTime API (LocalDateTime, DateTimeFormatter)**

---

## 🏗️ Project Structure

```
📦 ChatApplication
 ┣ 📜 ClientGUI.java
 ┣ 📜 ServerGUI.java
 ┣ 📜 chat_history.txt
 ┗ 📜 README.md
```

---

## ⚙️ How to Run the Project

### 🖥️ Step 1: Compile the Source Files
```bash
javac ServerGUI.java
javac ClientGUI.java
```

### 🧩 Step 2: Run the Server
Run the **ServerGUI** first to start listening for clients:
```bash
java ServerGUI
```
You’ll see:
```
Server started. Waiting for client...
```

### 💻 Step 3: Run the Client
On another system (or the same system with correct IP):
```bash
java ClientGUI
```
Make sure the IP address in the client code matches the server’s local IP:
```java
socket = new Socket("YOUR_SERVER_IP", 3333);
```

---

## 🗂️ Chat Logs

All chat messages and timestamps are automatically saved by the **server** in:
```
chat_history.txt
```

Example log:
```
===== New Chat Session (Server Started) =====
Session Start: 2025-10-07 00:45:54
Client: Hello
Server: Hi!
Client: How are you?
Server: I'm good!
Session End: 2025-10-07 00:48:18
===== Chat Ended =====
```

---

## 🧩 Future Enhancements

- 🔐 Add encryption for messages
- 🌐 Add multi-client support (group chat)
- 🧠 Include chat history viewer in GUI
- 💾 Auto-generate separate log files per session

---

## 👨‍💻 Developed By
**Bodhane Shubham**  


---
**🛠️Java Socket Chat Application Architecture**  
<img width="1494" height="607" alt="Java Socket Chat Application Architecture" src="https://github.com/user-attachments/assets/f361269d-3c1e-489e-b1b9-5b4a73d7e540" />

---

