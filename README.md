# Java Multithreaded Chat

A multi-user chat application built with a concurrent Java TCP server and a JavaFX desktop client, with message history persisted to SQLite.

## About the project

Built as a portfolio project to explore a different paradigm than my C#/.NET projects (web APIs) and my Python project (sequential ETL pipeline): concurrent, stateful, network programming. The server handles multiple simultaneous client connections using one thread per client, with careful synchronization around the shared list of connected clients to safely broadcast messages without race conditions.

## Tech stack

Java 21 · Maven · JavaFX 21 · SQLite JDBC · Gson · JUnit 5

## Features

- TCP server accepting multiple simultaneous client connections, each handled on its own thread
- Real-time message broadcast to all connected clients (excluding the sender, whose message is echoed locally)
- Join/leave notifications when a user connects or disconnects
- JSON-based message protocol (`JOIN` / `CHAT` message types) via Gson
- Message history persisted to SQLite on every message sent
- JavaFX desktop client with a graphical chat window
- Unit tests covering message serialization and database persistence

## Architecture

- `Main.java` - server entry point; accepts connections in a loop and spawns a thread per client
- `ClientHandler.java` - runs on its own thread per connected client; reads/parses messages, broadcasts to other clients, triggers persistence
- `Message.java` - plain data object representing a protocol message (type, nick, content, timestamp), serialized to/from JSON
- `Database.java` - JDBC access layer for SQLite; creates the table and saves messages
- `ChatGUI.java` - JavaFX desktop client
- `ChatClient.java` - terminal-based client, used during development to test the protocol before the GUI existed
- `Launcher.java` - separate entry point required to work around JavaFX's module-path requirements when running from a fat classpath

**Concurrency model:** the server keeps a shared `CopyOnWriteArrayList<ClientHandler>` of connected clients. This structure was chosen because the list is read far more often (every broadcast) than it's written (only on connect/disconnect), which is exactly the access pattern `CopyOnWriteArrayList` is optimized for, it avoids the need for manual `synchronized` blocks while still being thread-safe.

## Running locally

**Prerequisites:** JDK 21, Maven (or use the IDE's bundled Maven)

```bash
git clone https://github.com/Vitormjere/JavaChatApp.git
cd JavaChatApp
```

Open the project in an IDE (IntelliJ IDEA recommended) and let it resolve the Maven dependencies.

**Start the server:**
Run `Main.java`.

**Start a client:**
Run `Launcher.java` (not `ChatGUI.java` directly — `Launcher` works around a JavaFX module-path limitation). You can run multiple instances to simulate multiple users chatting.

On first connection, you'll be prompted for a nickname. Messages you send are broadcast to every other connected client and saved to `chat_history.db` in the project root.

## Tests

```bash
mvn test
```

Covers message JSON serialization/deserialization and database persistence, using an isolated test database file.

## Author

Vitor Miranda Jeremias — [GitHub](https://github.com/Vitormjere)
