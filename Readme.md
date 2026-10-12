# 🛍️ GiftyGo – Interactive QR-Driven 3D Online Shopping App

**GiftyGo** is an Android-based online shopping application developed using Kotlin that enhances the shopping experience through QR code scanning and interactive 3D product visualization. Users can browse products, scan QR codes to access 3D models, explore product details, manage favorites, and view cart items through an intuitive mobile interface.

The application uses Firebase services for authentication and product data management, while GitHub supports source code management and 3D model storage.

## ✨ Features

- **Firebase Email Authentication** – Secure user sign-up and login using Firebase Authentication.
- **QR Code Scanning** – Scan QR codes to access corresponding 3D product models.
- **Dynamic Product Listing** – Display product information retrieved from Firebase Realtime Database.
- **Interactive 3D Product Visualization** – Explore products using an interactive 3D preview.
- **Product Details** – View product information, including size, color, and price.
- **Favorites & Wishlist** – Like and save preferred products for easy access.
- **Shopping Cart** – Retrieve and display cart items using Firebase Cloud Firestore.
- **Cloud-Based Data Management** – Integrate Firebase services for authentication and product-related data.
- **Version Control** – Manage source code and 3D assets through GitHub.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Android application development |
| Android Studio | Development environment |
| Firebase Authentication | Email-based user authentication |
| Firebase Realtime Database | Product listing and real-time data retrieval |
| Firebase Cloud Firestore | Shopping cart data management |
| QR Code Scanning | Accessing product-specific 3D models |
| 3D Model Integration | Interactive product visualization |
| Git & GitHub | Version control and source code management |

## 📱 Application Workflow

1. **Authenticate** – Register or log in using email authentication.
2. **Browse Products** – Explore products displayed from Firebase Realtime Database.
3. **Scan QR Code** – Scan a product's QR code to access its associated 3D model.
4. **Explore in 3D** – Interact with the product's 3D preview.
5. **View Product Details** – Check available size, color, and price information.
6. **Save Favorites** – Like products for future reference.
7. **View Shopping Cart** – Access cart items retrieved from Cloud Firestore.

## 🚀 Getting Started

Follow these steps to run GiftyGo locally.

### Prerequisites

- Android Studio
- Android SDK compatible with the project configuration
- JDK version supported by the project's Gradle setup
- A Firebase project
- Git installed on your system

### Installation

**1. Clone the repository**

**2. Open the project**

Open Android Studio and select **Open** to import the cloned project.

**3. Configure Firebase**

- Create or select a project in the Firebase Console.
- Register the Android application using its package name.
- Download the `google-services.json` configuration file.
- Place the file in the app module directory.
- Enable Firebase Email/Password Authentication.
- Configure Firebase Realtime Database and Cloud Firestore according to the application requirements.
- Apply the required Firebase security rules.

**4. Sync and build**

Allow Gradle to synchronize the project dependencies. Resolve any configuration issues before building the application.

**5. Run the application**

Connect an Android device or launch an emulator, then run the app from Android Studio.

## 📦 Download APK

Try the latest version of the GiftyGo Android application:

👉 **[Download the Latest GiftyGo APK](https://github.com/Archana982/GiftygoApp/blob/master/GiftyGo.apk)**


## 📂 Project Components

The application includes the following major functional components:

- User authentication
- Product listing and data retrieval
- QR code scanning
- Interactive 3D product viewer
- Product detail screen
- Favorites management
- Shopping cart interface
- Firebase integration

The exact directory structure may vary depending on the current Android Studio project configuration.

## 🔐 Security Considerations

- Never commit private keys, passwords, or sensitive credentials to GitHub.
- Configure Firebase Authentication and database access rules appropriately.
- Restrict database access to authorized users where required.
- Validate product identifiers and QR code data before retrieving product information.
- Review the storage and access permissions for 3D model assets.

## 🔮 Future Enhancements

Potential improvements for future versions include:

- Online payment gateway integration
- Order placement and order tracking
- Product search, filtering, and sorting
- Product reviews and ratings
- User profile and address management
- Personalized product recommendations
- Optimized 3D model loading and caching
- Responsive web-based shopping experience

## Sample screenshots of GiftyGo App

### SignIn/SignUp Page 
<img width="700" height="1000" alt="dcc2e75b-03f2-443e-b915-35dabef57ffc" src="https://github.com/user-attachments/assets/8d3d80a4-b0f4-4228-9a27-72d96a93c30d" />

### Home Page
<img width="700" height="1000" alt="9376cfff-3bba-433a-bf86-976a3becc888" src="https://github.com/user-attachments/assets/71f46493-cd14-49dc-b2dc-e7d1c455a923" />

### Product Details Page
<img width="700" height="1000" alt="aa79c809-bef5-445c-874a-96ee55165cb0" src="https://github.com/user-attachments/assets/96156c2c-934d-42a2-86ce-f34c0b9efdf2" />


## 📄 License

A license has not yet been specified. Add an appropriate `LICENSE` file before distributing the project for reuse.

## 👨‍💻 Developed With

Built with **Kotlin, Android Studio, Firebase, QR code technology, and interactive 3D visualization** to create a more engaging mobile shopping experience.

**GiftyGo – Scan. Explore. Shop.**


