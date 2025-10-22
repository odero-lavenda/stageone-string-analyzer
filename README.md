# Stage One: String Analyzer API

A Spring Boot application that analyzes strings — detects palindromes, word count, unique characters, and more — and supports natural language filtering.

## 🚀 Features
- Analyze and store strings with computed metadata
- Retrieve string details by value
- Filter stored strings via structured filters or natural language
- Delete strings by value
- Returns SHA-256 hash and character frequency map

## 🧩 Tech Stack
- Java 17+
- Spring Boot
- JPA (Hibernate)
- H2 / PostgreSQL
- Lombok
- JUnit 5 + Mockito

## 📦 Endpoints
| Method | Endpoint | Description |
|--------|-----------|-------------|
| POST | `/api/strings` | Analyze and store a new string |
| GET | `/api/strings/{value}` | Retrieve analyzed string details |
| GET | `/api/strings` | Get all strings with filters |
| DELETE | `/api/strings/{value}` | Delete a string |
| POST | `/api/strings/natural` | Filter using natural language query |

## 🧠 Example Request (Analyze)
```bash
POST /api/strings
{
  "value": "madam"
}
🧪 Example Response
json
Copy code
{
  "value": "madam",
  "is_palindrome": true,
  "word_count": 1,
  "unique_characters": 3,
  "sha256_hash": "c1d9f50f86825a1a2302ec2449c17196...",
  "character_frequency": {"m": 2, "a": 2, "d": 1}
}
⚙️ Setup
bash
Copy code
git clone https://github.com/<your-username>/stageone-string-analyzer.git
cd stageone-string-analyzer
mvn spring-boot:run
✅ Tests
Run tests with:

bash
Copy code
mvn test
