📚 Android Library Search App
This project marks my transition from Java Core to Android Development. It is a functional search application that allows users to find books by title or author within a local data structure.

🛠️ Technical Stack
Language: Kotlin

UI Framework: Android XML (Views)

Architecture: Model-View-Controller (MVC) logic

Minimum SDK: API 24 (Android 7.0 Nougat)

🚀 Features & Learnings
Efficient UI: Used LinearLayout and EditText to create a clean, responsive search interface.

Kotlin Data Classes: Leveraged Kotlin's data class to create a lightweight, readable Book model.

Interface Interaction: Implemented setOnClickListener to handle user input and trigger search logic.

Advanced Search Logic: Used Kotlin's .find and ignoreCase = true to provide a user-friendly search experience that matches both Titles and Authors.

State Control: Integrated a "Clear" function to reset the UI state and improve user experience.

📂 Project Structure
MainActivity.kt: Contains the core logic for finding views and executing the search algorithm.

activity_main.xml: The visual layout defining the application's interface.

<<<<<<< HEAD
Book.kt: The data model representing the library's contents.
=======
Book.kt: The data model representing the library's contents.
>>>>>>> 433abe533b07bf41886efd354d42213cc20ddf93
