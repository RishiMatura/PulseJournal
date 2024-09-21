# 📝 Pulse Journal: Streamlining Personal Journaling with MongoDB and Spring Boot 📒 

## Overview

Pulse Journal is a personal journaling application designed to provide a seamless and efficient experience for managing journal entries. Built with Spring Boot for the backend and MongoDB for data storage, the application supports creating, updating, and deleting journal entries, as well as converting text entries into audio files using the ElevenLabs API for a unique voice journal experience.

## Features

- **Journal Management:** Create, update, delete, and view journal entries.
- **Text-to-Speech Integration:** Converts journal text into audio using the ElevenLabs API, automatically saving the audio when a new entry is created.
- **REST API:** Well-defined REST endpoints for journal and audio management.
- **MongoDB Integration:** Utilizes MongoDB for storing journal data.
- **Spring Boot:** Efficient and scalable backend powered by Spring Boot.
- **Environment Variable Management:** Securely handles sensitive configurations using environment variables.
- **Text-to-Speech** Conversion for Journal Entries.
- **Current Weather and Quotes API** (Retrieve real-time weather data and daily motivational quotes)
- **Role-Based Authentication** (Admin/User Permissions)
- **Auto Password Encoding** Automatically encodes user passwords for secure storage.
- **SMTP Integration:** Enables email functionality for notifications and user communication.

## Technologies Used

- **Java (Spring Boot):** Backend framework.
- **MongoDB:** NoSQL database for storing journal data.
- **ElevenLabs API:** Text-to-speech conversion for voice journal entries.
- **WeatherStack API:** Provides current weather data.
- **API Ninja:** Fetches quotes for the application.
- **Maven:** Build and dependency management.
- **Git:** Version control.
- **IntelliJ IDEA:** Development environment.
- **Postman:** API testing and documentation.

## Prerequisites

To run this project locally, you need the following:
- Java 21 
- Maven installed
- MongoDB installed or access to a MongoDB cloud instance
- IntelliJ IDEA (Optional but recommended for development)
- Postman for API testing
- ElevenLabs API key
- API Ninja Key
- MailSlurp or Ethereal Mail API Keys
- Weather Stack Key

## Setup and Installation


1. **Clone Repository:** Clone this repository to your local machine:
   ```sh
   git clone https://github.com/RishiMatura/PulseJournal.git
   
2. **Configure Environment Variables:** 
- Create an `env_var.env` file with the following content:

   ```sh
  SPRING_DATA_MONGODB_URI=<your_mongodb_uri>
  SPRING_DATA_MONGODB_DATABASE=<your_database_name>
  ELEVEN_LABS_API_KEY=<your_api_key>
   
- Make sure to replace the placeholders with your actual MongoDB URI, database name, and ElevenLabs API key.

3. **Build the Project:**
   ```sh
   mvn clean install
   
4. **Run the Application:**
    ```sh
    mvn spring-boot:run
    
5. **Access the Application:** Once the application is running, you can interact with the REST API through Postman:
    - http://localhost:8080/user
    - http://localhost:8080/journal

 **API Endpoints**

| Method   | Endpoint                                     | Description                               | Payload Example                          |
|----------|----------------------------------------------|-------------------------------------------|------------------------------------------|
| `POST`   | `/journal/post`                              | Create a new journal entry                | `{ "title": "Sample", "content": "..." }` |
| `GET`    | `/journal/getAll`                            | Fetch all journal entries                 | N/A                                      |
| `DELETE` | `/journal/id/{id}`                           | Deletes the journal by its ID             | N/A                                      |
| `GET`    | `/journal/id/{id}`                           | Fetch a journal entry by ID               | N/A                                      |
| `GET`    | `/admin/all-users`                           | Fetch all users (Admin access)            | N/A                                      |
| `POST`   | `/public/create-user`                        | Create a new user                         | `{ "username": "john_doe", "password": "..." }` |
| `PUT`    | `/user/update`                               | Update user information                   | `{ "username": "john_doe", "email": "..." }` |
| `GET`    | `/user`                                      | Fetch user details                        | N/A                                      |
| `DELETE` | `/user`                                      | Deletes the User along with all its entries| N/A                                      |

## Example Request

## Create a Journal Entry
POST localhost:8080/journal/post
Content-Type: application/json
- Requires Authentication :  UserName and Password

![image](https://github.com/user-attachments/assets/e1f2743c-31ab-4a86-9946-1aef0a2443f4)
- Request Snipped: 
  ```sh
     {
       "title": "A Day in the Life",
       "content": "Today was a productive day. I worked on the Plse Journal project."
     }
## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.
