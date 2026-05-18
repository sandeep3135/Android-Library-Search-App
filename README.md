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

Add new feature implementation:
Book.kt: The data model representing the library's contents.

Memory-Efficient Scrolling: Uses a RecyclerView to display a long list of books without slowing down the phone by "recycling" row layouts as they move off-screen.

Dynamic Data Binding: Connects your Book.kt data class (Title and Author) to a custom design in item_book.xml using a "Bridge" called the BookAdapter.

Real-Time Search Filtering: Allows users to type into the search box to filter the list instantly. If the search box is empty, it resets to show the full library.

Improved User Interface (UI): Organizes the screen so the search tools stay at the top while the list takes up the remaining space.

Add new feature:
The "Dynamic Book Entry" Feature

This feature transforms the library from a "Read-Only" list into an interactive application where users can contribute data.

🛠️ What was implemented:
Mutable Data Handling: You upgraded the project logic from a fixed listOf to a mutableListOf, allowing the app to grow as the user adds data.

User Input Interface: You added two EditText fields (Title and Author) and an "Add" button to the main screen.

Adapter Synchronization: You implemented logic that takes user input, creates a new Book object, and tells the BookAdapter to refresh the RecyclerView using notifyDataSetChanged() (via your updateList function).

Input Validation: You added a check to ensure users don't add empty books to the library.

Add new feature:
🎓 Summary of what you just achieved:
Serialization: You learned how to turn complex objects into JSON strings so a computer can "remember" them. 
SharedPreferences: You mastered the standard way to store user settings and small data sets in Android.
Version Control: You successfully used a feature branch to troubleshoot and fix library errors before going live.

How did the test go? Is your new book still showing up after the restart?


New Features Implemented:
Professional CRUD & UI State Management:-

Update Logic: Integrated a standard click listener on list items to trigger an "Edit Book" dialog, allowing users to modify existing data.
Empty State UI: Built a dynamic layout that automatically shows a "Library is Empty" message with custom icons when the list is cleared.
UI Modernization: Refactored the layout using `FrameLayout` for layered views and added `elevation` and `padding` for a Material Design look.
Code Documentation: Added professional XML and Kotlin comments to explain the "why" behind the code structures.


New Features Implemented:
UI Personalization & Custom Themes

Layout & Styling Upgrades:- 
Custom Shape Drawables: Designed `bg_search_bar` and `bg_book_card` in XML to create fluid, modern capsule shapes and card layouts with precise border strokes.
Centralized Color Palette: Restructured `colors.xml` using a premium tech-focused theme (`primary_dark`, `accent_blue`, and `text_muted`) for uniform color management. 
Component Refactoring: Updated individual `item_book.xml` item rows to use elevation shadows, precise interior padding, and custom margins for clean list separation.


New Features Implemented:
Advanced Gesture Mechanics & Touch Safety:-

Bi-Directional Swiping: Integrated an `ItemTouchHelper` to detect fluid Left and Right swipe gestures across all list item cards.
System Gesture Interception: Engineered a fallback routine using `adapter.notifyItemChanged(position)` to safely catch accidental swipes caused by Android's global edge-to-back navigation.
Context-Aware Dialog Pools: Refactored the underlying Adapter signature to pass `bookList` dynamically, completely neutralizing position shifts during active live searches.


New Features Implemented:
Dynamic Dark Theme Architectures:-

Theme Inversion Toggle: Embedded an `AppCompatDelegate` toggle mechanism connected to an interactive top-bar asset action listener.
Semantic Asset Decoupling: Migrated vector drawables (`bg_search_bar` and `bg_book_card`) away from hardcoded color channels to dynamic system pointers (`?attr/colorSurface` and `?android:attr/colorBackground`).
Adaptive Typography Contracts: Refactored text rendering across elements to consume `?android:attr/textColorPrimary` and `textColorSecondary`, ensuring high-contrast visibility across runtime contexts.


New Features Implemented:
Visual Polish & Production Standardization:-

Adaptive Illustration States: Configured custom XML vector paths (`ic_empty_library`) to seamlessly bind against secondary text attributes, rendering beautiful, theme-aware graphics.
Production UX Refactoring: Cleaned up debug runtime triggers from core layout state listeners to isolate background thread reloads from intrusive user-facing notifications.
Final Build Consolidation: Merged all stable feature branches into production mainline, creating a clean, professional commit signature.