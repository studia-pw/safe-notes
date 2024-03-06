# Safe-notes

## Features

- Securely store and manage your notes
- Create public or private notes
- Encrypt private notes for enhanced security
- Two-factor authentication using Google Authenticator
- Customize the style of your notes
- Add image to your note \
❌ Add malicious script to your note

## Technologies Used

- Spring Boot: Backend framework for building the RESTful API
- Angular: Frontend framework for creating the user interface
- Docker: Containerization for easy deployment and scalability

## Getting Started

1. Clone the repository to your local machine:

```
git clone https://github.com/studia-pw/safe-notes.git
```

2. Navigate to the project directory:

```
cd safe-notes
```

3. Build and run the Docker containers:

```
docker-compose up --build
```

4. Access the application in your web browser at `http://localhost:4200`.

## Usage

1. Sign in to the application using your Google Authenticator OTP code.
2. Create new notes by clicking on the "New Note" button.
3. Choose whether your note should be public or private.
4. If the note is private, choose whether to encrypt it.
5. Customize the styling of your notes using html syntax.
6. Save your note.

## Security

- All private notes are encrypted using industry-standard encryption algorithms.
- Two-factor authentication adds an extra layer of security to your account.
- HTTPS ensures secure communication between the client and server.
