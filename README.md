# chinesetexts

## Project Overview

This is a preliminary functional version of the final degree project (Trabajo Fin de Grado - TFG) for the Software Engineering Bachelor's Degree at Universidad Rey Juan Carlos (URJC). The final and updated version of the project can be found [here](https://github.com/codeurjc-students/2025-ChineseTexts).

## Setup

To setup the project locally, follow these steps.

**1. Clone this repository:**

```bash
    git clone https://github.com/josevictorgarcia/chinesetexts
```

**2. Run the MySQL container using Docker:**

```bash
    docker run --name docker-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=shushuguan -p 3306:3306 -d mysql
```

**3. Install angular dependencies:**

```bash
    cd chinesetexts
    npm install
```

**4. Run the backend application:**
 Open the backend application in Visual Studio Code with the Java Spring Boot extension, and start the application.

**5. Run the frontend application:**

```bash
     npm start
```

**6. Start Python OCR Service:**
If running locally, move to the ocr-service folder in this repository ```cd ocr-service```, open a new terminal and run the following commands to create a virtual environment and install dependencies:

```bash
    python3 -m venv venv
    source venv/bin/activate
    pip install -r requirements.txt
```

Then run the ocr-service:
```bash
    python paddleOCRService.py
```

NOTE. Useful links:
- [paddle-ocr](https://github.com/PaddlePaddle/PaddleOCR)
- [paddle-ocr quick start](https://www.paddleocr.ai/latest/en/quick_start.html)
- [paddlepaddle](https://github.com/PaddlePaddle/Paddle)
- [paddlepaddle official website](https://www.paddlepaddle.org.cn/en)

## Deployment

To deploy the project on a Virtual Machine (VM), follow these steps. Ensure that the VM is running Ubuntu 20.04 or similar Linux-based operating system.

**1. Set up the environment:**
Install Docker, Docker Compose, Angular, Java and all needed technologies used locally.

**2. Clone this repository in the VM:**

```bash
    git clone https://github.com/josevictorgarcia/chinesetexts
```

**3. Create Docker image:**

```bash
    chmod +x docker/create_image.sh
    docker/create_image.sh 1.0
```

**4. Run the app:**

```bash
    docker-compose -f docker/docker-compose.yml up --build
```

## App

The following section provides an overview of the app, screens and user interface.

### Home screen:

![Home Screen](/images/show-text.png)

### Text screen:

![Text Screen](/images/text-page.png)

### Profile screen:

![Profile Screen](/images/profile.png)

### Collections screen:

![Collections Screen](/images/collections-page.png)

### Flashcards screen:

![Flashcards Screen](/images/flashcards-page.png)

### Signup screen:

![Signup Screen](/images/signup-page.png)

### Add text screen:

![Add Text Screen](/images/addText-page.png)

## Navigation Diagram

The diagram below shows how users can navigate across different screens in the app.

![Navigation Diagram](/images/navigation-diagram.png)

## Permissions

| Action / Role   | Unregistered user | Registered user | Admin |
|--------------------|:-----:|:----------------:|:-----:|
| View texts    | ✅    | ✅               | ✅    |
| View text details (Words, Pinyin, ...)     | ✅    | ✅               | ✅    |
| Change language    | ✅    | ✅               | ✅    |
| View collections/flashcards    | ❌    | ✅               | ✅    |
| Create collections    | ❌    | ✅               | ✅    |
| Edit collections    | ❌    | ✅               | ✅    |
| Delete collections    | ❌    | ✅               | ✅    |
| Add flashcards to collections    | ❌    | ✅               | ✅    |
| Delete flashcards from collections    | ❌    | ✅               | ✅    |
| View profile info    | ❌    | ✅               | ✅    |
| Edit profile info    | ❌    | ✅               | ✅    |
| Sign up users    | ❌    | ❌               | ✅    |
| Add texts    | ❌    | ❌               | ✅    |
| Delete texts    | ❌    | ❌               | ✅    |
| Add words    | ❌    | ❌               | ✅    |
| Edit words    | ❌    | ❌               | ✅    |
| Delete users    | ❌    | ❌               | ✅    |

