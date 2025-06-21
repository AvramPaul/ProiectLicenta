# Social Media Application for Car Spotting and Automatic Recognition of Cars Using AI Model
  The application named **"Car Spotting AI"** is a mobile app for **Android** that integrates the AI model **ResNet18** for classifying car images by make, model and year. This is a social media style platform where users can create an account, log in, and create their posts about cars. Each uploaded photo is automatically classified and displayed in a public feed visible to all users. Users can react to others posts, also see their own posts and who liked or disliked them.
  The backend is implemented using **Spring Boot** in the IDE **IntelliJ Idea**, connected to the MySql database. The front-end is developed in **Android Studio**, using the programming language Java. The app is tested using the **AVD Manager**, with a **Pixel 9** emulator.

  ## Installation Guide
  
  ### What software you need: 
 * IntelliJ Idea : https://www.jetbrains.com/idea/
 * Java developement Kit  (JDK 21) :  https://www.oracle.com/java/technologies/downloads/
 * Download MySql Installer and install MySql Server and MySql Workbench : http://dev.mysql.com/downloads/installer/
 * Android Studio: https://developer.android.com/studio

 * Python 3.11 : https://www.python.org/downloads/release/python-3110/
 * For python libraries open Command Prompt or PowerShell and run the following: `pip install torch torchvision pandas numpy matplotlib`

  ### Running the application
  1. **Clone the repository** 
     * In git bash run command `git clone https://github.com/AvramPaul/ProiectLicenta.git` to clone the repository
  2. Import MySql database
     * Open MySQL Workbench 
     * Go to: Server → Data Import
     * Choose Import from Self-Contained File
     * Select the SQL dump folder: car_spotting_db (this file is provided in the repo)
     * Click Start Import
  3. Configure backend properties
     * Open the file `car-spotting-backend/src/main/resources/application.properties`
     * And update with you MySQL credentials
       ```bash
        server.port=8081
        spring.datasource.url=jdbc:mysql://localhost:3306/car_spotting
        spring.datasource.username=root
        spring.datasource.password=your_password
  4. Run Android App
     * Open in  `car-spotting-frontend` Android Studio
     * Use AVD Manager in Android Studio to download and run the **Pixel 9** emulator
